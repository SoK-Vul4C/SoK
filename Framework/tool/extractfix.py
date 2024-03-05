from tool.container import DockerContainer
from logger import logger
import os
import sys

class ExtractFix(DockerContainer):

    repair_dir=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            repair_dir,
            container_name,
            repository_name="vul4c/extractfix",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.repair_dir=repair_dir


    def config(self):
        setup_file=os.path.join(self.repair_dir,"setup.sh")
        
        exploit_name=self.find_file(self.repair_dir,1,"test_case*")[0]
        exploit_file=os.path.join(self.repair_dir, exploit_name)
        
        setup_exist=self.file_exists(setup_file)
        exploit_exist=self.file_exists(exploit_file)

        if not (setup_exist and exploit_exist ):
            logger.error("ExtractFix lacks the necessary configuration files")
            raise RuntimeError(f"ExtractFix lacks the necessary configuration files")
        
        setup_command=f"bash -c \"bash {setup_file}\""
        exit_code,output=self.exec_command(setup_command,workdir=self.repair_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was built through setup with an error code {exit_code}")
            raise RuntimeError(f"An error occurred when the project was built through setup with an error code {exit_code}")
        
        project_config_file=os.path.join(self.repair_dir,"*.sh")
        project_driver_file=os.path.join(self.repair_dir,"*driver")
        self.add_permissions(project_config_file)
        self.add_permissions(project_driver_file)


    def repair(self):
        
        extractfix_command =f"bash -c \"source /ExtractFix/setup.sh && bash run 2>&1 |tee extractfix.log\""
        exit_code,output=self.exec_command(extractfix_command,workdir=self.repair_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        i=0
        while True:
           result_file=os.path.join(self.repair_dir,"result"+str(i))
           if self.dir_exists(result_file):
                self.cp_file(result_file,self.result_dir)
                i=i+1
           else:
               break
    
    def config_validate(self):
        config_command=f"bash -c \"python3 config_validate.py\""
        exit_code,output=self.exec_command(config_command,workdir=self.repair_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when preparing the validate patched file")
            raise RuntimeError(f"An error occurred when preparing the validate patched file")

    def run(self):
        self.config()
        self.repair()
        self.save_result()
        self.config_validate()