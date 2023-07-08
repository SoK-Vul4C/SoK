import os
import subprocess
from sys import exit
import copy

def parse_patch_file(file):
    with open(file, mode='r') as f:
        contents=f.read().split('\n')
    
    change_list=[]
    temp=[]
    for content in contents:
        if content.startswith("File"):
            if temp !=[]:
                change_list.append(copy.deepcopy(temp))
            temp.clear()
        temp.append(content.strip())
    change_list.append(temp)
    file2line={}
    file_line2code={}
    for patch_fragment in change_list:
        relative_path=""
        line=-1
        add_code=[]
        for content in patch_fragment:
            if content.startswith("File"):
                dir, filename = os.path.split(content.split(' ')[1])
                rc,outputs=execute_command("find {this_dir}/source -name {filename}".format(this_dir=this_dir,filename=filename))
                temp_files=[]
                for temp_file in outputs.split('\n'):
                    if temp_file.strip()!='':
                        temp_files.append(temp_file)
                if len(temp_files)==0:
                    continue
                if len(temp_files)!=1:
                    exit(1)
                abs_path=temp_files[0]
                index = abs_path.find("source")
                relative_path = abs_path[index + len("source/"):]
            elif content.startswith("Line") :
                line=int(content.split(' ')[1])
            elif content.startswith("+"):
                add_code.append(content[1:]+'\n')
        if relative_path not in file2line:
            file2line[relative_path]=[line]
            file_line2code[relative_path+":"+str(line)]=add_code
        elif line not in file2line[relative_path]:
            file2line[relative_path].append(line)
            file_line2code[relative_path+":"+str(line)]=add_code
    return file2line,file_line2code


def execute_command(command, show_output=True, env=dict(), dir=None):
    if not dir:
        dir = os.getcwd()
    proc = subprocess.Popen([command], stdout=subprocess.PIPE, shell=True, env=env, cwd=dir)
    output, error = proc.communicate()
    return int(proc.returncode),output.decode('utf-8')

os.mkdir("runtime")
this_dir=os.path.dirname(os.path.abspath(__file__))
patch_file=""
for file in os.listdir():
    if ".bc.patch" in file:
        patch_file=file
if patch_file=="":
    exit()
file2line,file_line2code=parse_patch_file(patch_file)

patch_dir="runtime/patch"
os.mkdir(patch_dir)
source_dir=os.path.join(this_dir,"source")

buggy_files=""
for file in file2line.keys():
    buggy_files=buggy_files+file+"|"

    file2line[file].sort()
    increment=0
    file_name=os.path.split(file)[1]
    with open(os.path.join(source_dir,file),mode='r') as f:
        contents=f.readlines()

    for line in file2line[file]:
        add_code=file_line2code[file+":"+str(line)]
        add_code_len=len(add_code)
        contents=contents[:line-1+increment]+add_code+contents[line-1+increment:]
        increment+=add_code_len
    
    with open(os.path.join(patch_dir,file_name),mode='w') as f:
        f.writelines(contents)

with open('config',mode='r') as f:
    config=f.read()

config_temp=config.replace("<fix-file-path>",buggy_files)
with open(os.path.join(patch_dir,"config"),mode='w') as f:
    f.write(config_temp)
