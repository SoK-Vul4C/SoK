Affected version:
1.900.12

Source code:
git clone https://github.com/jasper-software/jasper.git

Commit hash:
b9be3d9f35fccb7811ff68bbd6a57156f0192427

Command:
> cd /path/to/compile/source
> ./src/appl/imginfo -f $FILE

Expected Output:
jpc_dec.c:1234:33: runtime error: signed integer overflow: 210 * -2147483646 cannot be represented in type 'int'
imginfo: jas_seq.c:90: jas_seq2d_create: Assertion `xstart <= xend && ystart <= yend' failed.
