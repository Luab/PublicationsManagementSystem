function start(){
    var audio = $("audio")[0];
    $("img").mouseenter(function() {
        audio.play();
    }).mouseleave(function() {
        audio.pause();
    });
}