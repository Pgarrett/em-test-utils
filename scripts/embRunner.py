import os
import subprocess
import signal
import time
import shutil


# java -jar cs/rest/original/features-service/target/features-service-sut.jar http://localhost:8080/swagger.json
# java -jar cs/rest/original/scout-api/api/target/scout-api-sut.jar
# java -jar cs/rest/original/proxyprint/target/proxyprint-sut.jar
# java -jar cs/rest/original/catwatch/catwatch-backend/target/catwatch-sut.jar http://localhost:8080/v2/api-docs
# java -jar cs/rest/artificial/ncs/target/rest-ncs-sut.jar http://localhost:8080/v2/api-docs
# java -jar cs/rest/artificial/scs/target/rest-scs-sut.jar http://localhost:8080/v2/api-docs
# java -jar cs/rest/artificial/news/target/rest-news-sut.jar http://localhost:8080/v2/api-docs
# java -jar cs/rest-gui/ocvn/web/target/ocvn-rest-sut.jar
# java -jar cs/rest/original/languagetool/languagetool-server/target/languagetool-sut.jar
# java -jar cs/rest/original/restcountries/target/restcountries-sut.jar
# java -jar cs/rest/original/session-service/target/session-service-sut.jar http://localhost:8080/v2/api-docs
# java -jar cs/rest-gui/gestaohospital/target/gestaohospital-rest-sut.jar
# java -jar cs/rest-gui/genome-nexus/web/target/genome-nexus-sut.jar
# java -jar cs/graphql/petclinic-graphql/target/petclinic-graphql-sut.jar http://localhost:9977/graphql
# java -jar cs/graphql/graphql-ncs/target/graphql-ncs-sut.jar http://localhost:8080/graphql
# java -jar cs/graphql/graphql-scs/target/graphql-scs-sut.jar http://localhost:8080/graphql

# Define the list of JAR files and corresponding names
bb_jars_and_names = [
    {"jar": "rest/original/features-service/target/features-service-sut.jar", "name": "features-service", "type": "rest", "targetUrl": "http://localhost:8080/swagger.json"},
    {"jar": "rest/original/catwatch/catwatch-backend/target/catwatch-sut.jar", "name": "catwatch", "type": "rest", "targetUrl": "http://localhost:8080/v2/api-docs"},
    {"jar": "rest/artificial/ncs/target/rest-ncs-sut.jar", "name": "rest-ncs", "type": "rest", "targetUrl": "http://localhost:8080/v2/api-docs"},
    {"jar": "rest/artificial/scs/target/rest-scs-sut.jar", "name": "rest-scs", "type": "rest", "targetUrl": "http://localhost:8080/v2/api-docs"},
    {"jar": "rest/artificial/news/target/rest-news-sut.jar", "name": "rest-news", "type": "rest", "targetUrl": "http://localhost:8080/v2/api-docs"},
    {"jar": "rest/original/session-service/target/session-service-sut.jar", "name": "session-service", "type": "rest", "targetUrl": "http://localhost:8080/v2/api-docs"},
    {"jar": "graphql/petclinic-graphql/target/petclinic-graphql-sut.jar", "name": "petclinic-graphql", "type": "graphql", "targetUrl": "http://localhost:9977/graphql"},
    {"jar": "graphql/graphql-ncs/target/graphql-ncs-sut.jar", "name": "graphql-ncs", "type": "graphql", "targetUrl": "http://localhost:8080/graphql"},
    {"jar": "graphql/graphql-scs/target/graphql-scs-sut.jar", "name": "graphql-scs", "type": "graphql", "targetUrl": "http://localhost:8080/graphql"}
]

technologies = ["python", "js", "java"]
output_formats = {"python": "PYTHON_UNITTEST", "js": "JS_JEST", "java": "JAVA_JUNIT_4"}

base_path = "../../"
emb_base_path = base_path + "EMB/jdk_8_maven/"
output_base_path = base_path + "em-thesis-utils/results"
em_jar = base_path + "EvoMaster/core/target/evomaster.jar"

testBb = True

