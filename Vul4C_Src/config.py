import configparser
import os
from os.path import normpath, expanduser


def get_config(section: str, config_name: str, default: str = "") -> str:
    """
    Returns the value of the given config entry.
    First the vul4c.ini file is checked,
    if the value or the config file is not found,
    the environment variables are checked for the provided name,
    and if none of the two previous checks have a value, the default value is used.

    :param section: section name in the config file
    :param config_name: config entry name in the config file and/or in the env vars
    :param default: default value if no config value is found
    :return:    string value of the config entry
    """
    config = configparser.ConfigParser()
    config_path = os.path.join(VUL4C_DATA, "vul4c.ini")
    config.read(config_path, encoding="utf-8")

    value = config.get(section, config_name, fallback=None)
    if value is None or value == "":
        value = os.environ.get(config_name)
    return value if value else default


# VUL4C
VUL4C_DATA = normpath(os.environ.get("VUL4C_DATA", expanduser("~/vul4c_data")))
LOG_TO_FILE = get_config("VUL4C", "LOG_TO_FILE", "1") == "1"
FILE_LOG_LEVEL = get_config("VUL4C", "FILE_LOG_LEVEL", "INFO").upper()
VUL4C_PATH = normpath(get_config("VUL4C", "VUL4C_PATH", os.getcwd()))
DATASET_PATH = normpath(get_config("VUL4C", "DATASET_PATH", os.path.join(VUL4C_PATH, "Vul4C_Src", "Vul4C.json")))


# DIRS
VUL4C_OUTPUT = normpath(get_config("DIRS", "VUL4C_WORKDIR", "VUL4C"))
