import csv
import json
import os
import re
import shutil
import subprocess
import xml.etree.ElementTree as ElementTree
from shutil import copytree, ignore_patterns

import git
from loguru import logger

import Vul4C_Src.utils as utils
from Vul4C_Src.config import VUL4C_OUTPUT, LOG_TO_FILE, DATASET_PATH, VUL4C_PATH


class VulnerabilityNotFoundError(Exception):
    def __init__(self, message):
        super().__init__(message)


class Vulnerability:
    def __init__(self, data: dict):
        self.data = data

        self.vul_id = self.get_column("vul_id")
        self.cve_id = self.get_column("cve_id")
        self.cwe_id = self.get_column("cwe_id")
        self.project = self.get_column("repo_slug").replace("/", "_"),
        self.project_url = f"https://github.com/{self.get_column('repo_slug')}"
        self.build_system = self.get_column("build_system")
        self.compliance_level = self.get_column("compliance_level")
        self.compile_cmd = self.get_column("compile_cmd")
        self.test_all_cmd = self.get_column("test_all_cmd")
        self.test_cmd = self.get_column("test_cmd")
        self.cmd_options = self.get_column("cmd_options")
        self.failing_module = self.get_column("failing_module")
        self.fixing_commit_hash = self.get_commit_hash(self.get_column("human_patch"))
        self.human_patch_url = self.get_column("human_patch")
        self.failing_tests = [value.strip() for value in self.get_column("failing_tests").split(',')]
        self.warning = [value.strip() for value in self.get_column("warning").split(',')]

    @staticmethod
    def get_commit_hash(commit_url: str) -> str:
        commit_hash = commit_url.split("/")[-1]
        if ".." in commit_hash:
            return commit_hash.split("..")[-1].strip()
        else:
            return commit_hash.strip()

    def get_column(self, column: str) -> str:
        value = self.data.get(column)
        if isinstance(value, str):
            value = value.strip()
        elif isinstance(value, list):
            value = str(",".join(value))
        elif value is None:
            # ignore None values except vul_id
            if column == "vul_id":
                raise ValueError("Cannot find vul_id in the file!")
            value = ""
        return value

    def to_json(self, file_path: str = None, indent: int = 2) -> str:
        vulnerability_dict = {key: value for key, value in self.__dict__.items() if key != 'data'}
        if file_path:
            with open(file_path, "w", encoding="utf-8") as json_file:
                json.dump(vulnerability_dict, json_file, indent=indent)
        else:
            return json.dumps(vulnerability_dict, indent=indent)

    @classmethod
    def from_json(cls, project_dir: str):
        try:
            logger.debug("Reading vulnerability from file...")
            with open(os.path.join(project_dir, VUL4C_OUTPUT, "vulnerability_info.json"), "r") as info_file:
                # need to rename some entries
                vul = json.load(info_file)
                #data["repo_slug"] = data["project"]
                #data["human_patch"] = data["human_patch_url"]
                #vul = cls(data)
            #assert vul.vul_id is not None, "vul_id not found in json, info file probably empty or incomplete"
            return vul
        except (OSError, AssertionError) as err:
            logger.debug(err)
            raise VulnerabilityNotFoundError("No vulnerability found in the directory!")


def load_vulnerabilities() -> dict:
    """
    Loads vulnerability information from the csv dataset into a dictionary of Vulnerability objects.
    The program exits if the dataset is not found.

    :return: dictionary of Vulnerability objects where the key is the vul id
    """
    with open(DATASET_PATH, encoding="utf-8") as dataset_file:
        vulnerabilities = json.load(dataset_file)
        return vulnerabilities


def get_vulnerability(vul_id: str) -> Vulnerability:
    """
    Loads specified vulnerability from the dataset.
    Raises VulnerabilityNotFoundError if the vulnerability is not found.

    :param vul_id: vulnerability id
    :return: Vulnerability object with vulnerability data
    """

    vul = load_vulnerabilities().get(vul_id)
    if vul is None:
        raise VulnerabilityNotFoundError(f"Vulnerability not found: {vul_id}!")

    return vul


def get_info(vul_id: str) -> None:
    """
    Logs information about the specified vulnerability.
    Raises VulnerabilityNotFoundError if the vulnerability is not found.

    :param vul_id: vulnerability id
    """
    vul = get_vulnerability(vul_id)
    vul_json = json.dumps(vul, indent=2, ensure_ascii=False).encode("ascii", errors="replace").decode("ascii")
    logger.info(vul_json)


