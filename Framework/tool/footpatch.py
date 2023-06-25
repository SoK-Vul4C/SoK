from tool.container import DockerContainer
from logger import logger
import os
import sys

class FootPatch(DockerContainer):

    work_dir=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            repository_name="vul4c/footpatch",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir


    def repair(self):

        repair_file=os.path.join(self.work_dir,"repair.sh")
        repair_exist=self.file_exists(repair_file)

        if not repair_exist:
            logger.error("FootPatch lacks the necessary configuration files")
            raise RuntimeError(f"FootPatch lacks the necessary configuration files")
        
        footpatch_command=f"bash -c \"bash {repair_file}"

        exit_code,output=self.exec_command(footpatch_command, workdir=self.work_dir,user='vagrant')


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        infer_dir=os.path.join(self.work_dir,'source/infer-out')
        exit_code,outputs=self.find_file(infer_dir,2,"patches")
        output= outputs.split('\n')

        for dir in output:
            if dir != "":
                self.cp_file(os.path.join(dir,"*"),self.result_dir)

        
    
                