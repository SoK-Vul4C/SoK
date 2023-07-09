from tool.container import DockerContainer
from logger import logger
import os
import sys

class IntPTI(DockerContainer):

    work_dir=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            repository_name="vul4c/intpti",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir


    def repair(self):

        static_analysis_file=os.path.join(self.work_dir,"repair.sh")
        static_analysis_exist=self.file_exists(static_analysis_file)

        if not static_analysis_exist:
            logger.error("IntPTI lacks the necessary configuration files")
            raise RuntimeError(f"IntPTI lacks the necessary configuration files")
        
        intpti_command=f"bash -c \"source /etc/environment && bash {static_analysis_file}\""
        exit_code,output=self.exec_command(intpti_command,workdir=self.work_dir)
        
        logger.info("repair ended")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        intpti_work_dir=os.path.join(self.work_dir,"source")
        patch_log_dir=os.path.join(intpti_work_dir,"output")
        self.cp_file(f"{patch_log_dir}/*",self.result_dir)

        
    
                