#! /bin/bash

cd $1

make clean
CC=clang-10 CXX=clang++-10 ./autogen.sh --disable-docs CFLAGS="-fno-omit-frame-pointer -g -Wno-narrowing" CXXFLAGS="-fno-omit-frame-pointer -g -Wno-narrowing"
make -j10
if [ $? -ne 0 ]; then
    exit 2
fi
output1=$(make check -j10)

total1=$(echo "$output1" | grep -oP '# TOTAL: \K\d+')
pass1=$(echo "$output1" | grep -oP '# PASS:  \K\d+')
skip1=$(echo "$output1" | grep -oP '# SKIP:  \K\d+')
xfail1=$(echo "$output1" | grep -oP '# XFAIL: \K\d+')
fail1=$(echo "$output1" | grep -oP '# FAIL:  \K\d+')
xpass1=$(echo "$output1" | grep -oP '# XPASS: \K\d+')
error1=$(echo "$output1" | grep -oP '# ERROR: \K\d+')

echo "TOTAL: $total1"
echo "PASS:  $pass1"
echo "SKIP:  $skip1"
echo "XFAIL: $xfail1"
echo "FAIL:  $fail1"
echo "XPASS: $xpass1"
echo "ERROR: $error1"

make clean

patch_paths=($(echo "$2" | tr '|' ' '))
buggy_paths=($(echo "$3" | tr '|' ' '))
count=${#patch_paths[@]}

for ((i=0; i<=$count; i++)); do
    cp ${buggy_paths[$i]} ./copy${i}.c
    cp ${patch_paths[$i]} ${buggy_paths[$i]}
done

CC=clang-10 CXX=clang++-10 ./autogen.sh --disable-docs CFLAGS="-fno-omit-frame-pointer -g -Wno-narrowing" CXXFLAGS="-fno-omit-frame-pointer -g -Wno-narrowing"
make -j10
if [ $? -ne 0 ]; then
    make clean
    for ((i=0; i<=$count; i++)); do
        mv ./copy${i}.c ${buggy_paths[$i]}
    done
    exit 2
fi
output2=$(make check -j10)

total2=$(echo "$output2" | grep -oP '# TOTAL: \K\d+')
pass2=$(echo "$output2" | grep -oP '# PASS:  \K\d+')
skip2=$(echo "$output2" | grep -oP '# SKIP:  \K\d+')
xfail2=$(echo "$output2" | grep -oP '# XFAIL: \K\d+')
fail2=$(echo "$output2" | grep -oP '# FAIL:  \K\d+')
xpass2=$(echo "$output2" | grep -oP '# XPASS: \K\d+')
error2=$(echo "$output2" | grep -oP '# ERROR: \K\d+')

echo "TOTAL: $total2"
echo "PASS:  $pass2"
echo "SKIP:  $skip2"
echo "XFAIL: $xfail2"
echo "FAIL:  $fail2"
echo "XPASS: $xpass2"
echo "ERROR: $error2"

for ((i=0; i<=$count; i++)); do
    mv ./copy${i}.c ${buggy_paths[$i]}
done

make clean

if [ "$total1" != "$total2" ] || [ "$pass1" != "$pass2" ] || [ "$skip1" != "$skip2" ] || [ "$xfail1" != "$xfail2" ] || [ "$fail1" != "$fail2" ] || [ "$xpass1" != "$xpass2" ] || [ "$error1" != "$error2" ];
then
    exit 1
else
    exit 0
fi