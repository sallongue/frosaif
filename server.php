<?php
# allow to retrieve the json file sent by the android app
$json = file_get_contents('php://input');
$obj = json_decode($json);
# retrieve the information encoded into the json
# The json object contains the following information:
# "lon" longitude of the event  
# "lat" latitude  of the event  
# "address" address calculated thanbks to google services thanks to GPS coordinates
# "event" event code
#       "SNP" stands for  "Signalement Nid primaire"
#       "SNS" = "Signalement Nid secondaire"
#       "DNP" = "Destruction Nid primaire"
#       "DNS" = "Destruction Nid secondaire":
#       "DNAD" = "Descente du nid apres destruction":
#       "INC" = "Incident":
#       "ADR" = "Attaque de rucher":
#       "ADP" = "Attaque de personne":
#       "FRI" = "Frelon isolé":
#       "INF" = "Information":
# "role" Role of the declarant
# "email" 
# "nom" name of the declarant
# "tel" telephon of the declarant 
# "details" free string for details 
# "android" = "1" indicates that the POST is from the android app
# "photo" if value equal to "photo" then the "image has been filled
#         else "nophoto" 
# "image" the encoded image sent by the declarant
#
# in the following some json access example especially the decoding of the image if sent
# in real the information are put into the database
$lon = $obj->lon;
$details = $obj->details;
$email = $obj->{'email'};
$photo = $obj->photo;
if(strcmp($photo,"nophoto")) {
    $image = $obj->image;
    file_put_contents("/var/www/html/titi.png", base64_decode($image));
}
# if everithing is allright, send back success and the tag used to uniquelly identify the
# event in the database and provided by this latest
echo "success\n";
# fake tag value
echo '"tag":"DDD444"';
?>
