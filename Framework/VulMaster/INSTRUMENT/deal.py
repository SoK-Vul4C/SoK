import json
from pprint import pprint
with open('generated_results/final_model.json',mode='r') as f:
    predictions = json.load(f)
# pprint(predictions[0])

import csv
import os


file="src.txt.predictions"
content=""
for j in range(0,50):
    prediction = predictions[j]['generated_answer'].split("are: ", 1)[1].replace("<vul-start>", '<S2SV_ModStart>').replace("<vul-end>", '<S2SV_ModEnd>')
    content += prediction + "\r\n"
# print(content)
with open(file, mode='w',encoding='utf-8') as f:
    f.write(content)