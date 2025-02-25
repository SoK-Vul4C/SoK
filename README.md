# SoK: Automated Vulnerability Repair

The benchmark dataset Vul4C and framework for automated vulnerability repair in C/C++. 

## Table of Contents

1. [Benchmark Dataset Vul4C](#1-benchmark-dataset-vul4c)
2. [Framework](#2-framework)
3. [Repository Structure](#3-repository-structure)
4. [Usage](#4-usage)
<!-- 5. [Results](#5-results) -->

## 1. Benchmark Dataset Vul4C

Our benchmark dataset Vul4C contains 140 vulnerabilities over 19 CWE types and 23 software.

Here is the statistics of Vul4C.

| CWE Type  |  Total  | Single-Line | Single-Hunk | Multi-Hunks | Multi-Files |
| :-------: | :-----: | :---------: | :---------: | :---------: | :---------: |
|  CWE-119  |   35    |     10      |      7      |     12      |      6      |
|  CWE-125  |   29    |      3      |      4      |     12      |     10      |
|  CWE-476  |   16    |      1      |      4      |     10      |      1      |
|  CWE-369  |   11    |      1      |      8      |      2      |      0      |
|  CWE-190  |    9    |      0      |      0      |      6      |      3      |
|  CWE-787  |   14    |      3      |      1      |      6      |      4      |
|  CWE-20   |    6    |      0      |      2      |      2      |      2      |
|  CWE-416  |    4    |      0      |      0      |      4      |      0      |
|  CWE-835  |    4    |      0      |      1      |      2      |      1      |
|  CWE-189  |    2    |      1      |      0      |      1      |      0      |
|  CWE-617  |    2    |      0      |      1      |      1      |      0      |
|  CWE-120  |    1    |      0      |      0      |      0      |      1      |
|  CWE-415  |    1    |      0      |      1      |      0      |      0      |
|  CWE-704  |    1    |      0      |      0      |      1      |      0      |
|  CWE-770  |    1    |      0      |      1      |      0      |      0      |
|  CWE-191  |    1    |      1      |      0      |      0      |      0      |
|  CWE-682  |    1    |      0      |      0      |      0      |      1      |
|  CWE-843  |    1    |      0      |      0      |      1      |      0      |
|    N/A    |    5    |      0      |      1      |      3      |      1      |
| __Total__ | __144__ |   __20__    |   __31__    |   __63__    |   __30__    |

## 2. Framework

|     Tool    |    Venue    | Github Repo                                  |
| :---------: | :---------: | :------------------------------------------- |
|  VulRepair  |  FSE'22     | <https://github.com/awsm-research/VulRepair> |
|   VRepair   |  TSE'22     | <https://github.com/ASSERT-KTH/VRepair>      |
|     VQM     |  TOSEM'24   | <https://github.com/awsm-research/VQM>       |
|  VulMaster  |  ICSE'24    | <https://github.com/soarsmu/VulMaster_>      |
| ExtractFix  |  TOSEM'20   | <https://extractfix.github.io/>              |
|  VulnFix    |  ISSTA'22   | <https://github.com/yuntongzhang/vulnfix>    |
| CrashRepair |  TOSEM'24   | <https://github.com/nus-apr/CrashRepair>     |
|    Senx     |  S&P'19     | Not open source                              |
|   SAVER     |  ICSE'20    | <https://github.com/kupl/SAVER_public/>      |
| FootPatch   |  ICSE'18    | <https://github.com/squaresLab/footpatch>    |
|   IntPTI    |  TDSC'19    | <https://github.com/45258E9F/IntPTI>         |

## 3. Repository Structure

This repository is structured as follow:

```
|----- Vul4C-Benchmark
    |----- [Software]
        |----- [CVE ID]
            |----- [CVE ID]_[CWE ID]_[filename].diff 
            |----- [CVE ID]_[CWE ID]_[filename]_NEW.c
            |----- [CVE ID]_[CWE ID]_[filename]_OLD.c
            |----- README.txt 
            |----- exploit
            |----- setup.sh
|----- Framework
|----- Results
    |----- Results.xlsx: All experimental results.
    |----- [Vulnerability Repair Tools]
        |----- [Software]
            |----- [CVE ID]
                |----- 50-Candidates: This folder contains all 50 candidates generated by models. (Only for learning-based methods.)
                |----- Candidate Patches: This folder contains all patches generated by vulnerability repair tools. 
                                          (For learning-based methods, this folder contains all successfully restored patches within original 50 generated candidates.)
                |----- Compilable Patches: This folder contains all successfully compiled patches within all candidate patches.
                |----- Plausible Patches: This folder contains all patches that successfully pass vulnerability exploit test within all compilable patches.
                |----- Correct Patches: This folder contains all correct patches assessed by humans.
|----- README.md
```

## 4. Usage

Please ensure that you have properly installed docker and Docker SDK for Python  before using.

Following is a startup command for vul4c you can invoke it on selected repair tool and benchmark.

```
python3 Framework/vul4c.py --tool "Vulnfix" --software "libxml2" --CVEID "CVE-2017-5969" 
```

You can find the results in the folder `vul4c-result`,and the folder where the results will be stored will be named ` $tool_$CVEID_$timestamp`

<!-- ## 5. Results -->

