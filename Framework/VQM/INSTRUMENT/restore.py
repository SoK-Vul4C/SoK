import codecs
import re
from multiprocessing import Pool
import os
import csv
import clang.cindex
from clang.cindex import CursorKind
import shutil

def restore():
    with open('/tmp/test.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        for root, dirs, files in os.walk('/tmp/runtime'):
            path = os.path.relpath(root, '/tmp/runtime')
            levels = path.split(os.sep)
            if len(levels) == 1:
                writer.writerow(levels)

    index = clang.cindex.Index.create()

    with open('/tmp/test.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            if row[0] != '.':
                old_path = '.' 
                new_path = f'runtime/{row[0]}'
                if not os.path.isdir('runtime'):
                    os.mkdir('runtime')
                if not os.path.isdir(new_path):
                    os.mkdir(new_path)
                result_path = 'candidate_result'
                if not os.path.isdir(result_path):
                    os.mkdir(result_path)    
                new_file = f'/tmp/runtime/{row[0]}/out'
                with open(new_file, 'r') as f:
                    out_content = f.read()
                ind = out_content.find(" (")
                func_name = out_content[out_content.rfind(" ", 0, ind) + 1:ind]
                
                for filename in os.listdir(old_path):
                    if filename.endswith('OLD.c'):
                        old_file = os.path.join(old_path, filename) 
                        pre_version_file_tu = index.parse(old_file)
                        for cursor in pre_version_file_tu.cursor.walk_preorder():
                            if cursor.kind == CursorKind.FUNCTION_DECL and cursor.spelling == func_name:
                                    start_loc = cursor.extent.start.offset
                                    end_loc = cursor.extent.end.offset
                        with open(old_file, 'r') as f:
                            old_content = f.read()
                        new_content = old_content[:start_loc] + out_content + old_content[end_loc:]
                        with open(new_path + '/file.new.c', 'w') as f:
                            f.write(new_content) 
                        with open(result_path + f'/{row[0]}.c', 'w') as f:
                            f.write(new_content) 





def check_out(pre_version_file_str, post_version_file_str, out_beam, num_tokens, path):

    yizhi_list=list()
    pre_version_tokens = pre_version_file_str.replace(' //<S2SV>','').split(' ') + ["<S2SV_null>"] * num_tokens

    parse_error=True
    special = re.compile("^<S2SV_Mod(Start|End)>$")
    # Check each beam position
    idx = 0
    for out_str in out_beam:
        try:
            idx = idx + 1
            chk_file_str=""
            pre_tokens = ["<S2SV_null>"] * num_tokens
            out_tokens = out_str.split(' ')
            out_pre = out_tokens[1:num_tokens+1]
            out_idx = num_tokens+1
            i = 0
            while i <= len(pre_version_tokens)-num_tokens:
                if ' '.join(out_pre) == ' '.join(pre_tokens): # Found match
                    
                    while out_idx < len(out_tokens) and not special.match(out_tokens[out_idx]):
                        chk_file_str+=out_tokens[out_idx]+' '
                        out_idx+=1
                    if out_idx >= len(out_tokens): # Must have been Add action
                        out_pre = ['<S2SV_OUTNOMATCH>'] * num_tokens
                    else:
                        out_pre = out_tokens[out_idx+1:out_idx+num_tokens+1]
                        if (out_tokens[out_idx] == '<S2SV_ModEnd>'):
                            # Post token sequence is at least num_tokens+1 forward
                            # Jumping num_tokens+1 is safe because of while loop
                            pre_tokens = pre_version_tokens[i+1:i+num_tokens+1]
                            i+=num_tokens+1
                            while i <= len(pre_version_tokens) and ' '.join(out_pre) != ' '.join(pre_tokens): # No match yet
                                pre_tokens = pre_tokens[1:num_tokens]+[pre_version_tokens[i]]
                                i+=1
                            if i <= len(pre_version_tokens)-num_tokens:
                                chk_file_str += ' '.join(pre_tokens)+' '
                            else: # End-of-function special case
                                while pre_tokens[0] != '<S2SV_null>':
                                    chk_file_str += pre_tokens[0]+' '
                                    pre_tokens = pre_tokens[1:]
                            out_idx += num_tokens+1
                            if out_idx >= len(out_tokens): # Must have been Add action
                                out_pre = ['<S2SV_OUTNOMATCH>'] * num_tokens
                            else:
                                out_pre = out_tokens[out_idx+1:out_idx+num_tokens+1]
                                out_idx += num_tokens+1
                        else:
                            pre_tokens = ['<S2SV_NOMATCH>'] * num_tokens
                            out_idx += num_tokens+1
                else:
                    pre_tokens = pre_tokens[1:num_tokens]+[pre_version_tokens[i]]
                    if i < len(pre_version_tokens)-num_tokens:
                        chk_file_str+=pre_version_tokens[i]+' '
                    i+=1
            # Check if all delta tokens were processed
            if out_idx == len(out_tokens):
                parse_error=False    
            out = chk_file_str[:-1].replace('<S2SV_blank>',' ')  
            dir_path = os.path.join(path, str(idx))
            # with open("skd", mode='a')as f:
            #     f.write(out+"\n")
            new = post_version_file_str.replace(' //<S2SV>','').replace('<S2SV_blank>',' ') 
            old = pre_version_file_str.replace(' //<S2SV>','').replace('<S2SV_blank>',' ')
            if out != old:
                os.makedirs(dir_path, exist_ok=True)
                out_path = os.path.join(dir_path, 'out')
                with codecs.open(out_path, 'w', 'utf-8') as f:
                    f.write(out)
            else:
               yizhi_list.append(str(idx)) 
            #if chk_file_str[:-1] == post_version_file_str.replace(' //<S2SV>',''):
                #break
        except Exception as e:
            print("Check_out fail: "+str(e)+str(idx))
    return yizhi_list

if __name__=="__main__":
    num_tokens = 3
    chk_lines=""
    prediction_path = 'src.txt.predictions'
    old_tokens_path = 'old.tokens'
    new_tokens_path = 'new.tokens'
    if os.path.isfile(prediction_path) and os.path.isfile(old_tokens_path) and os.path.isfile(new_tokens_path):

        restore_path = '/tmp/runtime'
        if not os.path.isdir(restore_path):
            os.mkdir(restore_path)

        pre_version_file = old_tokens_path
        post_version_file = new_tokens_path
        out_path = prediction_path
        out_lines = open(out_path).read().split('\n')

        beam_width=50
        out_beam = out_lines[0:beam_width]
        pre_version_file_str = open(pre_version_file).read()
        post_version_file_str = open(post_version_file).read()
        if pre_version_file_str.endswith(' '):
            pre_version_file_str=pre_version_file_str[:-1]
        if post_version_file_str.endswith(' '):
            post_version_file_str=post_version_file_str[:-1]
        if True:
            yizhi_list=check_out(pre_version_file_str, post_version_file_str, out_beam, num_tokens,restore_path)

    with open("yizhi.list",mode='w') as f:
        f.write('|'.join(yizhi_list))

    if not os.path.isdir('runtime'):
        os.mkdir('runtime')
    if not os.path.isdir('candidate_result'):
        os.mkdir('candidate_result')    
    restore()
    restore_path = '/tmp/runtime'
    shutil.rmtree(restore_path)

