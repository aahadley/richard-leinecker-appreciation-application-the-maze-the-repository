from flask import Flask, request, jsonify
import pymysql as sql
import flask_cors
from flask_restful import Api, Resource, reqparse

app = Flask(__name__)

db = sql.connect( "db.cg1ciqpxsnvy.us-east-1.rds.amazonaws.com" , "masterUsername", "masterPassword")
cursor = db.cursor()
cursor.execute("SELECT VERSION()")
data = cursor.fetchone()


q = []
SUCCESS = {"success" : True}
FAILURE = {"success" : False}

@app.route('/')
@flask_cors.cross_origin()
def hello_world():
    return 'Hello from Flask!'

@app.route("/submitmove", methods=["POST"])
@flask_cors.cross_origin()
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
@flask_cors.cross_origin()
def get_moves():
    '''Dequeue the first n moves. defaults to 1'''

    with open("key.txt", 'r') as f:
        if request.json["key"] != f.readline().strip():
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
@flask_cors.cross_origin()
def __clearmoves__():
    '''Empty the queue of all moves'''

    with open("key.txt", 'r') as f:
        if request.json["key"] != f.readline().strip():
            return jsonify({"success" : "authentication error"})

    q = []
    return jsonify(SUCCESS)


@app.route("/gethighscore", methods=["POST"])
@flask_cors.cross_origin()
def get_high_score():
    '''return the high score for a given seed'''

    #return jsonify(FAILURE) #unitl the query is inserted

    try:
        seed = request.json["seed"]
        query = "SELECT highScore FROM mazeDB.mazes WHERE seed = \'"+seed+"\'"
        cursor.execute(query)
        data = cursor.fetchall()

        db.commit()

    except:
        return jsonify(FAILURE)

    return jsonify({"highScore" : data[0][0]})

@app.route("/newhighscore", methods=["POST"])
@flask_cors.cross_origin()
def new_high_score():

    #return jsonify(FAILURE) # until the query is inserted

    try:
        new_score = request.json["score"]
        
        if new_score != "undefined":
            new_score = str(list(new_score)[:min(5, len(new_score))])

        seed      = request.json["seed"]

        #UPDATE `mazeDB`.`mazes` SET `highScore` = '2147483646' WHERE (`seed` = '43');
        query = "UPDATE mazeDB.mazes SET highScore = %s WHERE seed = %s"
        val = (new_score,seed)

        cursor.execute(query,val)
        db.commit()

    except:
        return jsonify(FAILURE)


    return jsonify(SUCCESS)

if __name__ == "__main__":
    app.run(debug = True)