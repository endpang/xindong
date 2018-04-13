<?php


include_once( 'lib/mypdo.php');


$girl = new mypdo("girl_new");

$list = $girl->getlist(" `state` = 0 and `colour` > 79 ",1,100);
$wb_list = array_unique(array_column($list, 'wb_id'));
$user_list = array_unique(array_column($list,'uid'));


$weibo = new  mypdo("weibo");
$user = new mypdo("weibo_user");

//$weibo_array = $weibo->idsin("wb_id",$wb_list);
$user_array = $user->idsin("userid",$user_list);

$user_re = [];

foreach($user_array as $user_one){
    $user_re[$user_one["userid"]] = $user_one;
}

foreach($list as $key=>$list_one){
    $list[$key]["image"] = "https://image.maps.cc/newthumb/".$list_one["image"];
    $list[$key]["user"] = (object)$user_re[$list_one["uid"]];
    
}
//$result["image"] = $list;
//$result["weibo"] = $weibo_array;
//$result["user"] = $user_array;

echo json_encode($list);

//print_r($weibo_array);
//print_r($user_array);
