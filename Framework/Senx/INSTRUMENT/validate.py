import os
import sys
import subprocess
import signal
import enum
import shutil
import configparser
import values

#to identify crashes caused by sanitize
ubsan_exit_code = 54 
asan_exit_code = 55

# sanitizers environment options for it to work as expected
sanitizer_env = {"ASAN_OPTIONS":
                    "redzone=64:" +
                    "exitcode=" + str(asan_exit_code),
                 "UBSAN_OPTIONS":
                    "halt_on_error=1:" +
                    "exitcode=" + str(ubsan_exit_code) + ":" +
                    "print_stacktrace=1"}
# include original env so that other env vars are not thrown away
modified_env = {**os.environ, **sanitizer_env}

def parse_config_and_setup_runtime(config_file):
    config = configparser.ConfigParser()
    with open(config_file, "r") as f:
        config.read_string("[DEFAULT]\n" + f.read())
    config_dict = config['DEFAULT']
    
    values.prog_cmd=config_dict['cmd']
    values.build_cmd=config_dict['build-cmd']
    values.bin_path=config_dict['binary']
    values.poc_path=config_dict['exploit']
    values.source_path=config_dict['source-dir']
    values.runtime_path = config_dict['runtime-dir']
    values.bug_file_path_in_source=os.path.join(values.source_path,config_dict['fix-file-path'])
    values.result_path=os.path.join(values.runtime_path,'result')

def parse_config_and_update_bug_file_path(config_file):
    config = configparser.ConfigParser()
    with open(config_file, "r") as f:
        config.read_string("[DEFAULT]\n" + f.read())
    config_dict = config['DEFAULT']
    values.bug_file_path_in_source=os.path.join(values.source_path,config_dict['fix-file-path'])

def parse_prog_output_for_crash_line(out, rc):
    bug_type = ""
    crash_lines = list()

    is_asan = (rc == asan_exit_code)
    is_ubsan = (rc == ubsan_exit_code)
    is_unexpected_exit= (is_asan==0 and is_ubsan==0 and rc!=0 )
    if not is_asan and not is_ubsan and not is_unexpected_exit: 
        return bug_type, crash_lines

    splitted_out = out.splitlines()
    if len(splitted_out) == 0:
        return bug_type, crash_lines

    
    # Case 1: Assertion failed
    if is_unexpected_exit:
        bug_type="error"
        info_line=""
        for line in splitted_out:
            if 'Assertion' in line and 'failed' in line:
                info_line = line
        if not info_line:
            crash_lines =list()
        else:
            bug_type="assertion failed"
            crash_lines = [info_line]

    # Case 2: UBSAN
    if is_ubsan:
        bug_type = "ubsan"
        info_line = ""
        for line in splitted_out:
            if 'runtime error' in line:
                info_line = line
        if not info_line: 
            crash_lines = list()
        else:
            crash_loc = info_line.split('runtime error')[0]
            crash_lines = [ crash_loc ]

    # Case 3: ASAN
    elif is_asan:
        processed_frames = set()
        for line in splitted_out:
            if '#' not in line:
                continue
            words = line.split()
            curr_frame = words[0]
            if curr_frame in processed_frames:
                continue
            processed_frames.add(curr_frame)
            if line[-1] != ')':
                crash_lines.append(words[-1])
        # (2) get the type of bug
        for line in splitted_out:
            if 'ERROR: AddressSanitizer:' in line:
                bug_type = line.split()[2]
                break
            if 'WARNING: AddressSanitizer failed to allocate' in line:
                bug_type = 'failed to allocate'
                break
            if 'ERROR: LeakSanitizer:' in line:
                bug_type = 'memory leaks'
                break

    return bug_type, crash_lines

def init_prog_cmd_with_input_file(input_file,inited_cmd):
    inited_cmd = inited_cmd.replace("<exploit>", input_file)
    return inited_cmd