def checkout(vul_id: str, project_dir: str, force: bool = False) -> None:
    vul = get_vulnerability(vul_id)
    if force and os.path.exists(project_dir):
        shutil.rmtree(project_dir)
        logger.info("Removing project directory...")

    assert not os.path.exists(project_dir), f"Directory '{project_dir}' already exists!"

    commands = vul["get_command"]
    work_dir = project_dir
    if not os.path.exists(work_dir):
        os.makedirs(work_dir, exist_ok=True)
    for cmd in commands:
        if cmd.startswith("cd source"):
            work_dir = os.path.join(work_dir, "source")
            continue

        result = subprocess.run(cmd, shell=True, cwd=work_dir)
        if result.returncode != 0:
            logger.error(f"Failed to run command: {cmd}")
            raise subprocess.CalledProcessError(result.returncode, cmd)
        
    # 创建json文件
    json_path = os.path.join(project_dir, VUL4C_OUTPUT, "vulnerability_info.json")
    os.makedirs(os.path.dirname(json_path), exist_ok=True)
    with open(json_path, "w", encoding="utf-8") as json_file:
        json.dump(vul, json_file, indent=4)
    
    # 拷贝exploit
    exploit_path = os.path.join(VUL4C_PATH, "Vul4C-Benchmark", vul["project"], vul_id)
    for file in os.listdir(exploit_path):
        # logger.info(f"Checking exploit file: {file}")
        if file.startswith("exploit"):
            shutil.copy(os.path.join(exploit_path, file), os.path.join(project_dir, VUL4C_OUTPUT))
            logger.info(f"Copied exploit file: {file}")



def build(project_dir: str, suffix: str = None, clean: bool = False) -> None:

    vul = Vulnerability.from_json(project_dir)
    
    assert (vul["compile_command"] is not None and vul["compile_command"] != ""), f"No build command found for {vul.vul_id}"

    compile_cmd = vul["compile_command"]
    logger.debug(compile_cmd)

    log_path = os.path.join(project_dir, VUL4C_OUTPUT, utils.suffix_filename("compile.log", suffix))
    log_output = open(log_path, "w", encoding="utf-8") if LOG_TO_FILE else subprocess.DEVNULL
    work_dir = os.path.join(project_dir, "source")

    logger.info("Compiling...")
    for cmd in compile_cmd:
        subprocess.run(cmd,
                    shell=True,
                    stdout=log_output,
                    stderr=subprocess.STDOUT,
                    cwd=work_dir,
                    check=True)


def apply(project_dir: str, patch_path: str) -> None:


    vul = Vulnerability.from_json(project_dir)

    assert os.path.exists(patch_path), f"Patch file {patch_path} does not exist!"
    file_path = vul["file_name"]
    file_path = os.path.join(project_dir, "source", file_path)

    with open(file_path, 'r') as f:
        old_lines = f.readlines()

    # 读取补丁文件内容
    with open(patch_path, 'r') as f:
        patch_lines = f.readlines()


    logger.info(f"Applying patch to {file_path}...")
    # 解析补丁内容（统一格式 diff -u）
    patched_lines = old_lines.copy()
    i = 0
    while i < len(patch_lines):
        line = patch_lines[i]
        # 匹配 diff -u 的块头（如 "@@ -1,5 +1,6 @@"）
        if line.startswith('@@'):
            # 解析行号范围（如 "-1,5" 表示原文件从第1行开始，共5行）
            match = re.search(r'@@ \-(\d+),(\d+) \+(\d+),(\d+) @@', line)
            if not match:
                i += 1
                continue
            old_start = int(match.group(1)) - 1  # 转为0-based索引
            old_len = int(match.group(2))
            new_len = int(match.group(4))
            i += 1

            # 提取补丁块中的修改行
            old_end = old_start + old_len
            patch_chunk = []
            while i < len(patch_lines) and not patch_lines[i].startswith('@@'):
                patch_chunk.append(patch_lines[i])
                i += 1

            # 应用补丁（替换/删除/添加）
            new_chunk = []
            for chunk_line in patch_chunk:
                if chunk_line.startswith('-'):
                    continue  # 删除原行
                elif chunk_line.startswith('+'):
                    new_chunk.append(chunk_line[1:])  # 添加新行
                else:
                    new_chunk.append(chunk_line[1:])  # 保留原行（以空格开头）

            # 替换原文件中的对应行
            patched_lines[old_start:old_end] = new_chunk

        else:
            i += 1

    # 写入补丁后的文件
    output_path = file_path
    with open(output_path, 'w') as f:
        f.writelines(patched_lines)



