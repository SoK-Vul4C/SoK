from tool.container import DockerContainer
from logger import logger
import os
import sys

class VulnFix(DockerContainer):

    repair_dir=""
    config_file=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            repair_dir,
            container_name,
            repository_name="vul4c/vulnfix",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.repair_dir=repair_dir


    def config(self):
        core_config_command="bash -c \"echo core | tee /proc/sys/kernel/core_pattern && echo performance | tee cpu*/cpufreq/scaling_governor && echo 0 | tee /proc/sys/kernel/randomize_va_space\""
        self.exec_command(core_config_command,workdir="/sys/devices/system/cpu")

        config_file=os.path.join(self.repair_dir,"config")
        setup_file=os.path.join(self.repair_dir,"setup.sh")
        
        exploit_name=self.find_file(self.repair_dir,1,"exploit*")[0]
        exploit_file=os.path.join(self.repair_dir, exploit_name)
        
        config_exist=self.file_exists(config_file)
        setup_exist=self.file_exists(setup_file)
        exploit_exist=self.file_exists(exploit_file)

        if not (config_exist and setup_exist and exploit_exist ):
            logger.error("VulnFix lacks the necessary configuration files")
            raise RuntimeError(f"VulnFix lacks the necessary configuration files")
        
        alternate_path_command=f"bash -c \"sed -i 's#<path>#{self.repair_dir}#g' {config_file}\""
        self.exec_command(alternate_path_command)
        logger.info(f"alternate the path in config with command {alternate_path_command}")
        
        setup_command=f"bash -c \"bash {setup_file}\""
        exit_code,output=self.exec_command(setup_command,workdir=self.repair_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was built through setup with an error code {exit_code}")
            raise RuntimeError(f"An error occurred when the project was built through setup with an error code {exit_code}")
        
        self.config_file=config_file


    def repair(self):
        
        vulnfix_command =f"bash -c \"stty cols 100 && stty rows 100 && python3.8 /home/yuntong/vulnfix/src/main.py --budget 60 {self.config_file}\""
        exit_code,output=self.exec_command(vulnfix_command,workdir=self.repair_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        runtime_dir=os.path.join(self.repair_dir,"runtime")
        runtime_exists=self.dir_exists(runtime_dir)
        if not runtime_exists:
            logger.info("vulnfix failed to generate a patch due to an unexpected abort")
        
        result_file=os.path.join(runtime_dir,"vulnfix.result")
        result_exists=self.file_exists(result_file)
        if not result_exists:
            logger.info("vulnfix failed to generate result due to an unexpected abort")
        
        debug_log_file=os.path.join(runtime_dir,"vulnfix.log.debug")
        info_log_file=os.path.join(runtime_dir,"vulnfix.log.info")

        debug_log_exists=self.file_exists(debug_log_file)
        info_log_exists=self.file_exists(info_log_file)

        if result_exists:
            self.cp_file(result_file,self.result_dir)
        if debug_log_exists:
            self.cp_file(debug_log_file,self.result_dir)
        if info_log_exists:
            self.cp_file(info_log_file,self.result_dir)
    
                