var imageDiv = [];

loadPhotos = function(images){
    //remove loaded images
    imageDiv.forEach(function(v){
        v.remove();
    });
    imageDiv=[];

    //show newly loaded images
    images.forEach(function(v,i){
        var div = parent.document.createElement('li');
        div.id = 'div' + i;
        div.innerHTML = "<figure><img src='" + v.url + "' alt='alt.png' /></figure>";
        parent.document.getElementById("photoGrid").appendChild(div);

        imageDiv.push(div);
    });
}

function search(str){
    var data = document.getElementById("search").value;

    $.ajax({
         url: '/search',
         type: 'post',
         dataType: 'json',
         data: String(data),
         async: false,
         contentType: 'application/json; charset=UTF-8',
         success: function (respond) {
           //var photos = JSON.parse(respond);
            loadPhotos(respond);
            alert("Load Success");
         },
         error: function(request,status,error){
             alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
         }
    });
}

///////////////////////////////////////////
$(window).load(function(){
    document.getElementById ("searchBtn").addEventListener ("click", search, false);

    $.ajax({
         url: '/get',
         type: 'get',
         dataType: 'json',
         async: false,
         contentType: 'application/json; charset=UTF-8',
         success: function (respond) {
           //var photos = JSON.parse(respond);
           loadPhotos(respond);
            alert("Load Success");
         },
         error: function(request,status,error){
             alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
         }
     });
});
