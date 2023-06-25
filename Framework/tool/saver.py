from tool.container import DockerContainer
from logger import logger
import os
import sys

class Saver(DockerContainer):

    work_dir=""
    result_dir="/vul4c_result"

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            repository_name="vul4c/saver",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir


    def repair(self):

        static_analysis_file=os.path.join(self.work_dir,"repair.sh")
        static_analysis_exist=self.file_exists(static_analysis_file)

        if not static_analysis_exist:
            logger.error("Saver lacks the necessary configuration files")
            raise RuntimeError(f"Saver lacks the necessary configuration files")
        
        saver_work_dir=os.path.join(self.work_dir,"source")

        num=1    
        while True:
            report_file=os.path.join(self.work_dir,f"report{num}.json")
            flag=self.file_exists(report_file)
            if not flag:
                break
            self.cp_file(report_file,saver_work_dir)
            num=num+1

        saver_command=f"bash -c \"bash {static_analysis_file} && cd {saver_work_dir}"
        for i in range(1,num):
            saver_command+=f" && timeout -k 5m 1h infer saver --error-report report{i}.json 2>&1 |tee patch{i}.log"
        saver_command+="\""
        exit_code,output=self.exec_command(saver_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")


    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        saver_work_dir=os.path.join(self.work_dir,"source")
        num=1    
        while True:
            patch_log_file=os.path.join(saver_work_dir,f"patch{num}.log")
            flag=self.file_exists(patch_log_file)
            if not flag:
                break
            self.cp_file(patch_log_file,self.result_dir)
            num=num+1

        
    
                