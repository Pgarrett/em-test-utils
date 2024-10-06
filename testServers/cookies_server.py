from flask import Flask, request, make_response
import datetime

app = Flask(__name__)

@app.route('/login', methods=['POST'])
def login():
    response = make_response("Logged in")
    max_age = 7 * 24 * 60 * 60  # 7 days in seconds
    expires = datetime.datetime.utcnow() + datetime.timedelta(seconds=max_age)
    response.set_cookie('username', 'foo', max_age=max_age, expires=expires, secure=True, httponly=True)
    return response

@app.route('/check', methods=['GET'])
def check():
    username = request.cookies.get('username')
    if username == 'foo':
        response = make_response('this:foo', 200)
        response.mimetype = "text/plain"
        return response
    response = make_response('', 401)
    response.mimetype = "text/plain"
    return response

if __name__ == '__main__':
    app.run(debug=True)
