<?php
$pdo = new PDO('mysql:host=127.0.0.1;dbname=maps;port=3306','root','');
$pdo->exec('set names utf8');

$json = file_get_contents("PHP://input");
$array = json_decode($json,true);
$text = $array["content"]["text"];

preg_match('/\d+\.\d+/', $text, $score);
$score_this =  empty($score[0]) ? 0 : $score[0];
file_put_contents("color.log","[xxid:".$_GET["xxid"]."]".$text.PHP_EOL,FILE_APPEND);

        $sql_update = "UPDATE girl_new set
        `face`= 1,
        `colour` = :colour
        where `xxid` = :xxid";
        $res_array = [
        ":colour" => $score_this*10,
        ":xxid" => $_GET["xxid"],
        ];
/**/
if(empty($score_this)){
        $sql_update = "UPDATE girl set `face` =1,`colour` = 0,`state` = 1 where `xxid` = :xxid";
        file_put_contents("sql.log",$sql_update.PHP_EOL,FILE_APPEND);
        $res_array = [
            ":xxid" => $_GET["xxid"],
        ];
        $stmt = $pdo->prepare($sql_update);
        $result = $stmt->execute($res_array);
}else{

        $stmt = $pdo->prepare($sql_update);
        $result = $stmt->execute($res_array);
}
//*/
if($result){
  $re = [
    200, //code
    $score_this,
    $text,
    $array["content"]["imageUrl"],
  ];
}else{
    $re = [200];
}

echo json_encode($re);

//echo $result;
