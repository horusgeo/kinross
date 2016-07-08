myMap = L.map('mapDiv',{}).setView([-19.315, -43.636], 15);
//myMap = L.map('mapDiv',{});

L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
}).addTo(myMap);


myMap.attributionControl.setPosition('bottomleft');
L.control.scale().addTo(myMap);

/* *********************** LOCATE *********************** */

function onLocationFound(e) {
//    var radius = e.accuracy / 2;

    if(poseMarker) {
        myMap.removeLayer(poseMarker);
    }

    poseMarker = L.marker(e.latlng, {icon: iconPosePin}).addTo(myMap);
//           .bindPopup("You are within " + radius + " meters from this point").openPopup();

//    L.circle(e.latlng, radius).addTo(myMap);

    if(locateFlag==1){
        myMap.setView(poseMarker.getLatLng(), 18);
    }
}

function onLocationError(e) {
    alert(e.message);
}

myMap.on('locationfound', onLocationFound);
myMap.on('locationerror', onLocationError);

myMap.locate({watch: true, enableHighAccuracy: true});

function findLocation(){
    myMap.setView(poseMarker.getLatLng(), 18);
    locateFlag = 1;

    function stopLocate(){
        console.log("locateFlag");
        locateFlag=0;
        myMap.off('click', stopLocate);
        myMap.off('dragstart', stopLocate);
    }

    myMap.on('click', stopLocate);
    myMap.on('dragstart', stopLocate);

}


/* *********************** Load IMGS *********************** */

function loadImg(imgPath){

    console.log(imgPath);

    var options = {
                minZoom: 0,
                maxZoom: 30,
                opacity: 1.0,
                tms: false
    };

    var path = imgPath + '/{z}/{x}/{y}.png';

    console.log(path);

    L.tileLayer(path, options).addTo(myMap);
    //L.tileLayer('{z}/{x}/{y}.png', options).addTo(myMap);

}
/* *********************** Load KML *********************** */

var runLayer = omnivore.kml('./kml/fd.kml')
//    .on('ready', function() {
//         myMap.fitBounds(runLayer.getBounds());
//    })
    .addTo(myMap);



/* *********************** Click Points *********************** */

function startPoints(){
    centerMarker = L.marker(myMap.getCenter(), {icon: iconBlackPin})
                        .addTo(myMap)
                        .bindPopup(myMap.getCenter().toString())
                        .openPopup();

    myMap.on('move', function (e) {
		centerMarker.setLatLng(myMap.getCenter())
		            .bindPopup(myMap.getCenter().toString()).openPopup();
	});
}

function clickPoints(){
    clickPointsArray.push(centerMarker.getLatLng());
    propertyPolygon.addLatLng(centerMarker.getLatLng()).addTo(myMap);
}

function clearPoints(){
    clickPointsArray = [];
    propertyPolygon.setLatLngs([]);
    myMap.removeLayer(centerMarker);
    myMap.removeLayer(reguaPolygon);
    myMap.off('move', null);
}

function createProperty(id, nome, tipo){

    var p = L.polygon(propertyPolygon.getLatLngs(),{});

    console.log(id);

    if(tipo==0)
        p.color = "blue";
    if(tipo==1)
        p.color = "green";
    if(tipo==2)
        p.color = "yellow";
    if(tipo==3)
        p.color = "red";

    console.log(id);
    p.bindPopup(nome);

    console.log(id);
    var aux = new Prop(p, id, tipo, nome);
    properties.push(aux);
    aux.getPoly().addTo(myMap);
    clearPoints();
    callBackProperty(aux.getPoly().getLatLngs());
}

/* *********************** Pin *********************** */

function setPin(Text){
    pinMarker = L.marker({}, {icon: iconYellowPin});
    function onMapClick(e) {
        pinMarker.setLatLng(e.latlng)
        myMap.removeLayer(pinMarker);
        pinMarker.addTo(myMap)
                 .bindPopup(generatePinText(Text, e.latlng))
                 .openPopup();
    }
    myMap.on('click', onMapClick);
}

function keepPin(){

    var aux = L.marker(pinMarker.getLatLng(), {icon: iconYellowPin})
                .addTo(myMap)
                .bindPopup(pinMarker.getPopup().getContent())
                .openPopup();

    pins.push(aux);
    cancelPin();
    callBackPin(aux);
}

function cancelPin(){
    myMap.removeLayer(pinMarker);
    myMap.off('click', null);
}

/* *********************** Regua *********************** */

function clickRegua(){
    popup = L.popup({closeButton: false});

    clickReguaArray = [];
    reguaPolygon.setLatLngs([]);

    function onMapClick(e) {

        if(clickReguaArray.length == 2){
            clickReguaArray.pop();
            clickReguaArray.push(e.latlng);
            var latlng = reguaPolygon.getLatLngs();
            latlng.pop();
            latlng.push(e.latlng);
            reguaPolygon.setLatLngs(latlng);
            popup
                .setLatLng(e.latlng)
                .openOn(myMap)
                .setContent(calcula_dist());
        }else{
            clickReguaArray.push(e.latlng);

            latlngBck = e.latlng;

            if(clickReguaArray.length==1){
                reguaPolygon.addLatLng(e.latlng).addTo(myMap);
            }else if(clickReguaArray.length==2){
                popup
                    .setLatLng(e.latlng)
                    .openOn(myMap)
                    .setContent(calcula_dist());
                reguaPolygon.addLatLng(e.latlng).addTo(myMap);
            }
        }
    }

    myMap.on('click', onMapClick);
}

function closeRegua(){
    clickReguaArray = [];
    reguaPolygon.setLatLngs([]);
    myMap.off('click', null);
    myMap.removeLayer(reguaPolygon);
    myMap.closePopup(popup);
}

/* *********************** CallBacks to Android *********************** */
function callBackProperty(prop){
    var lats = [];
    var lngs = [];
    for(i = 0; i < prop.length; i++){
        lats.push(prop[i].lat);
        lngs.push(prop[i].lng);
    }
    Android.callBackPropriedade(lats, lngs);
}

function callBackPin(pin){
    var l = pin.getLatLng();
    Android.callBackPins(l.lat, l.lng, pin.getPopup().getContent());
}

/* *********************** Populate Map *********************** */

function populatePin(texto, lat, lng){
    var aux = L.marker([lat,lng], {icon: iconYellowPin})
                    .addTo(myMap)
                    .bindPopup(texto)
                    .openPopup();

        pins.push(aux);
        aux.addTo(myMap);
}


function newProp(id, lat, lng, nome, tipo){

    var latlng = [];
    latlng.push(L.latLng(lat,lng));
    var p;

    if(tipo==0)
        p = L.polygon(latlng, {color:"blue"});
    if(tipo==1)
        p = L.polygon(latlng, {color:"green"});
    if(tipo==2)
        p = L.polygon(latlng, {color:"yellow"});
    if(tipo==3)
        p = L.polygon(latlng, {color:"red"});

    var aux = new Prop(p, id, tipo, nome);
    properties.push(aux);

}

function continueProp(lat, lng){

    var latlng = L.latLng(lat,lng);
    properties[properties.length-1].getPoly().addLatLng(latlng);

}

function addProp(){

    for (i=0;i<properties.length;i++){
        properties[i].getPoly().addTo(myMap);
    }

}