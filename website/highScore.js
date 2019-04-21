var currentSeed = 0;

window.setInterval(function(){
    if(currentSeed >= 50)
        currentSeed = 0;
    else currentSeed++;

    var jsonScore = getHighScore(currentSeed);
    var score = JSON.parse(jsonScore);
    console.log(score.highScore);

    document.getElementById(currentSeed).innerHTML = score.highScore;

},1200);

function getHighScore(num){
    
    var n = num.toString();
    var obj = {
        seed : n
    };

    var jsonPayload = JSON.stringify(obj);
    console.log(jsonPayload);

    var xhr = new XMLHttpRequest();
    xhr.open("POST","http://iburch.pythonanywhere.com/gethighscore",false);
    xhr.setRequestHeader("Content-type","application/json; charset=UTF-8");

    try{
        xhr.send(jsonPayload);
        //console.log("responseText: "+ xhr.responseText);
        //var jsonObj = JSON.parse(xhr.resposeText);
        //console.log(jsonObj);
        return xhr.responseText;
    }

    catch(err){
        console.log(err);
    }

}
