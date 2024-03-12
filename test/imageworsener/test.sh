#! /bin/bash
cd $1

./scripts/autogen.sh
./configure --disable-shared
make CFLAGS="-static -fno-omit-frame-pointer -ggdb" CXXFLAGS="-static -fno-omit-frame-pointer -ggdb" LDFLAGS="-fno-omit-frame-pointer" -j32
if [ $? -ne 0 ]; then
    exit 2
fi
output1=$(make check -j32)
echo $output1 > ../tmp1.txt

make clean


patch_paths=($(echo "$2" | tr '|' ' '))
buggy_paths=($(echo "$3" | tr '|' ' '))
count=${#patch_paths[@]}

for ((i=0; i<=$count; i++)); do
    cp ${buggy_paths[$i]} ./copy${i}.c
    cp ${patch_paths[$i]} ${buggy_paths[$i]}
done

./scripts/autogen.sh
./configure --disable-shared
make CFLAGS="-static -fno-omit-frame-pointer -ggdb" CXXFLAGS="-static -fno-omit-frame-pointer -ggdb" LDFLAGS="-fno-omit-frame-pointer" -j32

if [ $? -ne 0 ]; then
    make clean
    for ((i=0; i<=$count; i++)); do
        mv ./copy${i}.c ${buggy_paths[$i]}
    done
    exit 2
fi
output2=$(make check -j32)
echo $output2 > ../tmp2.txt


for ((i=0; i<=$count; i++)); do
    mv ./copy${i}.c ${buggy_paths[$i]}
done

make clean

if diff -q ../tmp1.txt ../tmp2.txt >/dev/null; then
    rm -rf ../tmp1.txt ../tmp2.txt
    exit 0
else
    rm -rf ../tmp1.txt ../tmp2.txt
    exit 1
fi
