from flask import Flask, send_from_directory, request, jsonify, url_for, make_response
import os

app = Flask(__name__)

# Set the directory path
directory = '/run/datad/facultad/tesis/em-thesis-utils/testServers/specs/'

db = {}

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
    
@app.route('/me', methods=['GET'])
def get_user():
    user_id = request.args.get('id', type=int)
    if user_id > 500:
        return 'Missing id parameter', 400
    return jsonify({"id": user_id}), 200

@app.route('/register', methods=['POST'])
def register_user():
    data = request.get_json()
    if data['id'] == '' or data['name'] == '':
        return 'Missing data', 403
    if user_id > 500:
        return 'Missing id or name in request body', 400
    if user_id < 100:
        return jsonify({'message': 'Id already existed'}), 200
    return data['name'], 201

@app.route('/login', methods=['POST'])
def post_login():
    data = request.get_json()
    headers = {}
    headers['location-empty'] = ''
    headers['location-valid'] = "http://location.com/arsrt"
    headers['location-invalid'] = "google.com"
    headers['location'] = "http://localhost:5000/test"
    if data['username'] == 'ph' and data['password'] == 'admin':
        return jsonify({'token': '123'}), 200, headers
        # return jsonify({'token': '123'}), 200
    # if request.headers['Authorization'] == '123':
    #     return jsonify({'ok': 'login ok'}), 200
    return 'Login error', 401, headers

@app.route('/get-cookies', methods=['GET'])
def get_cookies():
    cookies = request.cookies
    return jsonify({'cookies': cookies})

@app.route('/set-cookies', methods=['POST'])
def set_cookies():
    name = request.form.get('name')
    value = request.form.get('value')
    
    if not name or not value:
        return jsonify({'message': 'Name and value are required'}), 400
    
    response = make_response(jsonify({'message': 'Cookie set'}))
    response.set_cookie(name, value)
    return response

@app.route('/test', methods=['GET'])
def get_test():
    return jsonify({'email': 'foo@foo.foo'}), 200

@app.route('/foo', methods=['GET'])
def get_foo():
    return jsonify([{},{"id":"foo","properties":[{},{"name":"mapProperty1","type":"string","value":"one"},{"name":"mapProperty2","type":"string","value":"two"}],"empty":{}}]), 200

@app.route('/bar', methods=['GET'])
def get_bar():
    return jsonify({"p1":{},"p2":{"id":"foo","properties":[{},{"name":"mapProperty1","type":"string","value":"one"},{"name":"mapProperty2","type":"string","value":"two"}],"empty":{}}}), 200

@app.route('/empty', methods=['GET'])
def get_empty():
    return jsonify(), 200

@app.route('/create-resource', methods=['POST'])
def create_resource():
    data = request.get_json()
    resource_id = '123'  # Simulating a generated resource ID
    location_url = url_for('get_resource', resource_id=resource_id, _external=True)
    
    response = make_response(jsonify({
        'id': resource_id,
        'message': 'Resource created successfully'
    }), 201)
    
    response.headers['Location'] = location_url
    return response

@app.route('/resource/<resource_id>', methods=['GET'])
def get_resource(resource_id):
    # This is just a placeholder for the resource endpoint
    return jsonify({
        'id': resource_id,
        'name': 'exampleName',
        'value': 'exampleValue'
    })

@app.route('/api/bbdefault/<x>', methods=['GET'])
def cover_y(x):
    # This is just a placeholder for the resource endpoint
    if x == 'foo':
        response = make_response('OK', 200)
        response.mimetype = "text/plain"
        return response
    response = make_response('', 400)
    response.mimetype = "text/plain"
    return response


@app.route('/api/bbdefault', methods=['GET'])
def cover_x():
    if request.args:
        d = int(request.args.get("data"))
        # This is just a placeholder for the resource endpoint
        if d == 42:
            response = make_response('OK', 200)
            response.mimetype = "text/plain"
            return response
    response = make_response('', 400)
    response.mimetype = "text/plain"
    return response


@app.route('/api/bbdatapool/users', methods=['GET'])
def get_users():
    x = {"id": "userX123456", "name": "foo", "surname": "bar"}
    y = {"id": "userY777777", "name": "hello", "surname": "there"}
    z = {"id": "userZ984750", "name": "john", "surname": "smith"}
    data = [x, y, z]

    response = make_response(jsonify({"data": data, "error": None}), 200)
    return response



@app.route('/api/bbdatapool/users/<target_user>', methods=['GET'])
def get_specific_user(target_user):
    if target_user == "userY777777":
        y = {"id": "userY777777", "name": "hello", "surname": "there"}
        response = make_response(jsonify(y), 200)
        return response
    response = make_response('', 404)
    response.mimetype = "text/plain"
    return response

@app.route('/v3/api-docs', methods=['GET'])
def get_api_docs():
    response = make_response('', 200)
    response.mimetype = "text/plain"
    return response


if __name__ == '__main__':
    app.run(debug=True)
