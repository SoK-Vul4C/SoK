from tool.container import DockerContainer
from logger import logger
import os
import sys

class VRepair(DockerContainer):

    work_dir=""
    result_dir="/vul4c_result"
    new_file_name=""
    old_file_name=""
    cwe_id=""
    cve_id=""

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            new_file_name,
            old_file_name,
            cwe_id,
            cve_id,
            repository_name="vul4c/vrepair",
            tag_name="1.0",  
            gpu=True
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir
        self.new_file_name=new_file_name
        self.old_file_name=old_file_name
        self.cwe_id=cwe_id
        self.cve_id=cve_id


    def preprocess(self):
        preprocess_command=f"bash -c \"source /root/env.sh && python3 preprocess.py  {self.old_file_name} {self.new_file_name} {self.cwe_id} {self.cve_id}\""
        exit_code,output=self.exec_command(preprocess_command,workdir=self.work_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was preprocessed VRepair")
            raise RuntimeError(f"An error occurred when the project was preprocessed VRepair")
        

    def inference(self):
        
        inference_command =f"bash -c \"source /root/env.sh && python3 vrepair_inference.py  {self.old_file_name} {self.new_file_name} {self.cwe_id}\""
        exit_code,output=self.exec_command(inference_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while inference with VRepair")
            raise RuntimeError(f"An error occurred while inference with VRepair")
        else:
            logger.info("inference ended successfully")


    def restore(self):
        restore_command =f"bash -c \"source /root/env.sh && python3 restore.py\""
        exit_code,output=self.exec_command(restore_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while resotre with VRepair")
            raise RuntimeError(f"An error occurred while restore with VRepair")
        else:
            logger.info("restore ended successfully")
    
    def run(self):
        self.inference()
        self.restore()