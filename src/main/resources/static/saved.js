loadPhotos = function(images){
    images.forEach(function(v,i){
        var div = parent.document.createElement('li');
        div.id = 'div' + i;
        div.innerHTML = "<figure><img src='" + v.url + "' alt='alt.png' /></figure>";
        parent.document.getElementById("photoGrid").appendChild(div);
    });
}
///////////////////////////////////////////
$(window).load(function(){
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
