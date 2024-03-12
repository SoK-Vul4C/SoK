import os
import argparse
import sys
from tool.vulnfix import VulnFix
from tool.extractfix import ExtractFix
from tool.senx import Senx
from tool.vrepair import VRepair
from tool.vulrepair import VulRepair
from tool.validate import Validate
from tool.saver import Saver
from tool.footpatch import FootPatch
from tool.intpti import IntPTI
from tool.data import Data
from tool.test import Test

from logger import *
import subprocess
import time

def execute_command(command: str, show_output=True, env=dict(), dir=None):
    if not dir:
        dir = os.getcwd()
    logger.info(f"[{dir}] {command}")

    proc = subprocess.Popen([command], stdout=subprocess.PIPE, shell=True, env=env, cwd=dir)
    output, error = proc.communicate()
    
    return int(proc.returncode)

def cp_from_container(container_id: str, from_path: str, to_path: str):
    copy_command = f"docker cp {container_id}:{from_path} {to_path}"
    execute_command(copy_command)


def cp_to_container(container_id: str, from_path: str, to_path: str):
    copy_command = f"docker cp {from_path} {container_id}:{to_path}"
    execute_command(copy_command)

def parse_config(cve_runtime_dir, container_dir):
    cve_config=os.path.join(cve_runtime_dir, "config")
    if not os.path.exists(cve_config):
        return None
    with open(cve_config,mode="r") as f:
        contents=f.read().split("\n")
    dic={"binary":None,"cmd":None,"exploit":None,"build-cmd":None,"fix-file-path":None,"fix-loc":None,"crash-file-path":None,"crash-loc":None}
    for content in contents:
        if content!="":
            l=content.split("=")
            dic[l[0]]=l[1].strip()
    
    d=Data(cve_runtime_dir, container_dir, dic["binary"], dic["cmd"], dic["exploit"], dic["build-cmd"], dic["fix-file-path"], dic["fix-loc"], dic["crash-file-path"], dic["crash-loc"])
    return d

