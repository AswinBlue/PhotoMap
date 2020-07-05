      var map;
      var infowindows = [];
      var markers = [];

      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: 37.565288, lng: 126.989481},
          zoom: 5
        });

        //set base marker icon
        var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';

        //Map Click event Listener
        google.maps.event.addListener(map, 'click', function(event) {
            //TODO : functions when you click map
        });
      }


    function loadPhotos(photos) {
//        var photos = JSON.parse( ${photos} );
        //clear markers on map
        markers.forEach(function(v){
            v.setMap(null);
        });

        infowindows = [];
        markers = [];
        for (i = 0; i < photos.length; i++) {
            //must have 'place' and 'place.location'
            if(photos[i].lat != 999 && photos[i].lng != 999){
                //Info Window Content
                var contentString = "";
                if(photos[i].from == "Facebook") {
                    contentString += '<a href=' + photos[i].url + ' target="_blank"><IMG BORDER="0" ALIGN="Left" SRC = ' + photos[i].url + '></a>';
                }
                else if(photos[i].from == "Flickr") {
                    contentString += '<a href=' + photos[i].postURL + ' target="_blank"><IMG BORDER="0" ALIGN="Left" SRC = ' + photos[i].url + '></a>';
                }
                contentString +=  '<div id="content">'+
                    '<div id="siteNotice">'+
                    '</div>'+
                    '<h1 id="firstHeading" class="firstHeading">' + photos[i].title + '</h1>'+
                    '<div id="bodyContent">'+
                    '<p><b>Time</b> : ' + photos[i].time + ' \n</p>'+
                    '<p><b>Content</b> : ' + photos[i].content + ' \n</p>'+
                    '<p><b>place</b> : ' + photos[i].place + ' \n</p>'+
                    '<p><b>photos from </b> : ' + photos[i].from + ' \n</p>'+
                    '</div>'+
                    '</div>';

                //set InfoWindow
                var infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });
                infowindow.setZIndex(1000);
                infowindows.push(infowindow);
                //infowindows[i]=infowindow;

                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(photos[i].lat, photos[i].lng),
                    map: map
                  });
                  markers.push(marker);
                  //markers[i] = marker;

                //Marker Click Event
                markerListener(marker,infowindow);
            }
        }//--for()

        function markerListener(marker,infowindow){
            google.maps.event.addListener(marker,'click',function(){
                infowindow.open(map,marker);
            });
        }
        //google.maps.event.addDomListener(window, 'load', initMap);
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
////////////////////////////////////////////////////////////////
//    getUserPhotos();
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
