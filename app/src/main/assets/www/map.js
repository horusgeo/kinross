myMap = L.map('mapDiv', {
//            measureControl:true
            }).setView([-19.315, -43.636], 15);

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

var runLayer = omnivore.kml('./kml/fd.kml')
    .on('ready', function() {
         myMap.fitBounds(runLayer.getBounds());
    })
    .addTo(myMap);
L.control.scale().addTo(myMap);

//clickRegua();

function clickPoints(){

    var popup = L.popup();

    function onMapClick(e) {
        popup
            .setLatLng(e.latlng)
            .setContent(e.latlng.toString())
            .openOn(myMap);
        clickPointsArray.push(e.latlng);
        propertyPolygon.addLatLng(e.latlng).addTo(myMap);
    }

    myMap.on('click', onMapClick);

}

var popup = L.popup({closeButton: false});

function clickRegua(){

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
    myMap.off('click', null);
    myMap.removeLayer(reguaPolygon);
    myMap.closePopup(popup);
}



//function savePropertiesPoints(){

//    properties.push(propertyPolygon);

//    myMap.off('click', )

//}