def test(project_dir: str, suffix: str = None) -> dict:

    vul = Vulnerability.from_json(project_dir)

    assert vul["test_command"] is not None and vul["test_command"] != "", f"No test command found for {vul.vul_id}"
    assert (vul["compile_command"] is not None and vul["compile_command"] != ""), f"No build command found for {vul.vul_id}"

    test_cmd = vul["test_command"]
    logger.debug(test_cmd)
    

    # env = utils.get_java_home_env(vul.compliance_level)

    #if clean:
    #    utils.clean_build(project_dir, vul.build_system, env)
    #logger.info("getting compile command...")
    compile_cmd = vul["compile_command"]
    logger.debug(compile_cmd)

    log_path = os.path.join(project_dir, VUL4C_OUTPUT, utils.suffix_filename("compile.log", suffix))
    log_output = open(log_path, "w", encoding="utf-8") if LOG_TO_FILE else subprocess.DEVNULL
    work_dir = os.path.join(project_dir, "source")

    logger.info("Compiling...")
    for cmd in compile_cmd:
        subprocess.run(cmd,
                    shell=True,
                    stdout=log_output,
                    stderr=subprocess.STDOUT,
                    cwd=work_dir,
                    check=True)
    
    log_path = os.path.join(project_dir, VUL4C_OUTPUT, utils.suffix_filename("testing.log", suffix))
    log_output = open(log_path, "w", encoding="utf-8") if LOG_TO_FILE else subprocess.DEVNULL
    work_dir = os.path.join(project_dir, "source")

    logger.info(f"Running tests...")
    for cmd in test_cmd:
        result = subprocess.run(cmd,
                    shell=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    cwd=work_dir,
                    universal_newlines=True,
                    )
        if LOG_TO_FILE:
            log_output.write(result.stdout)
            log_output.flush()  # 确保立即写入

        # 打印到终端（logger）
        logger.info(result.stdout)


def reproduce(project_dir: str, suffix: str = None) -> dict:

    vul = Vulnerability.from_json(project_dir)

    assert vul["test_command"] is not None and vul["test_command"] != "", f"No test command found for {vul.vul_id}"
    test_cmd = vul["test_command"]
    logger.debug(test_cmd)
    log_path = os.path.join(project_dir, VUL4C_OUTPUT, utils.suffix_filename("testing.log", suffix))
    log_output = open(log_path, "w", encoding="utf-8") if LOG_TO_FILE else subprocess.DEVNULL
    work_dir = os.path.join(project_dir, "source")

    logger.info(f"Running tests...")
    for cmd in test_cmd:
        result = subprocess.run(cmd,
                    shell=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    cwd=work_dir,
                    universal_newlines=True,
                    )
        if LOG_TO_FILE:
            log_output.write(result.stdout)
            log_output.flush()  # 确保立即写入

        # 打印到终端（logger）
        logger.info(result.stdout)



