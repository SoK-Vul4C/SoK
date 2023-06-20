from tool.container import DockerContainer
from logger import logger
import os
import sys

class VulRepair(DockerContainer):

    work_dir=""
    result_dir="/vul4c_result"
    cve_id=""

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            cve_id,
            repository_name="vul4c/vulrepair",
            tag_name="1.0",  
            gpu=True
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir
        self.cve_id=cve_id

    def inference(self):
        inference_command=f"bash -c \"python3 vulrepair_inference.py --tokenizer_name=Salesforce/codet5-base --model_name_or_path=Salesforce/codet5-base --do_test --encoder_block_size 512 --decoder_block_size 256 --num_beams=50 --eval_batch_size 1 --inference_file {self.cve_id}.csv\""
        exit_code,output=self.exec_command(inference_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while inference with VulRepair")
            raise RuntimeError(f"An error occurred while inference with VulRepair")
        else:
            logger.info("inference ended successfully")
    
                