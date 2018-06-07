      var map;
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

//        //GeorssLayer
//        var georssLayer = new google.maps.KmlLayer({
//            url: 'http://www.uhoon.co.kr/test/1972/georss.xml'
//        });
//        georssLayer.setMap(map);
      }//--initMap();


		/**
		 * This is the getPhoto library
		 */
		function makeFacebookPhotoURL( id, accessToken ) {
			return 'https://graph.facebook.com/' + id + '/picture?access_token=' + accessToken;
		}
		function login( callback ) {
			FB.login(function(response) {
				if (response.authResponse) {
					console.log('Welcome!  Fetching your information.... ');
					if (callback) {
						callback(response);
					}
				} else {
					console.log('User cancelled login or did not fully authorize.');
				}
			},{scope: 'user_photos'} );
		}
		function getAlbums( callback ) {
			FB.api(
					'/me/albums',
					{fields: 'id,cover_photo'},
					function(albumResponse) {
						console.log( ' got albums ' );
						if (callback) {
							callback(albumResponse);
						}
					}
				);
		}
		function getPhotosForAlbumId( albumId, callback ) {
			FB.api(
					'/'+albumId+'/photos',
					{fields: 'id'},
					function(albumPhotosResponse) {
						console.log( ' got photos for album ' + albumId );
						if (callback) {
							callback( albumId, albumPhotosResponse );
						}
					}
				);
		}
		function getLikesForPhotoId( photoId, callback ) {
			FB.api(
					'/'+albumId+'/photos/'+photoId+'/likes',
					{},
					function(photoLikesResponse) {
						if (callback) {
							callback( photoId, photoLikesResponse );
						}
					}
				);
		}
		function getPlaceForPhotoId( photo, callback ) {
		    FB.api(
              '/'+photo.id+'/',
              'GET',
              {"fields":"name,place,created_time"},
              function(response) {
                  if(callback){
                    console.log('Saving photo infos : ' + photo.id);
                    callback( photo, response );
                  }
              }
            );
		}
		function getPhotos(callback) {
			var allPhotos = [];
			var accessToken = '';
			login(function(loginResponse) {
					accessToken = loginResponse.authResponse.accessToken || '';
					getAlbums(function(albumResponse) {
						var i, album, deferreds = {}, listOfDeferreds = [];


                        for (i = 0; i < albumResponse.data.length; i++) {
                            album = albumResponse.data[i];
                            deferreds[album.id] = $.Deferred();
                            listOfDeferreds.push( deferreds[album.id] );
                            getPhotosForAlbumId( album.id, function( albumId, albumPhotosResponse ) {
                                    var i, facebookPhoto;
                                    for (i = 0; i < albumPhotosResponse.data.length; i++) {
                                        facebookPhoto = albumPhotosResponse.data[i];
                                        deferreds[facebookPhoto.id] = $.Deferred();
                                        listOfDeferreds.push( deferreds[facebookPhoto.id] );
                                        getPlaceForPhotoId(facebookPhoto, function( photo, placeResponse) {
                                            allPhotos.push({
                                                'id'	:	photo.id,
                                                'added'	:	photo.created_time,
                                                'url'	:	makeFacebookPhotoURL( photo.id, accessToken ),
                                                'name'  :   placeResponse.name,
                                                'place' :   placeResponse.place,
                                                'time'  :   placeResponse.created_time
                                            });
                                            deferreds[photo.id].resolve();
                                        });
                                    }//--for()
                                    deferreds[albumId].resolve();
                                });
                        }//--for()

//							$.when.apply($, listOfDeferreds).then( function() {
//								if (callback) {
//									callback( allPhotos );
//								}
//							}, function( error ) {
//								if (callback) {
//									callback( allPhotos, error );
//								}
//							});
                    });//--getAlbums
			});
//TODO : this code temporarily sync functions with setTimeout()
setTimeout(function(){
				if (callback) {
                    callback( allPhotos );
                }
},2350);
		}//--getPhotos()

    function getUserPhotos(){
		/**
		 * This is the bootstrap / app script
		 */
		// wait for DOM and facebook auth
		var docReady = $.Deferred();
		var facebookReady = $.Deferred();
		$(document).ready(docReady.resolve);
		window.fbAsyncInit = function() {
			FB.init({
			  appId      : '381421658930345',
			  channelUrl : '//conor.lavos.local/channel.html',
			  status     : true,
			  cookie     : true,
			  xfbml      : true
			});
			facebookReady.resolve();
		};
		$.when(docReady, facebookReady).then(function() {
			if (typeof getPhotos !== 'undefined') {
				getPhotos( function( photos ) {
					console.log( photos );
					//TODO : use 'photos' to use photo
//document.getElementById("img1").src = photos[0].url;
//document.getElementById("p1").innerHTML = photos[0].place;


        // Create markers
        var infowindows = [];
        var markers = [];

        for (i = 0; i < photos.length; i++) {
            //must have 'place' and 'place.location'
            if(photos[i].place){
                if(photos[i].place.location){

                //Info Window Content
                var contentString = '<a href=' + photos[i].url + ' target="_blank"><IMG BORDER="0" ALIGN="Left" SRC = ' + photos[i].url + '></a>'+
                    '<div id="content">'+
                    '<div id="siteNotice">'+
                    '</div>'+
                    '<h1 id="firstHeading" class="firstHeading">' + photos[i].name + '</h1>'+
                    '<div id="bodyContent">'+
                    '<p><b>Time</b> : ' + photos[i].time + ' \n</p>'+
                    '<p><b>Id</b> : ' + photos[i].id + ' \n</p>'+
                    '</div>'+
                    '</div>';

                //set InfoWindow
                var infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });
                infowindows.push(infowindow);
                //infowindows[i]=infowindow;

                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(photos[i].place.location.latitude, photos[i].place.location.longitude),
                    map: map
                  });
                  markers.push(marker);
                  //markers[i] = marker;

                //Marker Click Event
                markerListener(marker,infowindow);

            }}//--if()if()
        }//--for()

        function markerListener(marker,infowindow){
            google.maps.event.addListener(marker,'click',function(){
                infowindow.open(map,marker);
            });
        }
        google.maps.event.addDomListener(window, 'load', initMap);


				});
			}
		});
		// call facebook script
		(function(d){
		 var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
		 js = d.createElement('script'); js.id = id; js.async = true;
		 js.src = "http://connect.facebook.net/en_US/all.js";
		 d.getElementsByTagName('head')[0].appendChild(js);
		}(document));
    }


    function loadPhotos() {
        var infowindows = [];
        var markers = [];
        var photos = ${'photos'};

        for (i = 0; i < photos.length; i++) {
            //must have 'place' and 'place.location'
            if(photos[i].lat != 999 && photos[i].lng != 999){
                //Info Window Content
                var contentString = '<a href=' + photos[i].url + ' target="_blank"><IMG BORDER="0" ALIGN="Left" SRC = ' + photos[i].url + '></a>'+
                    '<div id="content">'+
                    '<div id="siteNotice">'+
                    '</div>'+
                    '<h1 id="firstHeading" class="firstHeading">' + photos[i].name + '</h1>'+
                    '<div id="bodyContent">'+
                    '<p><b>Time</b> : ' + photos[i].time + ' \n</p>'+
                    '<p><b>Id</b> : ' + photos[i].id + ' \n</p>'+
                    '</div>'+
                    '</div>';

                //set InfoWindow
                var infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });
                infowindows.push(infowindow);
                //infowindows[i]=infowindow;

                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(photos[i].place.location.latitude, photos[i].place.location.longitude),
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
////////////////////////////////////////////////////////////////
//    getUserPhotos();
        loadPhotos();
