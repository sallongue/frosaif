<?php
$json = file_get_contents('php://input');
$obj = json_decode($json);
$lon = $obj->lon;
$details = $obj->details;
$email = $obj->{'email'};
$photo = $obj->photo;
if(strcmp($photo,"nophoto")) {
    $image = $obj->image;
    file_put_contents("/var/www/html/titi.png", base64_decode($image));
}
$str = "\n----- details = ".$details;
$str .= "\n----- photo = ".$photo;
$str .= "\n----- ".$image;
$f = fopen("toto.txt","w+");
#fputs($f,$str);
#fputs($f,var_dump($json));
echo "success\n";
echo '"tag":"DDD444"';
?>
