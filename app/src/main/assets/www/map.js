myMap = L.map('mapDiv',{}).setView([-19.315, -43.636], 15);
//myMap = L.map('mapDiv',{});

var options = {
                minZoom: 0,
                maxZoom: 21,
                opacity: 1.0,
                tms: false
                };

L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
}).addTo(myMap);

//L.tileLayer('{z}/{x}/{y}.png', options).addTo(myMap);

myMap.attributionControl.setPosition('bottomleft');

/* *********************** Load KML *********************** */

var runLayer = omnivore.kml('./kml/fd.kml')
    .on('ready', function() {
         myMap.fitBounds(runLayer.getBounds());
    })
    .addTo(myMap);
L.control.scale().addTo(myMap);


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
    ;

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
                 .bindPopup(Text + '<br>' + e.latlng.toString() + '</br>')
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
                .setContent(calcula_dist() + " metros");
        }else{
            clickReguaArray.push(e.latlng);

            latlngBck = e.latlng;

            if(clickReguaArray.length==1){
                reguaPolygon.addLatLng(e.latlng).addTo(myMap);
            }else if(clickReguaArray.length==2){
                popup
                    .setLatLng(e.latlng)
                    .openOn(myMap)
                    .setContent(calcula_dist() + " metros");
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
        console.log("entrou " + lats[i] + " " + lngs[i]);
    }
    Android.callBackPropriedade(lats, lngs);
}

function callBackPin(pin){
    var l = pin.getLatLng();
    Android.callBackPins(l.lat, l.lng, pin.getContent());
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
        console.log(i);
        properties[i].getPoly().addTo(myMap);
    }

}