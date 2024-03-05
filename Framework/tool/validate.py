from tool.container import DockerContainer
from logger import logger
import os
import sys

class Validate(DockerContainer):

    work_dir=""
    config_file=""
    validate_py=""
    result_dir="/vul4c_result"
    candidate_exist=None

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            repository_name="vul4c/vulnfix",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir


    def config(self):
        core_config_command="bash -c \"echo core | tee /proc/sys/kernel/core_pattern && echo performance | tee cpu*/cpufreq/scaling_governor && echo 0 | tee /proc/sys/kernel/randomize_va_space\""
        self.exec_command(core_config_command,workdir="/sys/devices/system/cpu")

        config_file=os.path.join(self.work_dir,"config")
        setup_file=os.path.join(self.work_dir,"setup.sh")
        validate_file=os.path.join(self.work_dir,"validate.sh")

        setup_exist=self.file_exists(setup_file)
        validate_exist=self.file_exists(validate_file)

        if validate_exist:
            setup_file=validate_file
        
        alternate_path_command=f"bash -c \"sed -i 's#<path>#{self.work_dir}#g' {config_file}\""
        self.exec_command(alternate_path_command)
        logger.info(f"alternate the path in config with command {alternate_path_command}")
        
        setup_command=f"bash -c \"rm -rf source && bash {setup_file}\""
        exit_code,output=self.exec_command(setup_command,workdir=self.work_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was built through setup with an error code {exit_code}")
            raise RuntimeError(f"An error occurred when the project was built through setup with an error code {exit_code}")
        
        self.config_file=config_file
        self.validate_py=os.path.join(self.work_dir, "validate.py")


    def validate(self):
        
        validate_command =f"bash -c \"python3 {self.validate_py} {self.config_file}\""
        exit_code,output=self.exec_command(validate_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        result_file=os.path.join(self.work_dir,"runtime/result")
        self.cp_file(result_file,self.result_dir)
        
        candidate_dir=os.path.join(self.work_dir,"candidate_result")
        if self.dir_exists(candidate_dir):
            self.cp_file(candidate_dir,self.result_dir)
    
    def run(self):
        self.config()
        self.validate()
        self.save_result()
                