def run_bin(input_file,timeout):
    inited_prog_cmd = init_prog_cmd_with_input_file(input_file, values.prog_cmd)
    cmd = values.bin_path + ' ' + inited_prog_cmd

    try:
        proc = subprocess.Popen(cmd, start_new_session=True, shell=True,
            encoding='utf-8', universal_newlines=True, errors='replace',
            env=modified_env,
            stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        stdout,stderr=proc.communicate(timeout=timeout*60)
    except subprocess.TimeoutExpired as e:
        os.killpg(os.getpgid(proc.pid), signal.SIGTERM)
        raise e

    bug_type, crash_lines = parse_prog_output_for_crash_line(stderr, proc.returncode)

    return bug_type, crash_lines, proc.returncode, stderr

def rebuild_project():
    proc = subprocess.Popen(values.build_cmd, shell=True,
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL,
        cwd=values.source_path)
    proc.communicate(timeout=5*60)
    rc = proc.returncode
    return rc

def classify_execution_return_value(bug_type, crash_lines, rc, result_dic):

    result_dic['check_patch'] = 'Pass'

    if crash_lines:
        if bug_type == values.bug_type_origin and crash_lines[0] == values.crash_lines_origin[0]:
            result_dic['check_patch'] = 'Fail'
        elif bug_type == values.bug_type_origin and crash_lines[0] != values.crash_lines_origin[0]:
            result_dic['check_patch'] = 'The bug type is the same, but the crash line number is different'
        elif bug_type != values.bug_type_origin:
            result_dic['check_patch'] = 'The types of bugs are different'

    return result_dic

def rebuild_and_validate_patch(input_file, error_report_path):
    # rebuild
    result_dic={}
    build_rc = rebuild_project()
    result_dic['check_compile'] = 'Pass'
    if build_rc != 0:
        result_dic['check_compile'] = 'Fail'
        result_dic['check_patch'] = 'Fail'
        return result_dic
    
    # validate
    bug_type, crash_lines, rc, stderr=run_bin(input_file, 5)
    with open(error_report_path, mode='a', encoding='utf-8') as f:
        f.write('=================patched error report======================\n')
        f.write(stderr)
    result_dic = classify_execution_return_value(bug_type=bug_type, crash_lines=crash_lines, rc=rc, result_dic=result_dic)
    return result_dic

def run_bin_orig(input_file, error_report_path):
    bug_type, crash_lines, rc, stderr = run_bin(input_file, 5)
    with open(error_report_path, mode='a', encoding='utf-8') as f:
        f.write('=================origin error report======================\n')
        f.write(stderr)
    return bug_type, crash_lines, rc

def save_result(result, tag):
    with open(values.result_path,mode='a',encoding='utf-8') as f:
        f.write(f"==================this is the {tag} patch result===============\n")
        f.write(f"check_compile:{result['check_compile']}\n")
        f.write(f"check_patch:{result['check_patch']}\n")


def replace_muti_files(src_list,tgt_list):
    for src,tgt in zip(src_list,tgt_list):
        shutil.copy2(src,tgt)


def main():
    config_file=sys.argv[1]
    parse_config_and_setup_runtime(config_file)
    
    with open(values.result_path,mode='a') as f:pass
    
    dir_list=[]
    for dir in os.listdir(values.runtime_path):
        if os.path.isdir(os.path.join(values.runtime_path,dir)):
            dir_list.append(dir)

    for dir in dir_list: 
        #config
        temp_path=os.path.join(values.runtime_path,dir)
        parse_config_and_update_bug_file_path(os.path.join(temp_path,'config'))

        buggy_list=values.bug_file_path_in_source.split("|")
        
        buggy_abs_list=[]
        patched_abs_list=[]
        buggy_in_source_abs_list=[]

        for buggy_file in buggy_list:
            if buggy_file.strip()=="":
                continue
            file_name=os.path.split(buggy_file)[1]
            values.old_file_path=os.path.join(temp_path,'{file_name}.old'.format(file_name=file_name))
            buggy_abs_list.append(values.old_file_path)

            values.patched_file_path=os.path.join(temp_path,file_name)
            patched_abs_list.append(values.patched_file_path)

            values.bug_file_path_in_source=os.path.join(values.source_path,buggy_file)
            buggy_in_source_abs_list.append(values.bug_file_path_in_source)

            shutil.copy2(values.bug_file_path_in_source,values.old_file_path)
            values.error_report_path=os.path.join(temp_path,'error.log')

        #origin exec
        values.bug_type_origin, values.crash_lines_origin, rc = run_bin_orig(values.poc_path, values.error_report_path)
        print(values.bug_type_origin,values.crash_lines_origin)

        #patch validate 
        replace_muti_files(patched_abs_list,buggy_in_source_abs_list)
        result_dic=rebuild_and_validate_patch(values.poc_path, values.error_report_path)
        
        print(result_dic)
        save_result(result_dic, tag=dir)

        #restore vulnerability version
        replace_muti_files(buggy_abs_list,buggy_in_source_abs_list)
        rebuild_project()

if __name__ == "__main__":
    main()