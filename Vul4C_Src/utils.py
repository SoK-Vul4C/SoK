import functools
import os
import subprocess
import urllib.request
import zipfile
from typing import List

import git
from loguru import logger

from Vul4C_Src.config import DATASET_PATH, VUL4C_DATA, VUL4C_PATH

SEPARATOR = 60 * "-"
THICK_SEPARATOR = 60 * "="


def suffix_filename(filename: str, suffix: str):
    """
    Puts version in the filename if needed.
    """
    name_list = filename.split(".")
    return f"{name_list[0]}_{suffix}.{name_list[1]}" if suffix else ".".join(name_list)


def log_frame(title: str):
    def decorator_log_frame(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            start = f" START {title} "
            logger.info(start.center(60, "="))
            try:
                func(*args, **kwargs)
            except Exception as err:
                logger.error(f"{err.__class__.__name__}: {err}")
                exit(1)
            finally:
                end = f" END {title} "
                logger.info(end.center(60, "="))

        return wrapper

    return decorator_log_frame

def check_compiler(compiler_name, required_version=None):
    try:
        result = subprocess.run(
            [f"{compiler_name}-{required_version}", "--version"],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
        )
        if result.returncode == 0:
            version_output = str(result.stdout).strip()
            #logger.info(f"{compiler_name} found:\n{version_output}")
            if required_version and required_version not in version_output:
                #logger.error(f"Required version {required_version} not found!")
                return False
            return True
        else:
            #logger.error(f"{compiler_name} not available!")
            return False
    except FileNotFoundError:
        #logger.error(f"{compiler_name} not installed!")
        return False

def check_status():
    """
    Checks availability of vul4c dependencies.
    """

    # check vul4c.ini
    vul4c_config = check_config()


    # check vul4c dataset
    vul4c_dataset = bool(DATASET_PATH) and os.path.exists(DATASET_PATH)
    vul4c_path = bool(VUL4C_PATH) and os.path.exists(VUL4C_PATH)


    def log_result(message: str, success: bool):
        logger.log("SUCCESS" if success else "ERROR", f"{message}: {'OK' if success else 'NOT FOUND'}")

    gcc_5 = check_compiler("gcc", "5")
    clang_6 = check_compiler("clang", "6.0")
    clang_10 = check_compiler("clang", "10")
    clangpp_6 = check_compiler("clang++", "6.0")
    clangpp_10 = check_compiler("clang++", "10")
    log_result("GCC 5", gcc_5)
    log_result("Clang 6.0", clang_6)
    log_result("Clang 10", clang_10)
    log_result("Clang++ 6.0", clangpp_6)
    log_result("Clang++ 10", clangpp_10)
    log_result("VUL4C path", vul4c_path)
    log_result("VUL4C config file", vul4c_config)
    log_result("VUL4C dataset", vul4c_dataset)


def check_config():
    """
    Check if vul4c is configured correctly.
    """

    errors = []

    if not os.path.exists(os.path.join(VUL4C_DATA, "vul4c.ini")):
        logger.critical("The vul4c.ini file does not exist!")
        return False

    for error in errors:
        logger.error(f"CONFIG ERROR - {error}")
    if errors:
        logger.warning("Please modify the vul4c.ini file or environment variables to resolve the error.")

    return not bool(errors)


def find_test_reports(project_dir: str) -> List[str]:
    report_files = []
    for r, dirs, files in os.walk(project_dir):
        for file in files:
            file_path = os.path.join(r, file)
            if (("target/surefire-reports" in file_path
                 or "target/failsafe-reports" in file_path
                 or "build/test-results" in file_path)  # gradle
                    and file.endswith('.xml') and file.startswith('TEST-')):
                report_files.append(file_path)
    return report_files


def evaluate_reproduction_results(results: dict):
    if results["tests_ran"] and results["has_tests"]:
        tests_status = "PASS"
    elif results["tests_ran"] or results["has_tests"]:
        tests_status = "ERROR"
    else:
        tests_status = "SKIP"

    if results["spotbugs_ran"] and results["spotbugs_ok"] and results["has_spotbugs_warnings"]:
        spotbugs_status = "PASS"
    elif results["spotbugs_ran"] or results["spotbugs_ok"] or results["has_spotbugs_warnings"]:
        spotbugs_status = "ERROR"
    else:
        spotbugs_status = "SKIP"

    if ((tests_status == "PASS" and spotbugs_status == "SKIP") or
            (tests_status == "SKIP" and spotbugs_status == "PASS") or
            (tests_status == "PASS" and spotbugs_status == "PASS")):
        log_level = "SUCCESS"
    elif tests_status in {"ERROR", "SKIP"} and spotbugs_status in {"ERROR", "SKIP"}:
        log_level = "ERROR"
    else:
        log_level = "WARNING"

    return log_level, tests_status, spotbugs_status


def is_relative_to(path: str, base: str) -> bool:
    abs_path = os.path.abspath(path)
    abs_base = os.path.abspath(base)
    return abs_path.startswith(abs_base + os.sep)