Affected version:
4.0.7

Source code:
git clone https://github.com/vadz/libtiff.git

Commit hash:
3144e57770c1e4d26520d8abee750f8ac8b75490

Command:
> cd /path/to/compile/source
> ./tools/tiffcp -i $FILE /tmp/foo

Expected Output:
tif_jpeg.c:1646:19: runtime error: shift exponent 136 is too large for 64-bit type 'long int'
