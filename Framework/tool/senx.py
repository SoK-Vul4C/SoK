from tool.container import DockerContainer
from logger import logger
import os
import sys

class Senx(DockerContainer):

    repair_dir=""
    config_file=""
    insrument_file=""
    config_parse_file=""
    bin_name=""
    bin_path=""
    cmd=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            repair_dir,
            container_name,
            repository_name="vul4c/senx",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.repair_dir=repair_dir


    def config(self):
        setup_file=os.path.join(self.repair_dir,"setup.sh")
        config_file=os.path.join(self.repair_dir,"config")
        instrument_file=os.path.join(self.repair_dir,"instrument.sh")
        config_parse_file=os.path.join(self.repair_dir,"config_parse.py")

        exploit_name=self.find_file(self.repair_dir,1,"exploit*")[0]
        exploit_file=os.path.join(self.repair_dir, exploit_name)
        
        setup_exist=self.file_exists(setup_file)
        exploit_exist=self.file_exists(exploit_file)
        config_exist=self.file_exists(config_file)

        if not (setup_exist and exploit_exist ):
            logger.error("Senx lacks the necessary configuration files")
            raise RuntimeError(f"Senx lacks the necessary configuration files")
        
        alternate_path_command=f"bash -c \"sed -i 's#<path>#{self.repair_dir}#g' {config_file}\""
        self.exec_command(alternate_path_command)
        logger.info(f"alternate the path in config with command {alternate_path_command}")

        setup_command=f"bash -c \"source /home/user/env.sh && bash {setup_file}\""
        exit_code,output=self.exec_command(setup_command,workdir=self.repair_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was built through setup with an error code {exit_code}")
            raise RuntimeError(f"An error occurred when the project was built through setup with an error code {exit_code}")
        
        self.config_file=config_file
        self.insrument_file=instrument_file
        self.config_parse_file=config_parse_file

    def repair(self):
        get_binary_command=f"bash -c \"python3 {self.config_parse_file}\""
        exit_code,output=self.exec_command(get_binary_command,workdir=self.repair_dir)
        info=output.split("\n")
        self.bin_path=info[0].strip()
        self.bin_name=info[1].strip()
        self.cmd=info[2].strip()
        self.cp_file(self.bin_path,self.repair_dir)

        extract_command=f"bash -c \"source /home/user/env.sh && bash {self.insrument_file} {self.bin_name}\""
        exit_code,output=self.exec_command(extract_command,workdir=self.repair_dir)

        senx_command =f"bash -c \"source /home/user/env.sh && senx -struct-def=def_file {self.bin_name}.bc {self.cmd} 2>err\""
        exit_code,output=self.exec_command(senx_command,workdir=self.repair_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        result_file=os.path.join(self.repair_dir,f"{self.bin_name}.bc.patch")
        if self.file_exists(result_file):
            self.cp_file(result_file,self.result_dir)

        
    def config_validate(self):
        config_command=f"bash -c \"python3 config_validate.py\""
        exit_code,output=self.exec_command(config_command,workdir=self.repair_dir)
        if exit_code != 0:
            logger.info(f"senx failed to produce a patch")
    
    def run(self):
        self.config()
        self.repair()
        self.save_result()
        self.config_validate()
                