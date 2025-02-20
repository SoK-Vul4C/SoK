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

#ifndef _SRC_PARSER_H__
#define _SRC_PARSER_H__

#include "std_comp.h"

extern struct trans_config TRANS_CFG;

/* Source Parser API */
int src_parser_cpp(const char *src, const struct trans_config *cfg);

#endif /* _SRC_PARSER_H__ */
