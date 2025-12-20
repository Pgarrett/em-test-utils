from flask import Flask, request, jsonify, send_from_directory
import os

app = Flask(__name__)

# Set the directory path
directory = '/run/datad/evoMaster/em-test-utils/testServers/specs/'

@app.route('/')
def index():
    return 'File Server is running'

@app.route('/<path:filename>')
def download_file(filename):
    # Check if the file exists in the directory
    if os.path.exists(os.path.join(directory, filename)):
        # Send the file to the client
        return send_from_directory(directory, filename)
    else:
        return f'File {filename} not found', 404

@app.route("/object", methods=["POST"])
def handle_object():
    data = request.get_json()
    print(f"Received: {data}")

    return jsonify({"message": "Received object", "data": data}), 200

@app.route("/array", methods=["POST"])
def handle_array():
    data = request.get_json()
    print(f"Received: {data}")

    return jsonify({"message": "Received array", "data": data}), 200

@app.route("/string", methods=["POST"])
def handle_string():
    data = request.data.decode("utf-8")
    return jsonify({"message": "Received string", "data": data}), 200

@app.route("/enum-examples", methods=["POST"])
def handle_enum_examples():
    data = request.get_json()
    print(f"Received: {data}")

    return jsonify({"message": "Received enum examples", "data": data}), 200

@app.route("/users", methods=["POST"])
def handle_users():
    data = request.get_json()
    print(f"Received: {data}")

    return jsonify({"message": "Received enum examples", "data": data}), 200

@app.route('/login', methods=['POST'])
def post_login():
    data = request.get_json()
    
    if data['username'] == 'ph' and data['password'] == 'admin':
        return jsonify({'token': '123'}), 200
    return 'Login error', 401

if __name__ == "__main__":
    app.run(debug=True)

# java -jar /run/datad/evoMaster/EvoMaster/core/target/evomaster.jar --blackBox true --maxTime 1m --ratePerMinute 60 --bbSwaggerUrl http://localhost:5000/dtoSpec.yaml --outputFormat JAVA_JUNIT_4 --outputFolder /run/datad/evoMaster/em-test-utils/results/dataset-dto/dto-server/src/test/java --dtoForRequestPayload true