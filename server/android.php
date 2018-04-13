<?php

if(empty($_GET["xxid"])){
    echo "";
    exit;
}

$pdo = new PDO('mysql:host=127.0.0.1;dbname=maps;port=3306','root','');
$pdo->exec('set names utf8');

$sql = "select * from `girl_new` where `xxid` = '".$_GET["xxid"]."'";

$sth = $pdo->query($sql);

while($row = $sth->fetch()){
    //print_R($row);
    $xxid = $row["xxid"];
    $pic_path = "/web/maps.cc/public/girl/newthumb/".$row["image"];
    $pic = file_get_contents($pic_path);
    $base64_img = base64_encode($pic);
    $result = [
        $xxid,
        $base64_img,
    ];
    echo json_encode($result);
}
