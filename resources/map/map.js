/** Global variables
 * Week days list
 * Map initialization
 * . . . etc
 */

var WEEK_DAYS = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"];

var map = L.map('mapid');
var markers = L.markerClusterGroup();

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets',
    accessToken: 'pk.eyJ1IjoiYmVpbnRlY2giLCJhIjoiY2pnNjl5aWQxMDhhOTJ5cXRldGt2YW9sNCJ9.U-Ol1oKEckIrx2bZFkJtRQ'
}).addTo(map);

var popup = L.popup();

function onMapClick(e) {
    var s = e.latlng.lat + "," +e.latlng.lng;
    myJavaMember.nearestShop(s, "driving");
}

function onHover(e){
    var popOnHover = L.popup();
    popOnHover
        .bindPopup("Lat: " + e.latlng.lat + "<br>Lng: " + e.latlng.lng)
        .openPopup();
}

function onClickOn() {
    map.on('click', onMapClick);
}


function onClickOff() {
    map.off('click', onMapClick);
}
// reset modal to origin state . . .
$('#mymodal').on('show.bs.modal', function (e) {
    // do something...
    $("#ajout_form").css("display", "block");
    $('#succes_ajout_mag').css("display", 'none');
    $("#mag_ajout").css("display", "block");
    $("#mag_annuler").css("display", "block");
    $("#btn_close_ajout").css("display", "none");
    $("#nom_mag_input").val("");
    $("#adresse_mag_input").val("");
});

function showOpeningHours(days, hours) {
    var res = [];
    days.map(function (a, index) {
        res.push("<b><em>" + a + "</em></b>" + "  |  " + hours[index]);
    });
    return res.join("<br>");
}

function AddingProductToShop() {
    myJavaMember.addProducts();
}

function markerCreation(lng, lat, shop_name, shop_hours) {
    var marker = L.marker([lat, lng]);
    marker
        .on('click', function () {
            myJavaMember.onClickMarker(lng, lat);
        })
        .bindPopup(
            '<b style="font-size:15px; color:green">Magasin:    </b><b style="font-size:15px">'
            + shop_name + '</b>,<br><br>' + showOpeningHours(WEEK_DAYS, shop_hours.split(";"))
            + '<br><br> <a href="#" onclick="AddingProductToShop()"><b>Ajouter des produits</b></<a>'
        );

    markers.addLayer(marker);
}

function recenterMap(lat, longi){
	map.setView([
        lat,
        longi
    ], 13);
}

function removeMarkers() {
    markers.clearLayers();
}

function add_markers() {
    map.addLayer(markers);
}

function addShopOnMap(){
    map.on('contextmenu', function (e) {
        console.log(e.latlng);
        var current_lng = e.latlng.lng;
        var current_lat = e.latlng.lat;
        popup
            .setLatLng(e.latlng)
            .setContent("You clicked the map at " + e.latlng.toString());

        $('#mymodal').modal('show');

        $('#mymodal').on('hide.bs.modal', function (e) {
            // do something...
            current_lat = null;
            current_lng = null;
        });

        $("#mag_ajout").on("click", function () {
            var k = false;
            var openingHours = [];
            var caracteristics = [];
            var nom = $("#nom_mag_input").val();
            var adr = $("#adresse_mag_input").val();

            var checkedValue = null;
            var checkboxElements = document.getElementsByClassName('checkboxes');
            for (var i = 0; checkboxElements[i]; ++i) {
                if (checkboxElements[i].checked) {
                    checkedValue = checkboxElements[i].value;
                    caracteristics.push(checkedValue);
                } else {
                    caracteristics.push(k);
                }
            }

            var days_cls_names = ['h_lun', 'h_mar', 'h_mer', 'h_jeu', 'h_vend', 'h_sam', 'h_dim'];
            var day_op, day_val, val;
            for (var m = 0; days_cls_names[m]; m++) {
                day_op = document.getElementsByClassName(days_cls_names[m]);
                day_val = [];
                val = null;
                for (var i = 0; day_op[i]; i++) {
                    val = day_op[i].value;
                    if (!val) {
                        val = "00:00";
                        day_val.push(val);
                    } else {
                        day_val.push(val);
                    }
                }
                day_val = day_val.join("-");
                openingHours.push(day_val);
            }

            caracteristics = caracteristics.join(";");
            openingHours = openingHours.join(";");
            if (nom && adr && current_lng && current_lat) {
                myJavaMember.addingNewShop(current_lng, current_lat, nom, adr, caracteristics, openingHours);

                //affichage d'un message au user . . .
                $("#ajout_form").css("display", "none");
                $('#succes_ajout_mag').css("display", 'block');
                $("#btn_close_ajout").css("display", "block");
                $("#mag_ajout").css("display", "none");
                $("#mag_annuler").css("display", "none");
            }
        });
    });
}

function rightClickOn() {
    map.on('contextmenu', addShopOnMap);
}

function rightClickOff() {
    map.off('contextmenu', addShopOnMap);
}
