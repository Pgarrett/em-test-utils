import os
import subprocess
import signal
import time
import shutil



base_path = "/run/datad/facultad/tesis/"
output_base_path = base_path + "em-thesis-utils/liveDemo/results"
em_jar = base_path + "EvoMaster/core/target/evomaster.jar"

useExp = False
resultsPath = "/ga"

args = sys.argv
if args[1] == "exp":
    useExp = True
    resultsPath = "/exp"


# Step 1: Run the server without inheriting its IO
process = subprocess.Popen(["python3", base_path +  "em-thesis-utils/liveDemo/server/numberServer.py"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

time.sleep(5)

try:
    base_params = ["--blackBox", "true", "--maxTime", "60s", "--ratePerMinute", "60", "--bbSwaggerUrl", "http://localhost:5000/numberSpec.yaml"]

    target_directory = output_base_path + resultsPath
    os.makedirs(target_directory, exist_ok=True)
    
    execution_params = base_params + ["--outputFormat", "PYTHON_UNITTEST", "--outputFolder", target_directory, "--namingStrategy", "ACTION"]

    nameWithQueryParameters = ["--nameWithQueryParameters", "true"]
    if useExp:
        execution_params += nameWithQueryParameters

    em_to_run = ["java", "-jar", em_jar] + execution_params
    print(f"Running: {em_to_run}")
    subprocess.run(em_to_run)
    print("\n============================================================\n\n")
    time.sleep(3)

    # Step 4: Run Python unittests
    # python_dir = os.path.join(m", "unittest", "discover", "-s", python_dir, "-p", "*_Test.py"]
    # print(f"Running: {py_to_run}\n")
    # subprocess.run(py_to_run)
    # print("\n============================================================\n\n")

    os.chdir(base_path)


finally:
    # Ensure the initial JAR process is terminated at the end of the iteration
    process.terminate()
    try:
        process.wait(timeout=5)
    except subprocess.TimeoutExpired:
        process.kill()
