import os
import docker
import random

from logger import logger

class Data(object):

    def __init__(self, abs_path, binary, cmd, exploit, build_cmd, fix_file=None, fix_loc=None, crash_file=None, crash_loc=None):
        self.abs_path=abs_path
        self.binary = binary
        self.poc_cmd = cmd
        self.exploit = exploit
        self.test = os.path.join(abs_path, "test.sh")
        self.runtime = os.path.join(abs_path, "runtime")
        self.source = os.path.join(abs_path, "source")
        self.fix_file = fix_file
        self.fix_loc = fix_loc
        self.crash_file = crash_file
        self.crash_loc = crash_loc
        self.build_cmd = build_cmd
        self.build_sh = os.path.join(abs_path,"setup.sh")
        self.tool_build_sh = os.path.join(abs_path,"repair_build.sh")



    