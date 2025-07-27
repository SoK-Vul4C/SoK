import os
import shutil

from setuptools import setup
from setuptools.command.install import install

import configparser
from pathlib import Path


class Vul4CConfigure(install):
    user_options = install.user_options + [
        ("location=", None, "Specify location for vul4c data directory (default: ~/vul4c_data).")
    ]

    def initialize_options(self):
        install.initialize_options(self)
        self.location = None

    def finalize_options(self):
        install.finalize_options(self)
        if self.location is None:
            self.location = os.path.expanduser("~/vul4c_data")
        if os.path.exists(self.location):
            print(f"ERROR: Directory already exists: {self.location}")
            exit(1)
        os.environ["VUL4C_DATA"] = self.location

    def run(self):
        os.makedirs(self.location, exist_ok=True)
        shutil.copy(os.path.join("Vul4C_Src", "vul4c.ini"), self.location)
        print(f"Data directory and files setup at: {self.location}")

        install.run(self)

setup_dir = Path(__file__).parent.resolve()
vul4c_ini_path = setup_dir / "Vul4C_Src/vul4c.ini"

if not vul4c_ini_path.exists():
    vul4c_ini_path.touch()

config = configparser.ConfigParser()
config.read(vul4c_ini_path)

if "VUL4C" not in config:
    config["VUL4C"] = {}

config["VUL4C"]["VUL4C_PATH"] = str(setup_dir)

with open(vul4c_ini_path, "w") as configfile:
    config.write(configfile)

setup(
    name='vul4c',
    version='1.0',
    description='Vul4C: A Dataset of Reproducible C/C++ Vulnerabilities',
    url='https://github.com/SoK-Vul4C/SoK',
    license='MIT',
    packages=['Vul4C_Src'],
    install_requires=[
        'unidiff',
        'loguru',
        'GitPython'
    ],
    cmdclass={
        'install': Vul4CConfigure
    },
    entry_points={
        'console_scripts': ['vul4c = Vul4C_Src.main:main']
    },
)