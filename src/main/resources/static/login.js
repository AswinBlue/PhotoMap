var allPhotos = null;

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
                    console.log('Loading photo infos : ' + photo.id);
                    callback( photo, response );
                  }
              }
            );
		}
		function getPhotos(callback) {
		    allPhotos = Array();
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
                                    var obj = new Object();
                                    obj.URL = makeFacebookPhotoURL( photo.id, accessToken );
                                    obj.time = placeResponse.created_time;
                                    obj.from = "Facebook"
                                    obj.title = placeResponse.name;
                                    if(placeResponse.place){
                                        obj.place = placeResponse.place.name;
                                        if(placeResponse.place.location){
                                            obj.lat = placeResponse.place.location.latitude;
                                            obj.lng = placeResponse.place.location.longitude;
                                        }
                                    }
                                    else{
                                        obj.place = "Not Specified";
                                        obj.lat = "999";
                                        obj.lng = "999";
                                    }
                                    //var jparam = JSON.stringify(obj);
                                    allPhotos.push(obj);

                                    deferreds[photo.id].resolve();
							    });
							}//--for()
							deferreds[albumId].resolve();
						});
					}//--for()
                });//--getAlbums
			});

			//TODO : this code temporarily sync functions with setTimeout()
            setTimeout(function(){
            				if (callback) {
                                callback( allPhotos );
                            }
            },2350);
		}

        function getUserPhotos(){
                // wait for DOM and facebook auth
                var docReady = $.Deferred();
                var facebookReady = $.Deferred();
                $(document).ready(docReady.resolve);
                window.fbAsyncInit = function() {
                    FB.init({
                      appId      : '381421658930345',
                      status     : true,
                      cookie     : true,
                      xfbml      : true
                    });
                    facebookReady.resolve();
                };
                $.when(docReady, facebookReady).then(function() {
                    if (typeof getPhotos !== 'undefined') {
                        getPhotos(function(allPhotos_){
                            var jparam = JSON.stringify(allPhotos_);
                            console.log(jparam);
                             $.ajax({
                                 url: '/load/FB',
                                 type: 'post',
                                 dataType: 'json',
                                 data: jparam,
                                 async: false,
                                 contentType: 'application/json; charset=UTF-8',
                                 success: function (respond) {
                                    if(respond.result > 0)
                                        alert("Photo Loaded Successfully");
                                    else
                                        alert("Empty data Loaded");
                                        return true;
                                 },
                                 error: function(request,status,error){
                                     alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                                     return false;
                                 }
                             });
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

///////////////////////////////////////////////////////////////////////////////////////////
/*
var JsonData = Array();
JsonData.push({"URL":"https://graph.facebook.com/207335319381819/picture?access_token=EAAFa5qZA9fKkBAKpizhi3SJNZAlh0gVeYuJckMXMvK3jec2nSfHZAhCMknu9SHGqmJibM9ibrKzgsSjPjUDRqnhmfxaVn8Ew5d5g0AUErO55SPpAmGAhZCslqqZAG0WhjZCZAzwY92dBEZCbIJdLixjSK5XJuey4lMskHcwyFHpTkAZDZD","time":"2012-04-18T15:10:39+0000","from":"facebook","lat":"999","lng":"999"});
JsonData.push({"URL":"https://graph.facebook.com/950274218421255/picture?access_token=EAAFa5qZA9fKkBAFlxl2PtAxzgVUKoUiZAZANGlQv5EHFx4UaiSgZAqmmNM7Nf66iU6jb1Ed10aPkJYfZBZBi4XBQTMfh54z9kIHhDCmxtSjuWVkkpZA7tWgigxJ8sXxyZBe5NrPEYzhGrdeJdmntVqy0zJvjKw0JB0PNpjXnRdIDmwZDZD","time":"2016-02-12T09:39:28+0000","from":"facebook","lat":"999","lng":"999"});
JsonData = JSON.stringify(JsonData);
alert(JsonData);

$.ajax({
     url: 'load',
     type: 'post',
     dataType: 'json',
     data: JsonData,
     async: false,
     contentType: 'application/json; charset=UTF-8',
     success: function (respond) {
        if(respond.result > 0)
            alert("Photo Loaded Successfully");
        else
            alert("Empty data Loaded");

         return true;
     },
     error: function(request,status,error){
         alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
         return false;
     }
 });
*/
///////////////////////////////////////////////////////////////////////////////

getFlickrPhotos = function(){
   var xhr = new XMLHttpRequest();
   xhr.open('POST', '/load/Flickr', true);
   xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
   xhr.onload = function () {
       // do something to response
       alert(this.responseText);
   };
   xhr.error = function(request,status,error){
        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
    xhr.send(document.getElementById("token").value);
      //xhr.send('user=person&pwd=password&organization=place&requiredkey=key');
}

///////////////////////////////////////////////////////////////////////////////
window.onload = function(){
    document.getElementById ("fb_login").addEventListener ("click", getUserPhotos, false);
    document.getElementById ("fk_login").addEventListener ("click", getFlickrPhotos, false);

}