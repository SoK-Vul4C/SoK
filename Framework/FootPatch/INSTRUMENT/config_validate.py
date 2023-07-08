import os
import shutil
import re
import subprocess
def execute_command(command: str, show_output=True, env=dict(), dir=None):
    if not dir:
        dir = os.getcwd()
    proc = subprocess.Popen([command], stdout=subprocess.PIPE, shell=True, env=env, cwd=dir)
    output, error = proc.communicate()
    
    return int(proc.returncode),output.decode('utf-8')

with open("config",mode='r') as f:
    config=f.read()

this_dir=os.path.dirname(os.path.abspath(__file__))
find_dir=os.path.join(this_dir,'source/infer-out')
exec_cmd="find {find_dir} -maxdepth 3 -name patches".format(find_dir=find_dir)
rc,output=execute_command(exec_cmd)

outputlist=output.split('\n')
patches_dir='temp'
os.mkdir(patches_dir)
for dir in outputlist:
    src_dir=dir.strip()
    if src_dir=="":
        continue
    for file in os.listdir(src_dir):
        src_file=os.path.join(src_dir,file)
        tgt_file=os.path.join(patches_dir,file)
        shutil.copy(src_file,tgt_file)

patches_list=os.listdir(patches_dir)
runtime_dir='runtime'
os.mkdir(runtime_dir)
for patch_file in patches_list:
    validate_dir=os.path.join(runtime_dir,patch_file)
    os.mkdir(validate_dir)
    
    #replace config file
    patch_file_path=os.path.join(patches_dir,patch_file)
    with open(patch_file_path,mode='r') as f:
        first_line=f.readline()

    buggy_program_abs_path=first_line.strip().split('\t')[0]
    buggy_program_abs_path=buggy_program_abs_path.split(' ')[1]
    pattern = r"source/(.*)"
    result = re.search(pattern, buggy_program_abs_path)
    if result:
        buggy_program_relative_path = result.group(1)
    
    config_temp=config.replace("<fix-file-path>",buggy_program_relative_path)
    with open(os.path.join(validate_dir,"config"),mode='w') as f:
        f.write(config_temp)
    
    patch_cmd="patch {buggy_file} -i {patch} -o {patched_file}".format(buggy_file=buggy_program_abs_path,patch=patch_file_path,patched_file=os.path.join(validate_dir,"patch_file"))
    rc,output=execute_command(patch_cmd)
