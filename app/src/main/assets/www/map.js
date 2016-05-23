myMap = L.map('mapDiv').setView([-19.315, -43.636], 15);

var options = {
                minZoom: 15,
                maxZoom: 21,
                opacity: 1.0,
                tms: false
                };

L.tileLayer('{z}/{x}/{y}.png', options).addTo(myMap);
myMap.attributionControl.setPosition('bottomleft')


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