from flask import Flask, request, jsonify
from graphene import ObjectType, String, Schema
from graphene.types.schema import GraphQLSchema
from graphql import graphql_sync
from graphql.type.schema import GraphQLSchema

class Query(ObjectType):
    hello = String(name=String(default_value="stranger"))

    def resolve_hello(self, info, name):
        return f'Hello {name}!'

schema = Schema(query=Query)

app = Flask(__name__)

@app.route('/graphql', methods=['POST'])
def graphql_endpoint():
    data = request.get_json()
    success, result = graphql_sync(schema.graphql_schema, data.get('query'), variable_values=data.get('variables'))
    status_code = 200 if success else 400
    return jsonify(result), status_code


if __name__ == '__main__':
    app.run(debug=True, port=5001)
