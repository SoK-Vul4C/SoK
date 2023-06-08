Affected version:
20161021

Source code:
git clone https://github.com/davea42/libdwarf-code.git

Commit hash:
85438a1b30ba3bea0380354a866b1c49414abcfa

Command:
> cd /path/to/compile/source
> ./dwarfdump/dwarfdump $FILE

Expected Output:
dwarf_leb.c:306:19: runtime error: negation of -9223372036854775808 cannot be represented in type 'long long int'; cast to an unsigned type to negate this value to itself