if testBb:
    # Loop through each JAR and name in BB
    for entry in bb_jars_and_names:
        jar = entry["jar"]
        name = entry["name"]

        print(f"Testing BB: {name}")

        # Step 1: Run the JAR without inheriting its IO
        process = subprocess.Popen(["java", "-jar", emb_base_path + "cs/" + jar], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        time.sleep(10)
        
        try:
            # Step 2: Create directories
            dir_path = os.path.join(output_base_path, name + "/action/bb")                    
       
            problem_type_args = []
            if entry["type"] == "rest":
                problem_type_args = ["--bbSwaggerUrl", entry["targetUrl"]]
            else:
                problem_type_args = ["--problemType", "GRAPHQL", "--bbTargetUrl", entry["targetUrl"]]
            
            base_params = ["--blackBox", "true", "--maxTime", "10m", "--ratePerMinute", "60"] + problem_type_args

            for technology in technologies:
                target_directory = os.path.join(dir_path, technology)
                os.makedirs(target_directory, exist_ok=True)
                print(f"EvoMaster to generate: {output_formats[technology]}\n")
                execution_params = base_params + ["--outputFormat", output_formats[technology], "--outputFolder", target_directory, "--namingStrategy", "ACTION"]
                em_to_run = ["java", "-jar", em_jar] + execution_params
                print(f"Running: {em_to_run}")
                subprocess.run(em_to_run)
                print("\n============================================================\n\n")

            # Step 4: Run Python unittests
            # python_dir = os.path.join(dir_path, "python")
            # print("Executing python tests\n")
            # py_to_run = ["python3", "-m", "unittest", "discover", "-s", python_dir, "-p", "*_Test.py"]
            # print(f"Running: {py_to_run}\n")
            # subprocess.run(py_to_run)
            # print("\n============================================================\n\n")

            # # Step 5: Run npm tests
            # js_dir = os.path.join(dir_path, "js")
            # shutil.copy(os.path.join(runner_base_path, "package.json"), os.path.join(js_dir, "package.json"))
            # shutil.copy(os.path.join(runner_base_path, "package-lock.json"), os.path.join(js_dir, "package-lock.json"))
            # os.chdir(js_dir)
            # npm_ci = ["npm", "ci"]
            # subprocess.run(npm_ci)
            # js_to_run = ["npm", "test", "."]
            # print("Executing js tests\n")
            # print(f"Running: {js_to_run}\n")
            # subprocess.run(js_to_run)
            print("\n************************************************************")
            print("************************************************************\n\n")

            os.chdir(base_path)

        
        finally:
            # Ensure the initial JAR process is terminated at the end of the iteration
            process.terminate()
            try:
                process.wait(timeout=5)
            except subprocess.TimeoutExpired:
                process.kill()


wb_jars_and_names = [
    {"jar": "/grpc/ncs/target/rpc-grpc-ncs-evomaster-runner.jar", "name": "rpc-ncs", "type": "rpc", "sutJarPath": "rpc/grpc/artificial/grpc-ncs/target"},
    {"jar": "/grpc/scs/target/rpc-grpc-scs-evomaster-runner.jar", "name": "rpc-scs", "type": "rpc", "sutJarPath": "rpc/grpc/artificial/grpc-scs/target"},
    {"jar": "/thrift/ncs/target/rpc-thrift-ncs-evomaster-runner.jar", "name": "thrift-ncs", "type": "rpc", "sutJarPath": "rpc/thrift/artificial/thrift-ncs/target"},
    {"jar": "/thrift/scs/target/rpc-thrift-scs-evomaster-runner.jar", "name": "thrift-scs", "type": "rpc", "sutJarPath": "rpc/thrift/artificial/thrift-scs/target"},
    # {"jar": "/rest/features-service/target/features-service-evomaster-runner.jar", "name": "features-service", "type": "rest", "sutJarPath": "rest/original/features-service/target"},
    # {"jar": "/rest/catwatch/target/catwatch-evomaster-runner.jar", "name": "catwatch", "type": "rest", "sutJarPath": "rest/original/catwatch/catwatch-backend/target"},
    # {"jar": "/rest/ncs/target/rest-ncs-evomaster-runner.jar", "name": "rest-ncs", "type": "rest", "sutJarPath": "rest/artificial/ncs/target"},
    # {"jar": "/rest/scs/target/rest-scs-evomaster-runner.jar", "name": "rest-scs", "type": "rest", "sutJarPath": "rest/artificial/scs/target"},
    # {"jar": "/rest/news/target/rest-news-evomaster-runner.jar", "name": "rest-news", "type": "rest", "sutJarPath": "rest/artificial/news/target"},
    # {"jar": "/rest/session-service/target/session-service-evomaster-runner.jar", "name": "session-service", "type": "rest", "sutJarPath": "rest/original/session-service/target"},
    # {"jar": "/graphql/petclinic-graphql/target/petclinic-graphql-evomaster-runner.jar", "name": "petclinic-graphql", "type": "graphql", "sutJarPath": "graphql/petclinic-graphql/target"},
    # {"jar": "/graphql/graphql-ncs/target/graphql-ncs-evomaster-runner.jar", "name": "graphql-ncs", "type": "graphql", "sutJarPath": "graphql/graphql-ncs/target"},
    # {"jar": "/graphql/graphql-scs/target/graphql-scs-evomaster-runner.jar", "name": "graphql-scs", "type": "graphql", "sutJarPath": "graphql/graphql-scs/target"},
]

# test whitebox
for entry in wb_jars_and_names:
    jar = entry["jar"]
    name = entry["name"]

    print(f"Testing WB: {name}")

    controller_args = ["java", "-jar", emb_base_path + "em/external" + jar, "40100", "8090", emb_base_path + "cs/" + entry["sutJarPath"]]
    print(f"Executing controller: {controller_args}")

    # Step 1: Run the JAR without inheriting its IO
    process = subprocess.Popen(controller_args, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    time.sleep(10)
    
    try:
        # Step 2: Create directories
        dir_path = os.path.join(output_base_path, name + "/action/wb")        
        
        problem_type_args = []
        if entry["type"] == "rest":
            problem_type_args = ["--problemType", "REST"]
        elif entry["type"] == "graphql":
            problem_type_args = ["--problemType", "GRAPHQL"]
        else:
            problem_type_args = ["--problemType", "RPC"]
        
        base_params = ["--maxTime", "10m"] + problem_type_args

        target_directory = os.path.join(dir_path, "java")
        os.makedirs(target_directory, exist_ok=True)
        print(f"EvoMaster to generate: JAVA_JUNIT_4\n")
        execution_params = base_params + ["--outputFormat", "JAVA_JUNIT_4", "--outputFolder", target_directory, "--namingStrategy", "ACTION"]
        em_to_run = ["java", "-jar", em_jar] + execution_params
        print(f"Running: {em_to_run}")
        subprocess.run(em_to_run)
        print("\n============================================================\n\n")

        print("\n************************************************************")
        print("************************************************************\n\n")

        os.chdir(base_path)

    
    finally:
        # Ensure the initial JAR process is terminated at the end of the iteration
        process.terminate()
        try:
            process.wait(timeout=5)
        except subprocess.TimeoutExpired:
            process.kill()
