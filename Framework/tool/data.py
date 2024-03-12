import os
import docker
import random

from logger import logger

class Data(object):

    def __init__(self, abs_path_host, abs_path_container, binary, cmd, exploit, build_cmd, fix_file=None, fix_loc=None, crash_file=None, crash_loc=None):
        self.abs_path_container = abs_path_container
        self.abs_path_host = abs_path_host
        self.binary = binary
        self.poc_cmd = cmd
        self.exploit = exploit
        self.test = os.path.join(abs_path_container, "test.sh")
        self.runtime_host = os.path.join(abs_path_host, "runtime")
        self.runtime_container = os.path.join(abs_path_container, "runtime")
        self.source = os.path.join(abs_path_container, "source")
        self.fix_file = fix_file
        self.fix_loc = fix_loc
        self.crash_file = crash_file
        self.crash_loc = crash_loc
        self.build_cmd = build_cmd
        self.build_sh = os.path.join(abs_path_container,"setup.sh")
        self.tool_build_sh = os.path.join(abs_path_container,"repair_build.sh")

    def host_path_2_container_path(self, path: str):
        if path.startswith(self.abs_path_host):
            return path.replace(self.abs_path_host, self.abs_path_container, 1)


    