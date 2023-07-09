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

    root_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    result_root_dir=os.path.join(root_dir,"vul4c-result")
    if not os.path.exists(result_root_dir):
        os.mkdir(result_root_dir)

    result_dir=tool.lower()+"_"+cveid.lower()+"_"+str(int(time.time()))
    result_dir=os.path.join(result_root_dir,result_dir)
    os.mkdir(result_dir)
    init_logger(result_dir)

    if tool in ["VulnFix"]:
        container_name="vul4c_"+tool.lower()+"_"+cveid.lower()+"_"+str(int(time.time()))
        tool_docker=VulnFix("/tmp","/tmp",os.path.join("/",cveid),container_name)
        container_id=tool_docker.container.id

        temp_dir="/tmp/"+container_name
        os.mkdir(temp_dir)
        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)

        instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
        if os.path.isdir(instrument_dir):
            copy_command=f"cp -r {instrument_dir}/* {cve_dir}/* {temp_dir}"
        else:
            copy_command=f"cp -r {cve_dir}/* {temp_dir}"
        
        execute_command(copy_command)
        cp_to_container(container_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        tool_docker.config()
        tool_docker.repair()
        tool_docker.save_result()

        cp_from_container(container_id,"/vul4c_result",result_dir)

    elif tool in ["ExtractFix","Senx"]:

        stamp=str(int(time.time()))
        repair_name="vul4c_"+tool.lower()+"_"+"repair"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp

        if tool=="ExtractFix":
            repair_container=ExtractFix("/tmp","/tmp",os.path.join("/",cveid),repair_name)
        elif tool=="Senx":
            repair_container=Senx("/tmp","/tmp",os.path.join("/",cveid),repair_name)
        repair_container_id=repair_container.container.id

        temp_dir="/tmp/"+repair_name
        os.mkdir(temp_dir)
        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)

        instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
        if os.path.isdir(instrument_dir):
            copy_command=f"cp -r {instrument_dir}/* {cve_dir}/* {temp_dir}"
        else:
            copy_command=f"cp -r {cve_dir}/* {temp_dir}"
        
        execute_command(copy_command)
        cp_to_container(repair_container_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        repair_container.config()
        repair_container.repair()
        repair_container.save_result()
        repair_container.config_validate()
        
        cp_from_container(repair_container_id,"/vul4c_result",result_dir)
        cp_from_container(repair_container_id,f"/{cveid}",temp_dir)

        validate_container = Validate("/tmp","/tmp",f"/{cveid}",validate_name)
        validate_id=validate_container.container.id
        cp_to_container(validate_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)
        validate_container.config()
        validate_container.validate()
        validate_container.save_result()

        validate_result_dir=os.path.join(result_dir,"temp")
        cp_from_container(validate_id,"/vul4c_result",validate_result_dir)

        execute_command(f"mv {validate_result_dir}/* {result_dir}/vul4c_result")
        shutil.rmtree(validate_result_dir)

    elif tool=="VRepair":
        stamp=str(int(time.time()))
        inference_name="vul4c_"+tool.lower()+"_"+"inference"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp

        temp_dir="/tmp/"+inference_name
        os.mkdir(temp_dir)

        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)
        
        copy_command=f"cp -r {instrument_dir}/* {cve_dir}/* {temp_dir}"
        execute_command(copy_command)

        for file in os.listdir(temp_dir):
            if "NEW" in file:
                new_file_name=file
            elif "OLD" in file:
                old_file_name=file

        for temp in new_file_name.split('_'):
            if "CWE" in temp:
                cwe_id=temp

        inference_container=VRepair("/tmp","/tmp",f"/{cveid}",inference_name,new_file_name,old_file_name,cwe_id,cveid)
        inference_id=inference_container.container.id
        cp_to_container(inference_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        inference_container.inference()
        inference_container.restore()
        cp_from_container(inference_id,f"/{cveid}",temp_dir)

        validate_container = Validate("/tmp","/tmp",f"/{cveid}",validate_name)
        validate_id=validate_container.container.id
        cp_to_container(validate_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)
        validate_container.config()
        validate_container.validate()
        validate_container.save_result()

        cp_from_container(validate_id,"/vul4c_result",result_dir)
    
    elif tool=="VulRepair":
        stamp=str(int(time.time()))
        preprocess_name="vul4c_"+tool.lower()+"_"+"preprocess"+"_"+cveid.lower()+"_"+stamp
        inference_name="vul4c_"+tool.lower()+"_"+"inference"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp

        temp_dir="/tmp/"+inference_name
        os.mkdir(temp_dir)

        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)
        
        copy_command=f"cp -r {instrument_dir}/* {cve_dir}/* {temp_dir}"
        execute_command(copy_command)

        for file in os.listdir(temp_dir):
            if "NEW" in file:
                new_file_name=file
            elif "OLD" in file:
                old_file_name=file

        for temp in new_file_name.split('_'):
            if "CWE" in temp:
                cwe_id=temp

        preprocess_container=VRepair("/tmp","/tmp",f"/{cveid}",preprocess_name,new_file_name,old_file_name,cwe_id,cveid)
        preprocess_id=preprocess_container.container.id
        cp_to_container(preprocess_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        preprocess_container.preprocess()
        cp_from_container(preprocess_id,f"/{cveid}",temp_dir)

        inference_container=VulRepair("/tmp","/tmp",f"/{cveid}",inference_name,cveid)
        inference_id=inference_container.container.id
        cp_to_container(inference_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        inference_container.inference()
        cp_from_container(inference_id,f"/{cveid}",temp_dir)

        cp_to_container(preprocess_id,os.path.join(temp_dir,"src.txt.predictions"),f"/{cveid}")
        shutil.rmtree(temp_dir)
        preprocess_container.restore()
        cp_from_container(preprocess_id,f"/{cveid}",temp_dir)

        validate_container = Validate("/tmp","/tmp",f"/{cveid}",validate_name)
        validate_id=validate_container.container.id
        cp_to_container(validate_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)
        validate_container.config()
        validate_container.validate()
        validate_container.save_result()

        cp_from_container(validate_id,"/vul4c_result",result_dir)

    elif tool in ["Saver","IntPTI"]:
        container_name="vul4c_"+tool.lower()+"_"+cveid.lower()+"_"+str(int(time.time()))
        if tool=="Saver":
            tool_docker=Saver("/tmp","/tmp",os.path.join("/",cveid),container_name)
        if tool=="IntPTI":
            tool_docker=IntPTI("/tmp","/tmp",os.path.join("/",cveid),container_name)
        container_id=tool_docker.container.id

        temp_dir="/tmp/"+container_name
        os.mkdir(temp_dir)
        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)
        
        copy_command=f"cp -r {cve_dir}/* {temp_dir}"
        execute_command(copy_command)
        cp_to_container(container_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        tool_docker.repair()
        tool_docker.save_result()

        cp_from_container(container_id,"/vul4c_result",result_dir)

    elif tool== "FootPatch":

        stamp=str(int(time.time()))
        repair_name="vul4c_"+tool.lower()+"_"+"repair"+cveid.lower()+"_"+stamp
        validate_name="vul4c_"+tool.lower()+"_"+"validate"+"_"+cveid.lower()+"_"+stamp

        repair_container=FootPatch("/tmp","/tmp",os.path.join("/",cveid),repair_name)
        repair_container_id=repair_container.container.id

        temp_dir="/tmp/"+repair_name
        os.mkdir(temp_dir)
        tool_config_dir=os.path.join(os.path.dirname(os.path.abspath(__file__)),tool)
        software_dir=os.path.join(tool_config_dir,software)
        cve_dir=os.path.join(software_dir,cveid)
        if not os.path.exists(cve_dir):
            logger.info("cve config dir dont exist, please check your software and cveid")
            sys.exit(1)

        instrument_dir=os.path.join(tool_config_dir,"INSTRUMENT")
        if os.path.isdir(instrument_dir):
            copy_command=f"cp -r {instrument_dir}/* {cve_dir}/* {temp_dir}"
        else:
            copy_command=f"cp -r {cve_dir}/* {temp_dir}"
        
        execute_command(copy_command)
        cp_to_container(repair_container_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)

        repair_container.repair()
        repair_container.save_result()
        repair_container.config_validate()

        cp_from_container(repair_container_id,"/vul4c_result",result_dir)
        cp_from_container(repair_container_id,f"/{cveid}",temp_dir)

        validate_container = Validate("/tmp","/tmp",f"/{cveid}",validate_name)
        validate_id=validate_container.container.id
        cp_to_container(validate_id,temp_dir,f"/{cveid}")
        shutil.rmtree(temp_dir)
        validate_container.config()
        validate_container.validate()
        validate_container.save_result()

        validate_result_dir=os.path.join(result_dir,"temp")
        cp_from_container(validate_id,"/vul4c_result",validate_result_dir)

        execute_command(f"mv {validate_result_dir}/* {result_dir}/vul4c_result")
        shutil.rmtree(validate_result_dir)

    else:
        logger.error("tool dont exist, please check your tool name")


if __name__ == "__main__":
    main()