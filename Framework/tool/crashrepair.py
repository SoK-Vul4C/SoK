from tool.container import DockerContainer
from logger import logger
import os
import sys

class CrashRepair(DockerContainer):
    
    def __init__(
            self,
            localhost_dir, 
            container_dir,
            software,
            cve_id,
            container_name,
            repository_name="vul4c/vulnfix",
            tag_name="1.0",  
            gpu=False
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.software = software
        self.cve_id = cve_id
        self.repair_dir = f"/data/vulnloc/{software}/{cve_id}"
        self.result_dir = f"/vul4c_result"
        self.log_dir = f"/logs/{software}/{cve_id}/1"
        self.log_file = os.path.join(self.log_dir, "orchestrator.log")
    
    def repair(self):
        # Default values for time limits and patch limit
        REPAIR_TIME_LIMIT = os.getenv("REPAIR_TIME_LIMIT", "60")
        TEST_TIME_LIMIT = os.getenv("TEST_TIME_LIMIT", "40")
        PATCH_LIMIT = os.getenv("PATCH_LIMIT", "50")

        # Change to WORKDIR (equivalent to `pushd` in bash)
        # os.chdir(WORKDIR)

        # Create necessary directories
        # os.makedirs(LOG_DIR, exist_ok=True)
        # os.makedirs(RESULTS_DIR, exist_ok=True)

        # Run crashrepair tool
        crashrepair_command = f"bash -c \"cd {self.repair_dir} && stty cols 100 && stty rows 100 && crashrepair repair --time-limit-minutes-validation {REPAIR_TIME_LIMIT} --time-limit-seconds-test {TEST_TIME_LIMIT} --patch-limit {PATCH_LIMIT} bug.json\""
        exit_code,output=self.exec_command(crashrepair_command,workdir=self.repair_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while repairing the project with an error code {exit_code}")
            raise RuntimeError(f"An error occurred while repairing the project with an error code {exit_code}")
        else:
            logger.info("repair ended successfully")
        
        mkdir_command=f"bash -c \"mkdir {self.log_dir}\""
        exit_code,output=self.exec_command(mkdir_command)
        
        with open(self.log_file, 'w') as log_file:
            log_file.write(output)
            
        
    def save_result(self):
        mkdir_command=f"bash -c \"mkdir {self.result_dir}\""
        exit_code,output=self.exec_command(mkdir_command)

        patch_dir=os.path.join(self.repair_dir,"patches")
        patch_exists=self.dir_exists(patch_dir)
        analysis_dir=os.path.join(self.repair_dir,"analysis")
        analysis_exists=self.dir_exists(analysis_dir)
        report_json=os.path.join(self.repair_dir,"report.json")
        report_exists=self.file_exists(report_json)
        if patch_exists:
            cp_command=f"bash -c \"cp -r {patch_dir} {self.result_dir}\""
            exit_code,output=self.exec_command(cp_command)
        else:
            logger.info("crashrepair failed to generate patch due to an unexpected abort")
            
        if analysis_exists:
            cp_command=f"bash -c \"cp -r {analysis_dir} {self.result_dir}\""
            exit_code,output=self.exec_command(cp_command)
        else:
            logger.info("crashrepair failed to generate analysis due to an unexpected abort")
            
        if report_exists:
            self.cp_file(report_json, self.result_dir)
        else:
            logger.info("crashrepair failed to generate report due to an unexpected abort")
        
        log_exists=self.dir_exists(self.log_dir)
        if log_exists:
            cp_command=f"bash -c \"cp -r {self.log_dir} {self.result_dir}\""
            exit_code,output=self.exec_command(cp_command)
        else:
            logger.info("crashrepair failed to generate log due to an unexpected abort")