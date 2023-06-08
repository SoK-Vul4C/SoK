Affected version:
master at 2017-04-12

Source code:
git clone git://sourceware.org/git/binutils-gdb.git

Commit hash:
ef6a5ae7bd1dd7b528f5cf368d98056603003c35

Command:
> cd /path/to/compile/source
> ./binutils/readelf -a $FILE

Expected Output:
readelf.c:16941:18: runtime error: shift exponent 64 is too large for 64-bit type 'long unsigned int'