def main():
    parser = argparse.ArgumentParser(description="Repair via Vul4C.")
    parser.add_argument('--tool', type=str,
                        help='Tools for vulnerability repair.')
    parser.add_argument('--software', type=str,
                        help='Software to repair.')
    parser.add_argument('--CVEID', type=str,
                        help='CVEID to repair.')

    parsed_args = parser.parse_args()
    tool = parsed_args.tool
    software=parsed_args.software
    cveid= parsed_args.CVEID

    print(tool)
    print(cveid)

    stamp=str(int(time.time()))
    root_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    Framework_dir=os.path.dirname(os.path.abspath(__file__))
    tool_config_dir=os.path.join(Framework_dir,tool)
    software_dir=os.path.join(tool_config_dir,software)
    cve_dir=os.path.join(software_dir,cveid)
    
    result_root_dir=os.path.join(root_dir,"vul4c-result")
    if not os.path.exists(result_root_dir):
        os.mkdir(result_root_dir)

    vul4c_runtime_dir=os.path.join(root_dir,"vul4c_runtime")
    if not os.path.exists(vul4c_runtime_dir):
        os.mkdir(vul4c_runtime_dir)
    
    result_dir=tool.lower()+"_"+cveid.lower()+"_"+stamp
    result_dir=os.path.join(result_root_dir,result_dir)
    os.mkdir(result_dir)

    init_logger(result_dir)

    if not os.path.exists(cve_dir):
        logger.info("cve config dir dont exist, please check your software and cveid")
        sys.exit(1)

    cve_runtime_dir=os.path.join(os.path.join(os.path.join(vul4c_runtime_dir,tool),software),cveid)
    if os.path.exists(cve_runtime_dir):
        shutil.rmtree(cve_runtime_dir)
    os.makedirs(cve_runtime_dir,exist_ok=True)

    test_script_dir=os.path.join(os.path.join(root_dir,"test"),software)
    instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
    copy_command=f"cp -r {cve_dir}/* {cve_runtime_dir}"
    execute_command(copy_command)
    if os.path.isdir(test_script_dir):
        copy_command=f"cp -r {test_script_dir}/* {cve_runtime_dir}"
        execute_command(copy_command)
    if os.path.isdir(instrument_dir):
        copy_command=f"cp -r {instrument_dir}/* {cve_runtime_dir}"
        execute_command(copy_command)

    container_dir=f"/{cveid}"
    d=parse_config(cve_runtime_dir, container_dir)

    if tool in ["VulnFix"]:
        container_name="vul4c_"+tool.lower()+"_"+cveid.lower()+"_"+stamp
        tool_docker=VulnFix(cve_runtime_dir, container_dir, container_dir ,container_name)
        container_id=tool_docker.container.id
        # cp_to_container(container_id,cve_runtime_dir,f"/{cveid}")
        tool_docker.config()
        tool_docker.repair()
        tool_docker.save_result()
        cp_from_container(container_id,"/vul4c_result",result_dir)

    elif tool in ["ExtractFix","Senx","FootPatch"]:
        repair_name="vul4c_"+tool.lower()+"_"+"repair"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp

        if tool=="ExtractFix":
            repair_container=ExtractFix(cve_runtime_dir, container_dir, container_dir, repair_name)
        elif tool=="Senx":
            repair_container=Senx(cve_runtime_dir, container_dir, container_dir, repair_name)
        elif tool=="FootPatch":
            repair_container=FootPatch(cve_runtime_dir, container_dir, container_dir, repair_name)
        repair_container_id=repair_container.container.id

        # temp_dir="/tmp/"+repair_name        
        # cp_to_container(repair_container_id,cve_runtime_dir,f"/{cveid}")
        repair_container.run()
        
        cp_from_container(repair_container_id,"/vul4c_result",result_dir)
        # cp_from_container(repair_container_id,f"/{cveid}",temp_dir)

        validate_container = Validate(cve_runtime_dir, container_dir, container_dir, validate_name)
        validate_id=validate_container.container.id
        # cp_to_container(validate_id,temp_dir,f"/{cveid}")
        # shutil.rmtree(temp_dir)
        validate_container.run()

        validate_result_dir=os.path.join(result_dir,"temp")
        cp_from_container(validate_id,"/vul4c_result",validate_result_dir)

        execute_command(f"mv {validate_result_dir}/* {result_dir}/vul4c_result")
        shutil.rmtree(validate_result_dir)

    elif tool in ["VRepair", "VulRepair"]:
        inference_name="vul4c_"+tool.lower()+"_"+"inference"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp
        # temp_dir="/tmp/"+inference_name
        for file in os.listdir(cve_runtime_dir):
            if "NEW" in file:
                new_file_name=file
            elif "OLD" in file:
                old_file_name=file

        for temp in new_file_name.split('_'):
            if "CWE" in temp:
                cwe_id=temp
        
        if tool == "VRepair":
            inference_container=VRepair(cve_runtime_dir, container_dir, container_dir, inference_name, new_file_name, old_file_name, cwe_id, cveid)
        else:
            inference_container=VulRepair(cve_runtime_dir, container_dir, container_dir, inference_name, new_file_name, old_file_name, cwe_id, cveid)
        
        inference_id=inference_container.container.id
        # cp_to_container(inference_id,cve_runtime_dir,f"/{cveid}")
        inference_container.run()
        # cp_from_container(inference_id,f"/{cveid}",temp_dir)

        validate_container = Validate(cve_runtime_dir, container_dir, container_dir, validate_name)
        validate_id=validate_container.container.id
        # cp_to_container(validate_id,temp_dir,f"/{cveid}")
        # shutil.rmtree(temp_dir)

        validate_container.run()
        cp_from_container(validate_id,"/vul4c_result",result_dir)
    
    elif tool in ["Saver","IntPTI"]:
        container_name="vul4c_"+tool.lower()+"_"+cveid.lower()+"_"+stamp
        if tool=="Saver":
            tool_docker=Saver(cve_runtime_dir, container_dir, container_dir, container_name)
        if tool=="IntPTI":
            tool_docker=IntPTI(cve_runtime_dir, container_dir, container_dir, container_name)
        container_id=tool_docker.container.id
        # temp_dir="/tmp/"+container_name
        # cp_to_container(container_id,cve_runtime_dir,f"/{cveid}")
        tool_docker.repair()
        tool_docker.save_result()

        cp_from_container(container_id,"/vul4c_result",result_dir)

    else:
        logger.error("tool dont exist, please check your tool name")
    
    test_file=os.path.join(cve_runtime_dir,"test.sh")
    if (not d==None) and os.path.exists(test_file):
        test_name="vul4c_"+tool.lower()+"_"+"test"+"_"+cveid.lower()+"_"+stamp
        test_container = Test(cve_runtime_dir, container_dir, container_dir, test_name, d)
        test_id=test_container.container.id
        test_container.run()
if __name__ == "__main__":
    main()