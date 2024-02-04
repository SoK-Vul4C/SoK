Affected version:


Source code:
https://github.com/trgil/gilcc

Commit hash:
9aa8b0b

Command:
> cd /path/to/compile/source
> ./src/gilcc $FILE

Expected Output:
=================================================================
==880437==ERROR: AddressSanitizer: stack-buffer-overflow on address 0x7fffffffe01c at pc 0x5555554027b4 bp 0x7fffffffdef0 sp 0x7fffffffdee0
READ of size 1 at 0x7fffffffe01c thread T0
    #0 0x5555554027b3 in src_parser_trans_stage_1_2_3 /5k_add/gilcc/CVE-2020-21572/source/src/src_parser.c:156
    #1 0x555555403144 in src_parser_cpp /5k_add/gilcc/CVE-2020-21572/source/src/src_parser.c:228
    #2 0x555555403d77 in main /5k_add/gilcc/CVE-2020-21572/source/src/gilcc.c:231
    #3 0x7ffff6a48c86 in __libc_start_main (/lib/x86_64-linux-gnu/libc.so.6+0x21c86)
    #4 0x555555401c69 in _start (/5k_add/gilcc/CVE-2020-21572/source/src/gilcc+0x1c69)

