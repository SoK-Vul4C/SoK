/************************************************************************
 * Copyright (c) 2019, Gil Treibush
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the full GNU General Public License is included in this
 * distribution in a file called "COPYING" or "LICENSE".
 ***********************************************************************/

#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

#include "src_parser.h"

#define TMP_FILE_NAME           ".gilcc-tmpfile-XXXXXX"
#define TMP_FILE_NAME_SIZE      22

/* Parse states: */
#define P_STATE_CODE        0
#define P_STATE_COMMENT_C   1

/* Parser file-buffer/stack */
#define PSTACK_BUF_SIZE     2
#define PARSER_BUF_SIZE     200

struct pbuf {
    char pbuf_buf[PARSER_BUF_SIZE];
    int pbuf_content_size;
    int pbuf_indx;
};

#define PBUF_CUR_CHAR(B) (B->pbuf_buf[B->pbuf_indx])
#define PBUF_DATA_SIZE(B) (B->pbuf_content_size - B->pbuf_indx)
#define PBUF_ADVN(B) (B->pbuf_indx++)
#define PBUF_CLEAR(B) (B->pbuf_indx = 0)
#define PBUF_EMPTY(B) (!(PBUF_DATA_SIZE(B)))
#define PBUF_NOT_EMPTY(B) (!(PBUF_EMPTY(B)))

static int pbuf_fill(struct pbuf *buf, const int ifd)
{
    int read_size;

    if (PBUF_NOT_EMPTY(buf))
        return PBUF_DATA_SIZE(buf);

    read_size = read(ifd, buf->pbuf_buf, PARSER_BUF_SIZE);
    buf->pbuf_indx = 0;
    buf->pbuf_content_size = read_size;

    return read_size;
}

static inline int pbuf_write_char(struct pbuf *buf, const int ofd)
{
    return write(ofd, &buf->pbuf_buf[buf->pbuf_indx], 1);
}

struct pstack {
    char pstack_buf[PSTACK_BUF_SIZE];
    int pstack_indx;
};

#define PSTACK_DATA_SIZE(S) (S.pstack_indx)
#define PSTACK_EMPTY(S) (!(PSTACK_DATA_SIZE(S)))
#define PSTACK_NOT_EMPTY(S) (!(PSTACK_EMPTY(S)))
#define PSTACK_PUSH_CHAR(S, C) (S.pstack_buf[S.pstack_indx++] = C)
#define PSTACK_POP_N(S, N) (S.pstack_indx -= N)
#define PSTACK_CLEAR(S) (S.pstack_indx = 0)
#define PSTACK_GET_ELEMENT(S, L) (S.pstack_buf[L - 1])
#define PSTACK_GET_ELEMENT_REV(S, L) (S.pstack_buf[S.pstack_indx - L])

static inline int pstack_write(struct pstack *stk, const int ofd)
{
    int write_size;

    if (!stk->pstack_indx)
        return 0;

    if ((write_size = write(ofd, stk->pstack_buf, stk->pstack_indx)) < 0)
        return write_size;

    stk->pstack_indx = 0;

    return write_size;
}

static inline int pstack_shift_write_n(struct pstack *stk, const int ofd, const int n)
{
    int write_size;
    int i;

    if (!stk->pstack_indx)
        return 0;

    if (n > stk->pstack_indx)
        return -1;

    if ((write_size = write(ofd, stk->pstack_buf, n)) < 0)
        return write_size;

    for (i = 0; (i + n) < stk->pstack_indx; i++)
        stk->pstack_buf[i] = stk->pstack_buf[(i + n)];

    stk->pstack_indx -= n;

    return write_size;
}

static void print_file_full(int fd)
{
    char f_buf[PARSER_BUF_SIZE];
    int read_size;

    if (lseek(fd, 0, SEEK_SET)) {
        fprintf(stderr, "**Error: Could not set offset.\n");
        return;
    }

    while ((read_size = read(fd, f_buf, PARSER_BUF_SIZE)) > 0) {
        int read_indx = 0;

        while (read_indx < read_size)
            putchar(f_buf[read_indx++]);
    }
}

static int src_parser_tstage_1_2_3_c_cmnt(  const int tmp_fd,
                                            const int src_fd,
                                            struct pbuf *buf,
                                            const struct trans_config *cfg)
{
    struct pstack stk = {
        .pstack_indx = 0
    };

    while (PBUF_DATA_SIZE(buf) || (pbuf_fill(buf, src_fd) > 0)) {
        switch (PBUF_CUR_CHAR(buf)) {
        case '*':
            PSTACK_PUSH_CHAR(stk, '*');
            break;

        case '/':
            PSTACK_PUSH_CHAR(stk, '/');
            break;

        default:
            PSTACK_CLEAR(stk);
            break;
        }

        if (PSTACK_DATA_SIZE(stk) > 1) {
            if (    (PSTACK_GET_ELEMENT_REV(stk, 2) == '*') &&
                    (PSTACK_GET_ELEMENT_REV(stk, 1) == '/')) {
                PBUF_ADVN(buf);
                return 0;
            } else {
                if (pstack_shift_write_n(&stk, tmp_fd, 1) < 0)
                    return -1;
            }
        }

        PBUF_ADVN(buf);
    }

    /* If we get here, we have reached the end of the file without ending the
     * comment.
     */

    return -1;
}

