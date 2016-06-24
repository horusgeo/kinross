myMap = L.map('mapDiv').setView([-19.315, -43.636], 15);

var options = {
                minZoom: 0,
                maxZoom: 21,
                opacity: 1.0,
                tms: false
                };


L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
    opacity: 0.2,
}).addTo(myMap);

L.tileLayer('{z}/{x}/{y}.png', options).addTo(myMap);


myMap.attributionControl.setPosition('bottomleft');

omnivore.kml('fd.kml').addTo(myMap);

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

//function savePropertiesPoints(){

//    properties.push(propertyPolygon);

//    myMap.off('click', )

//}