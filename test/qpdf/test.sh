#! /bin/bash
cd $1

./autogen.sh
./configure --enable-shared=no
make CFLAGS="-fno-omit-frame-pointer -g" CXXFLAGS="-fno-omit-frame-pointer -g" LDFLAGS="-fno-omit-frame-pointer" -j32
if [ $? -ne 0 ]; then
    exit 2
fi
output1=$(make check -j32)


total1=$(echo "$output1" | grep -oP 'Total tests: \K\d+')
pass1=$(echo "$output1" | grep -oP 'Passes: \K\d+')
fail1=$(echo "$output1" | grep -oP 'Failures: \K\d+')
unexpass1=$(echo "$output1" | grep -oP 'Unexpected Passes: \K\d+')
expfail1=$(echo "$output1" | grep -oP 'Expected Failures: \K\d+')
misstest1=$(echo "$output1" | grep -oP 'Missing Tests: \K\d+')
extratest1=$(echo "$output1" | grep -oP 'Extra Tests: \K\d+')

echo "Total tests: $total1"
echo "Passes:  $pass1"
echo "Failures:  $fail1"
echo "Unexpected Passes: $unexpass1"
echo "Expected Failures:  $expfail1"
echo "Missing Tests: $misstest1"
echo "Extra Tests: $extratest1"

make clean

patch_paths=($(echo "$2" | tr '|' ' '))
buggy_paths=($(echo "$3" | tr '|' ' '))
count=${#patch_paths[@]}

for ((i=0; i<=$count; i++)); do
    cp ${buggy_paths[$i]} ./copy${i}.c
    cp ${patch_paths[$i]} ${buggy_paths[$i]}
done

./autogen.sh
./configure --enable-shared=no
make CFLAGS="-fno-omit-frame-pointer -g" CXXFLAGS="-fno-omit-frame-pointer -g" LDFLAGS="-fno-omit-frame-pointer" -j32
if [ $? -ne 0 ]; then
    make clean
    for ((i=0; i<=$count; i++)); do
        mv ./copy${i}.c ${buggy_paths[$i]}
    done
    exit 2
fi
output2=$(make check -j32)

total2=$(echo "$output2" | grep -oP 'Total tests: \K\d+')
pass2=$(echo "$output2" | grep -oP 'Passes: \K\d+')
fail2=$(echo "$output2" | grep -oP 'Failures: \K\d+')
unexpass2=$(echo "$output2" | grep -oP 'Unexpected Passes: \K\d+')
expfail2=$(echo "$output2" | grep -oP 'Expected Failures: \K\d+')
misstest2=$(echo "$output2" | grep -oP 'Missing Tests: \K\d+')
extratest2=$(echo "$output2" | grep -oP 'Extra Tests: \K\d+')

echo "Total tests: $total2"
echo "Passes:  $pass2"
echo "Failures:  $fail2"
echo "Unexpected Passes: $unexpass2"
echo "Expected Failures:  $expfail2"
echo "Missing Tests: $misstest2"
echo "Extra Tests: $extratest2"

for ((i=0; i<=$count; i++)); do
    mv ./copy${i}.c ${buggy_paths[$i]}
done

make clean

if [ "$total1" != "$total2" ] || [ "$pass1" != "$pass2" ] || [ "$fail1" != "$fail2" ] || [ "$unexpass1" != "$unexpass2" ] || [ "$expfail1" != "$expfail2" ] || [ "$misstest1" != "$misstest2" ] || [ "$extratest1" != "$extratest2" ];
then
    exit 1
else
    exit 0
fi