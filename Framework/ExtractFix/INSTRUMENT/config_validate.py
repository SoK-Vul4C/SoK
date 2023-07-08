import os
import re
import shutil
from sys import exit as exit
if not os.path.exists("extractfix.log"):
    exit()
with open("extractfix.log" ,mode='r') as f:
    contents=f.read().split("\n")
with open("config",mode='r') as f:
    config=f.read()

index=0
os.mkdir("runtime")
for content in contents:
    if "diff" in content:
        l=content.strip().split(" ")
        relative_path=l[-4]
        pattern = r"project/(.*)"
        result = re.search(pattern, relative_path)
        if result:
            extracted_string = result.group(1)
        
        validate_dir=os.path.join("runtime","result{index}".format(index=index))
        shutil.copytree("result{index}".format(index=index),validate_dir)
        config_temp=config.replace("<fix-file-path>",extracted_string)
        with open(os.path.join(validate_dir,"config"),mode='w') as f:
            f.write(config_temp)
        index+=1

