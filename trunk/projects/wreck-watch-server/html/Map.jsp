<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Wreck Watch Map</title>
<script src="http://code.jquery.com/jquery-latest.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="http://jquery.offput.ca/js/jquery.timers.js"></script>

<script
	src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAwUmhtXq7l-pmd0VaUhQuOBTRzhAzSQ3u4E7LYXha2CwyHvWJ_RRIk9eLyEB9DTAVD0x0VYHWvAzg2A"
	type="text/javascript"></script>
	

	

<script type="text/javascript">

	var map;
	var waypoints = new Array();
	var routes = new Array();
	var showWrecks = true;
	var showRoutes = true;
	
    var accidentCallback = function (data, textStatus){
        console.log(data);
    	$.each(data.list.wreck, processAccidentItem);
    }
    var routeCallback = function (data, textStatus){
        console.log(data);
        
    	$.each(data.list.route, processRouteItem);
    }

    function processRouteItem(i, item){

        var temp = new Route();
        temp.setId(item.id__);
        var skip = false;
        $.each(routes, function(j, item2){
            if (item2.equals(temp)){
                skip = true;
            }
        });
        if (!skip){
            
        	temp.setMarker(GPolyline.fromEncoded({
           		color: "#"+genHex(),
            	weight: 10,
            	points: item.encodedRoute__,
            	levels: item.encodedLevels__,
            	zoomFactor: 32,
            	numLevels: 4
            }));
        	map.addOverlay(temp.marker);
        	routes.push(temp); 
        } 
        
    }


    function processAccidentItem(i, item){
		var temp = new Waypoint();
		temp.setLat(item.location__.latitude__);
		temp.setLon(item.location__.longitude__);
		temp.setTime(item.location__.timeStamp__);
		temp.setId(item.id__);
		temp.setMarker(new GMarker(new GLatLng(item.location__.latitude__, item.location__.longitude__)));
		var skip = false;
        $.each(waypoints, function(j, item2){
            if (item2.equals(temp)){
				skip = true;
            }
            
        });

        if (!skip){
         	waypoints.push(temp);
         	if (showWrecks){
            	map.addOverlay(temp.marker);
         	}
        }
    }
    
    $(document).ready(function(){
    	 if (GBrowserIsCompatible()) {
             map = new GMap2(document.getElementById("map_canvas"));
             map.enableContinuousZoom();
             map.enableScrollWheelZoom();
             map.addControl(new GSmallMapControl());
             map.addControl(new GMapTypeControl());
             map.setCenter(new GLatLng(36.14951909060777, -86.79576873779297), 13);
             $("#map_canvas").everyTime(10000, getAccidentData);
             $("#map_canvas").everyTime(10000, getRouteData);

             $("#hideWrecks").click(function(){
                 if (showWrecks){
                 	$.each(waypoints, function(i, item){
                   		map.removeOverlay(item.marker);
                 	});
                 	showWrecks = false;
                 	this.value = "Show Wrecks";
                 }else{
                     $.each(waypoints, function(i, item){
						map.addOverlay(item.marker);
                         }	);
                     showWrecks = true;
                     this.value = "Hide Wrecks";
                 }
                 
             
           });

             $("#hideRoutes").click(function(){
                 if (showRoutes){
                     $.each(routes, function(i, item){
						map.removeOverlay(item.marker);
                         });
                     showRoutes = false;
                     this.value = "Show Routes";
                 }else{
                     $.each(routes, function(i, item){
						map.addOverlay(item.marker);
                         });
                     showRoutes = true;
                     this.value = "Hide Routes";
                 }
                     
                 });
             
    	 }
    });

    function getAccidentData(){
        
        var bnds = map.getBounds();
        var qry = new String("http://localhost:80/wreckwatch/map/?type=locationrequest&nelat="+bnds.getNorthEast().lat()+"&swlat="+bnds.getSouthWest().lat()+
        "&nelon="+bnds.getNorthEast().lng()+"&swlon="+bnds.getSouthWest().lng()+"&callback=?");
    	$.getJSON(qry, accidentCallback);
    }

    function getRouteData(){
    	var bnds = map.getBounds();
        var qry = new String("http://localhost:80/wreckwatch/map/?type=routerequest&nelat="+bnds.getNorthEast().lat()+"&swlat="+bnds.getSouthWest().lat()+
        "&nelon="+bnds.getNorthEast().lng()+"&swlon="+bnds.getSouthWest().lng()+"&callback=?");
    	$.getJSON(qry, routeCallback);
    }

    function genHex(){
    	colors = new Array("0000ff", "00ff00", "ff0000", "ff00ff", "00ffff");
    	digit = new Array(5)
    	return colors[Math.round(Math.random()*5)];
    }
    function Waypoint(){

    	this.setLat = function (lat){
    		this.lat=lat;
    	}
    	this.setLon = function(lon){
    		this.lon = lon;
    	}
    	this.setTime = function(time){
    		this.wrecktime = time;
    	}
    	this.setId = function(id){
    		this.wreckid = id;
    	}
    	this.setMarker = function(marker){
        	this.marker = marker;
    	}
    	this.equals = function(wreck){
        	return (this.wreckid == wreck.wreckid);  
    	}
    }

    function Route(){
        this.setMarker = function(marker){
            this.marker = marker;
        }
        this.setId = function(id){
            this.wreckid = id;
        }
        this.equals = function(route){
            return (this.wreckid == route.wreckid);
        }
    }


    


      </script>
</head>
<body onunload="GUnload()" style="font-family: Arial; border: 0 none;">
<div id="control">
<input type="button" id="hideWrecks" value="Hide Wrecks" />
&nbsp;
&nbsp;
<input type="button" id="hideRoutes" value="Hide Routes" />
</div>
<div id="map_canvas" style="width: 1000px; height: 700px"></div>
</body>
</html>
