import logging
import os
import shutil


logger = logging.getLogger('vul4c')

def init_logger(dir):
    logger.setLevel(logging.DEBUG)

    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.INFO)

    info_file_handler = logging.FileHandler(os.path.join(dir,"vul4c_info_log"))
    info_file_handler.setLevel(logging.INFO)

    debug_file_handler = logging.FileHandler(os.path.join(dir,"vul4c_debug_log"))
    debug_file_handler.setLevel(logging.DEBUG)

    info_formatter = logging.Formatter("%(asctime)s %(message)s","%Y-%m-%d %H:%M:%S")
    debug_formatter = logging.Formatter("%(asctime)s [%(levelname)s] [%(funcName)s] %(message)s","%Y-%m-%d %H:%M:%S")

    console_handler.setFormatter(info_formatter)
    info_file_handler.setFormatter(info_formatter)
    debug_file_handler.setFormatter(debug_formatter)
    
    logger.addHandler(console_handler)
    logger.addHandler(info_file_handler)
    logger.addHandler(debug_file_handler)
