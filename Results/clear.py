import os 
import shutil
for root,dirs,files in os.walk("."):
    if "Compileable_Patches" in root:
        dst=os.path.join(os.path.split(root)[0],"Compilable_Patches")
        os.rename(root,dst)