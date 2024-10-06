from jsonrpclib.SimpleJSONRPCServer import SimpleJSONRPCServer

# Define math operations
def abs_val(x):
    """Returns the absolute value of a number."""
    return abs(x)

def add(x, y):
    """Adds two numbers."""
    return x + y

def subtract(x, y):
    """Subtracts y from x."""
    return x - y

def multiply(x, y):
    """Multiplies two numbers."""
    return x * y

# Create server
server = SimpleJSONRPCServer(('localhost', 9000))
print("JSON-RPC server listening on port 9000...")

# Register functions
server.register_function(abs_val, 'abs_val')
server.register_function(add, 'add')
server.register_function(subtract, 'subtract')
server.register_function(multiply, 'multiply')

# Start the server
server.serve_forever()
