with open("config") as f:
    contents=f.read().split("\n")
dic={}
for content in contents:
    if content!="":
        l=content.split("=")
        dic[l[0]]=l[1].strip()
bin_name=dic["binary"].split("/")[-1]
cmd=dic["cmd"].replace("<exploit>",dic["exploit"])
print(dic["binary"])
print(bin_name)
print(cmd)