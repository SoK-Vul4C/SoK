Affected version:
3.04

Source code:
https://github.com/Matthias-Wandel/jhead.git

Commit hash:
e64e904

Command:
> cd /path/to/compile/source
> ./jhead $FILE

Expected Output:
=================================================================
==695==ERROR: AddressSanitizer: heap-buffer-overflow on address 0x602000000014 at pc 0x55555541581d bp 0x7ffffffeeac0 sp 0x7ffffffeeab0
READ of size 1 at 0x602000000014 thread T0
    #0 0x55555541581c in process_COM /5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jpgfile.c:51
    #1 0x555555416251 in ReadJpegSections /5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jpgfile.c:240
    #2 0x555555416b4b in ReadJpegFile /5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jpgfile.c:378
    #3 0x555555411b63 in ProcessFile /5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jhead.c:905
    #4 0x555555415436 in main /5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jhead.c:1756
    #5 0x7ffff66aac86 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x21c86)
    #6 0x55555540e739 in _start (/5k_add/Matthias-Wandel_jhead/CVE-2020-28840/source/jhead+0xe739)

