from tool.container import DockerContainer
from logger import logger
import os
import sys

class VulMaster(DockerContainer):

    def __init__(
            self,
            localhost_dir, 
            container_dir,
            work_dir,
            container_name,
            new_file_name,
            old_file_name,
            cwe_id,
            cve_id,
            repository_name="vul4c/vulmaster",
            tag_name="1.0",  
            gpu=True
    ):
        super().__init__(repository_name, tag_name, container_name, localhost_dir, container_dir, gpu)
        self.work_dir=work_dir
        self.new_file_name=new_file_name
        self.old_file_name=old_file_name
        self.cwe_id=cwe_id
        self.cve_id=cve_id
        self.result_dir="/vul4c_result"
    

    def preprocess(self):
        preprocess_command=f"bash -c \"source /root/env_VRepair.sh && python3 preprocess.py  {self.old_file_name} {self.new_file_name} {self.cwe_id} {self.cve_id}\""
        exit_code,output=self.exec_command(preprocess_command,workdir=self.work_dir)
        if exit_code != 0:
            logger.error(f"An error occurred when the project was preprocessed VRepair")
            raise RuntimeError(f"An error occurred when the project was preprocessed VRepair")

        preprocess_vulmaster_command = f"bash -c \"source /root/env_VulMaster.sh && cp /{self.cve_id}/{self.cve_id}.csv data_construction/deduplicated_data/test.csv && cd data_construction && python3 prepare_vulmasters_data.py\""
        exit_code,output=self.exec_command(preprocess_vulmaster_command,workdir="/home/vulmaster")

        
    def inference(self):
        inference_command=f"bash -c \"source /root/env_VulMaster.sh && SLURM_NTASKS=1 python test_model.py \
        --eval_data data_construction/vulmaster_data/test.json \
        --write_results \
        --model_path checkpoint_final/final_model/checkpoint/best_dev \
        --per_gpu_eval_batch_size 1 \
        --n_context 10 \
	    --beam_size 50 \
        --text_maxlength 512 \
        --answer_maxlength 512 \
        --add_loss binary \
        --cat_emb \
        --name final_model \
        --checkpoint_dir checkpoint_final \
        --generated_output_path /{self.work_dir}/generated_results\""
        exit_code,output=self.exec_command(inference_command,workdir="/home/vulmaster")
        
        adapt_command = f"bash -c \"source /root/env_VulMaster.sh && python3 deal.py\""
        exit_code,output=self.exec_command(adapt_command,workdir=self.work_dir)

        if exit_code != 0:
            logger.error(f"An error occurred while inference with VulMaster")
            raise RuntimeError(f"An error occurred while inference with VulMaster")
        else:
            logger.info("inference ended successfully")
    
    def restore(self):
        restore_command =f"bash -c \"source /root/env_VRepair.sh && python3 restore.py\""
        exit_code,output=self.exec_command(restore_command,workdir=self.work_dir)
        
        if exit_code != 0:
            logger.error(f"An error occurred while resotre with VRepair")
            raise RuntimeError(f"An error occurred while restore with VRepair")
        else:
            logger.info("restore ended successfully")
    
    def run(self):
        self.preprocess()
        self.inference()
        self.restore()
                