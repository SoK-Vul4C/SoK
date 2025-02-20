Affected version:
1.900.13

Source code:
git clone https://github.com/jasper-software/jasper.git

Commit hash:
c62014d09d2fdaaad24f40142914c155c669bf7a

Command:
> cd /path/to/compile/source
> ./src/appl/imginfo -f $FILE

Expected Output:
imginfo: jas_seq.c:90: jas_seq2d_create: Assertion `xstart <= xend && ystart <= yend' failed.
