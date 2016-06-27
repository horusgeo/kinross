Math.radians = function(degrees) {
  return degrees * Math.PI / 180;
};

function calcula_dist(){

    lat1 = clickReguaArray[0].lat;
    lat2 = clickReguaArray[1].lat;

    lng1 = clickReguaArray[0].lng;
    lng2 = clickReguaArray[1].lng;

    dist = 6371*Math.acos(Math.cos(Math.radians(90-lat2))*Math.cos(Math.radians(90-lat1)) + Math.sin(Math.radians(90-lat2))*Math.sin(Math.radians(90-lat1))*Math.cos(Math.radians(lng1-lng2)));

    return 1000*dist;
}