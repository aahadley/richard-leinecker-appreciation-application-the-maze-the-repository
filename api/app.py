from flask import Flask, request, jsonify
#import pymysql as sql
#from flask_restful import Api, Resource, reqparse

app = Flask(__name__)

'''
db = sql.connect( "db.cm0a6cdoupkr.us-east-1.rds.amazonaws.com" , "masterUsername", "masterPassword")
cursor = db.cursor()
cursor.execute("SELECT VERSION()")
data = cursor.fetchone()
'''


q = []
SUCCESS = {"success" : True}
FAILURE = {"success" : False}


@app.route("/submitmove", methods=["POST"])
def submit_move():
    '''Add a move to the queue.'''

    try:
        move = request.json["move"]
        q.append(move)

    except:
        return jsonify(FAILURE)

    print(q)
    return jsonify(SUCCESS)


@app.route("/getmoves", methods=["POST"])
def get_moves():
    '''Dequeue the first n moves. defaults to 1'''

    with open("key.txt", 'r') as f:
        if request.json["auth-key"] != f.readline().strip():
            return jsonify({"success" : "authentication error"})


    try:
        n = request.json["num_moves"]

    except KeyError:
        n = 10


    try:
        moves = q[:min(n, len(q))]
        del q[:min(n, len(q))]

    except:
        return jsonify(FAILURE)    

    return jsonify({"moves" : moves})


@app.route("/__clearmoves__", methods=["POST"])
def __clearmoves__():
    '''Empty the queue of all moves'''

    with open("key.txt", 'r') as f:
        if request.json["auth-key"] != f.readline().strip():
            return jsonify({"success" : "authentication error"})

    q = []
    return jsonify(SUCCESS)


@app.route("/gethighscore", methods=["POST"])
def get_high_score():
    '''return the high score for a given seed'''

    return jsonify(FAILURE) #unitl the query is inserted

    try:
        seed = request.json["seed"]
        query = ""
        cursor.execute(query)
        db.commit()

    except:
        return jsonify(FAILURE)


    return jsonify(SUCCESS)

@app.route("/newhighscore", methods=["POST"])
def new_high_score():

    return jsonify(FAILURE) # until the query is inserted

    try:
        new_score = request.json["score"]
        seed      = request.json["seed" ]

        query = ""

        cursor.execute(query)
        db.commit()

    except:
        return jsonify(FAILURE)    


    return jsonify(SUCCESS)

if __name__ == "__main__":
    app.run(debug = True)
