import sys
import os
import subprocess
import signal
import time
import shutil


base_path = "/Users/pgarrett/Documents/facultad/tesis/"
output_base_path = base_path + "em-thesis-utils/liveDemo/results"
em_jar = base_path + "EvoMaster/core/target/evomaster.jar"
copy_path = base_path + "em-thesis-utils/bkpLiveDemo"

useExp = False
resultsPath = "/ga"
duration = "60s"

args = sys.argv

if not len(sys.argv) > 1:
    print("Wrong usage, missing parameter `ga` or `exp`")
    exit(-1)

if args[1] == "exp":
    useExp = True
    resultsPath = "/exp"
    duration = "2m"


base_params = ["--blackBox", "true", "--maxTime", duration, "--ratePerMinute", "60", "--bbSwaggerUrl", "http://localhost:5000/numberSpec.yaml"]

execution_params = base_params + ["--outputFormat", "PYTHON_UNITTEST", "--outputFolder", base_path + "em-thesis-utils/gendDemo/results" + resultsPath, "--namingStrategy", "ACTION"]
print_params     = base_params + ["--outputFormat", "PYTHON_UNITTEST", "--outputFolder", base_path + "em-thesis-utils/liveDemo/results" + resultsPath, "--namingStrategy", "ACTION"]

if useExp:
    execution_params += ["--nameWithQueryParameters", "true"]

em_to_run = ["java", "-jar", em_jar] + execution_params
em_to_print = ["java", "-jar", em_jar] + print_params
print(f"Running: {em_to_print}")
subprocess.run(em_to_run)
print("\n============================================================\n\n")
time.sleep(2)

shutil.copytree(copy_path + resultsPath, output_base_path + resultsPath)

# Step 4: Run Python unittests
print("Executing python tests\n")
py_to_run = ["python3.9", "-m", "unittest", "discover", "-s", output_base_path + resultsPath, "-p", "*_Test.py"]
print(f"Running: {py_to_run}\n")
subprocess.run(py_to_run)

os.chdir(base_path)