static int src_parser_tstage_1_2_3_default( const int tmp_fd,
                                            const int src_fd,
                                            struct pbuf *buf,
                                            const struct trans_config *cfg)
{
    struct pstack stk = {
        .pstack_indx = 0
    };

    pbuf_fill(buf, src_fd);

    while (PBUF_DATA_SIZE(buf) || (pbuf_fill(buf, src_fd) > 0)) {

        switch (PBUF_CUR_CHAR(buf)) {
        case ' ':
        case '\t':
            PSTACK_PUSH_CHAR(stk, ' ');
            break;

        case '\r':
        case '\n':
            PSTACK_PUSH_CHAR(stk, '\n');
            break;

        case '\\':
            PSTACK_PUSH_CHAR(stk, '\\');
            break;

        case '/':
            PSTACK_PUSH_CHAR(stk, '/');
            break;

        case '*':
            PSTACK_PUSH_CHAR(stk, '*');
            break;

        default:
            pstack_write(&stk, tmp_fd);
            pbuf_write_char(buf, tmp_fd);
            PBUF_ADVN(buf);
            continue;
        }

        if (PSTACK_DATA_SIZE(stk) > 1) {
            if ((PSTACK_GET_ELEMENT_REV(stk, 2) == ' ') &&
                (PSTACK_GET_ELEMENT_REV(stk, 1) == ' ')) {
                PSTACK_POP_N(stk, 1);

            } else if ( (PSTACK_GET_ELEMENT_REV(stk, 2) == '\n') &&
                        (PSTACK_GET_ELEMENT_REV(stk, 1) == '\n')) {
                PSTACK_POP_N(stk, 1);

            } else if ( (PSTACK_GET_ELEMENT_REV(stk, 2) == '\n') &&
                        (PSTACK_GET_ELEMENT_REV(stk, 1) == ' ')) {
                PSTACK_POP_N(stk, 1);

            } else if ( (PSTACK_GET_ELEMENT_REV(stk, 2) == ' ') &&
                        (PSTACK_GET_ELEMENT_REV(stk, 1) == '\n')) {
                PSTACK_POP_N(stk, 2);
                PSTACK_PUSH_CHAR(stk, '\n');

            } else if ( (PSTACK_GET_ELEMENT_REV(stk, 2) == '/') &&
                        (PSTACK_GET_ELEMENT_REV(stk, 1) == '*')) {
                PSTACK_POP_N(stk, 2);
                if (src_parser_tstage_1_2_3_c_cmnt(tmp_fd, src_fd, buf, cfg) < 0)
                    return -1;

            } else if ( (PSTACK_GET_ELEMENT_REV(stk, 2) == '\\') &&
                        (PSTACK_GET_ELEMENT_REV(stk, 1) == '\n')) {
                PSTACK_POP_N(stk, 2);

            } else {

                if (pstack_shift_write_n(&stk, tmp_fd, 1) < 0)
                    return -1;
            }
        }

        PBUF_ADVN(buf);
    }

    return 0;
}

static int src_parser_tstage_1_2_3(const int tmp_fd, const char *src, const struct trans_config *cfg)
{
    struct pbuf buf = {
        .pbuf_indx = 0,
        .pbuf_content_size = 0
    };

    int src_fd;
    int ret_val;

    src_fd = open(src, O_RDONLY);
    if (src_fd == -1) {
        fprintf(stderr, "**Error: Could not open source file: %s.\n", src);
        return -1;
    }

    ret_val = src_parser_tstage_1_2_3_default(tmp_fd, src_fd, &buf, cfg);

    close(src_fd);

    return ret_val;
}

int src_parser_cpp(const char *src, const struct trans_config *cfg)
{
    int tmp_fd;
    char fname[TMP_FILE_NAME_SIZE];
    int ret_val;

    strncpy(fname, TMP_FILE_NAME, TMP_FILE_NAME_SIZE);
    tmp_fd = mkstemp(fname);
    if (tmp_fd == -1) {
        fprintf(stderr, "**Error: could not create a working file.\n");
        return -1;
    }

    ret_val = src_parser_tstage_1_2_3(tmp_fd, src, cfg);
    if (ret_val < 0)
        return ret_val;

    print_file_full(tmp_fd);
    unlink(fname);

    return 0;
}