def extract_patch_files(vul: Vulnerability, project_dir: str, repo_dir: str, compare_with_parent: bool = False) -> None:
    """
    Compares the human_patch commit to the vulnerable HEAD commit by default and
    extracts the modified files into separate folders.
    If compare_with_parent is True, it will compare to the parent of the fixing hash.
    It creates a 'human_patch' and a 'vulnerable' directory and places the corresponding files in them.
    A 'paths.json' file is also placed in each directory which points to the files location in the project.

    :param vul: vulnerability
    :param project_dir: path to the projects directory (where it is copied)
    :param repo_dir: path to the git directory with all the commits
    :param compare_with_parent: if True, it will compare the fixing commit hash with
    """
    human_patch_dir = os.path.join(project_dir, VUL4C_OUTPUT, "human_patch")
    vulnerable_dir = os.path.join(project_dir, VUL4C_OUTPUT, "vulnerable")
    paths_filename = "paths.json"

    repo = git.Repo(repo_dir)
    compare_to = f"{vul.fixing_commit_hash}~1" if compare_with_parent else repo.head.commit
    logger.debug(f"Comparing fixing commit to {'parent' if compare_with_parent else 'HEAD'}...")
    diff = repo.commit(vul.fixing_commit_hash).diff(compare_to)
    changed_java_source_files = []

    for modified_file in diff.iter_change_type("M"):
        file_path = modified_file.a_path
        if file_path.endswith(".java") and ("test/" not in file_path and "tests/" not in file_path):
            changed_java_source_files.append(modified_file)

    # extract vulnerable and patched code into separate folders
    os.makedirs(human_patch_dir)
    os.makedirs(vulnerable_dir)

    logger.debug("Writing file contents...")
    for file in changed_java_source_files:
        human_patch = file.a_blob
        vulnerable = file.b_blob

        # write human_patch file content
        with open(os.path.join(human_patch_dir, human_patch.name), "w",
                  encoding="utf-8") as f:
            f.write(human_patch.data_stream.read().decode("utf-8", errors="replace"))

        # write vulnerable file content
        with open(os.path.join(vulnerable_dir, vulnerable.name), "w",
                  encoding="utf-8") as f:
            f.write(vulnerable.data_stream.read().decode("utf-8", errors="replace"))

    # write paths into file
    logger.debug("Writing paths data...")
    with open(os.path.join(human_patch_dir, paths_filename), "w",
              encoding="utf-8") as f:
        json.dump({entry.a_blob.name: entry.a_blob.path for entry in changed_java_source_files}, f, indent=2)

    with open(os.path.join(vulnerable_dir, paths_filename), "w",
              encoding="utf-8") as f:
        json.dump({entry.b_blob.name: entry.b_blob.path for entry in changed_java_source_files}, f, indent=2)


def read_test_results(vul: Vulnerability, project_dir: str) -> dict:
    """
    Reads test results from result files.
    Modify from https://github.com/program-repair/RepairThemAll/blob/master/script/info_json_file.py

    :param vul: vulnerability
    :param project_dir: where the project is located
    :return:    dictionary of test results
    """

    logger.debug("Reading test results...")

    # find report files
    report_files = utils.find_test_reports(project_dir)

    failing_tests_count = 0
    error_tests_count = 0
    passing_tests_count = 0
    skipping_tests_count = 0

    failures = []
    passing_test_cases = set()
    skipping_test_cases = set()

    for report_file in report_files:
        with open(report_file, 'r') as file:
            tree = ElementTree.parse(file)
            testsuite_class_name = tree.getroot().attrib['name']
            test_cases = tree.findall('testcase')

            for test_case in test_cases:
                failure_list = {}
                class_name = test_case.attrib['classname'] if 'classname' in test_case.attrib else testsuite_class_name
                method_name = test_case.attrib['name']
                failure_list['test_class'] = class_name
                failure_list['test_method'] = method_name

                # failure
                failure = test_case.findall('failure')
                if len(failure) > 0:
                    failing_tests_count += 1
                    failure_list['failure_name'] = failure[0].attrib['type']
                    if 'message' in failure[0].attrib:
                        failure_list['detail'] = failure[0].attrib['message']
                    failure_list['is_error'] = False
                    failures.append(failure_list)
                    continue

                # error
                error = test_case.findall('error')
                if len(error) > 0:
                    error_tests_count += 1
                    failure_list['failure_name'] = error[0].attrib['type']
                    if 'message' in error[0].attrib:
                        failure_list['detail'] = error[0].attrib['message']
                    failure_list['is_error'] = True
                    failures.append(failure_list)
                    continue

                # skip
                skip_tags = test_case.findall("skipped")
                if len(skip_tags) > 0:
                    skipping_tests_count += 1
                    skipping_test_cases.add(class_name + '#' + method_name)
                    continue

                # pass
                passing_tests_count += 1
                passing_test_cases.add(class_name + '#' + method_name)

    repository = {'name': vul.project, 'url': vul.project_url, 'human_patch_url': vul.human_patch_url}
    overall_metrics = {'number_running': passing_tests_count + error_tests_count + failing_tests_count,
                       'number_passing': passing_tests_count,
                       'number_error': error_tests_count,
                       'number_failing': failing_tests_count,
                       'number_skipping': skipping_tests_count}
    tests = {'overall_metrics': overall_metrics,
             'failures': failures,
             'passing_tests': list(passing_test_cases),
             'skipping_tests': list(skipping_test_cases)}

    json_data = {'vul_id': vul.vul_id, 'cve_id': vul.cve_id, 'repository': repository, 'tests': tests}
    return json_data