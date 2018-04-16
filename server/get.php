<?php
/////////////////////////////////////////////////////////////////////////////
// 这个文件是 server 项目的一部分
// 作者 ：蟠桃
// 邮箱 ：zhiweip@oupeng.com
// 说明 ：
// 
// 时间 ：2018/4/13  下午4:28
/////////////////////////////////////////////////////////////////////////////
include_once( 'lib/mypdo.php');
$pdo = new PDO('mysql:host=127.0.0.1;dbname=maps;port=3306','root','');
$pdo->exec('set names utf8');
file_put_contents("/web/maps.cc/public/girl/log/get.log",json_encode($_GET).PHP_EOL,FILE_APPEND);
if(!empty($_GET["xxid"]) && $_GET["xxid"] != "null"){
    $girl = new mypdo("girl_new");
    $update = [
        'state' => 1,
    ];
    $girl->IorU($update,"xxid",$_GET["xxid"]);
}

$sql = "select * from `girl_new` where  colour = 0 and `state` = 0 order by id limit 1";
$sth = $pdo->query($sql);
$row = $sth->fetch();
//print_r($row);
$result = [
    "200",
    "hah",
    "xixi",
    "https://maps.cc/girl/newthumb/".$row["image"],
    $row["xxid"],
    $row["url"],

];
echo json_encode($result);