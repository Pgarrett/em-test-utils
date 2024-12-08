from flask import Flask, send_from_directory, request, jsonify, url_for, make_response
import random
import os

app = Flask(__name__)

# Set the directory path
directory = '/run/datad/facultad/tesis/em-thesis-utils/liveDemo/server'

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

@app.route('/number/random', methods=['GET'])
def random_number():
    random_num = random.random()
    return jsonify({"random_number": random_num})

@app.route('/number/next', methods=['GET'])
def next_number():
    n = request.args.get('n', type=int)
    next_num = n + 1 if n is not None else 1
    return jsonify({"next_number": next_num})

@app.route('/echo', methods=['GET'])
def echo():
    text = request.args.get('text')
    if not text:
        response = "no message"
    else:
        words = text.split()
        response = words if len(words) > 1 else words[0]
    return jsonify({"message": response})

if __name__ == '__main__':
    app.run(debug=True)
