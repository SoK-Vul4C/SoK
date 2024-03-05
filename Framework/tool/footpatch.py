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
        
        footpatch_command=f"bash -c \"bash {repair_file}\""
        chown_permission=f"bash -c \"chown -R vagrant:vagrant {self.work_dir}\""
        
        self.exec_command(chown_permission,workdir=self.work_dir)
        exit_code,output=self.exec_command(footpatch_command, workdir=self.work_dir,user='vagrant')


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        infer_dir=os.path.join(self.work_dir,'source/infer-out')
        outputs=self.find_file(infer_dir,3,"patches")

        for dir in outputs:
            dir=dir.strip()
            if dir != "":
                self.cp_file(os.path.join(dir,"*"),self.result_dir)

    
    def config_validate(self):
        config_command=f"bash -c \"python3 config_validate.py\""
        exit_code,output=self.exec_command(config_command,workdir=self.work_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when preparing the validate patched file")
            raise RuntimeError(f"An error occurred when preparing the validate patched file")
    
    
    def run(self):
        self.repair()
        self.save_result()
        self.config_validate()