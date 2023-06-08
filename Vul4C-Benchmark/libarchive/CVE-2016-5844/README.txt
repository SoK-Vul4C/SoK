Affected version:
3.2.0

Source code:
git clone https://github.com/libarchive/libarchive.git

Commit hash:
167e97be1d35c1e0947d768adbf94712244aad6b

Command:
> cd /path/to/compile/source
> ./bsdtar -tf $FILE

Expected Output:
libarchive/archive_read_support_format_iso9660.c:1094:32: runtime error: signed integer overflow: 8388631 * 2048 cannot be represented in type 'int'

