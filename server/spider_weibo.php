<?php
include_once( 'libweibo/config.php' );
include_once( 'libweibo/saetv2.ex.class.php' );
include_once( 'lib/mypdo.php');

$pdo_user = new mypdo("user");
if($user = $pdo_user->getmin("count")){
    //print_R($user);
    $token = $user["access_token"];
    $c = new SaeTClientV2( WB_AKEY , WB_SKEY , $token);
    $home_timeline = $c->home_timeline($page = 1, $count = 200, $since_id = 0, $max_id = 0, $base_app = 0, $feature = 2);
    //header('Content-type: application/json');
    //echo json_encode($home_timeline);
    //exit;
    //print_R($home_timeline);
    foreach($home_timeline["statuses"] as $wb){
            //$re["id"] = $wb["id"];
            $result = findpic($wb,$user["wb_id"]);
            //print_R($result);
            //exit;
            //foreach();
    }

    $up_this = [
        "count" => "+1",   
    ];
    $pdo_user->IorU($up_this,"wb_id",$user["wb_id"]);
}

function findpic($wb,$byid){
    if(!empty($wb["retweeted_status"])){
        return findpic($wb["retweeted_status"],$byid);   
    }
    $result = [];
    $girl = new mypdo("girl_new");
    $result["id"] = $wb["id"];
    $result["uid"] = $wb["user"]["id"];
    if(empty($wb["pic_urls"])){
        $result["error"] = 1; 
        return $result;
    }
    $f = 0;
    foreach($wb["pic_urls"] as $pic_one){
        $url = str_replace("thumbnail","mw690",$pic_one["thumbnail_pic"]);
        
        $h = pathinfo($url, PATHINFO_EXTENSION);
        if(in_array($h,["gif"])){
            continue;
        }
        $img_file = file_get_contents($url);
        if(!empty($img_file)){
                //file_put_contents("/web/maps.cc/public/girl/newimg/".md5($url).".".$h,$img_file);
                $local_image = "/web/maps.cc/public/girl/newimg/".md5($url).".".$h;
                if(file_exists($local_image)){
                        continue;
                }
                file_put_contents($local_image,$img_file);          
                $thumb = file_get_contents("http://127.0.0.1:8080/find/".md5($url).".".$h);
                //echo "http://127.0.0.1:8080/find/".md5($url).".".$h;
                //$sql = "INSERT INTO `maps`.`girl` (`xxid`, `image`, `thumb`, `face`, `colour`, `datatime`, `url`) VALUES (:xxid, :image, :thumb, :face, :colour,NOW(),:url )";
                if(!empty($thumb)){
                        $result["img"][] = $thumb;
                        $insert_array = [
                                "xxid" => md5($url), 
                                "image" => md5($url).".".$h,
                                'thumb' => 1,
                                'face' => 0,
                                'colour' => 0,
                                'url' => $url,
                                'by' => $byid,
                                'datatime' => 'now()',
                                'wb_id'=>$wb["id"],
                                'uid'=>$wb["user"]["id"],
                        ];
                        $girl->IorU($insert_array);
                        $f = 1;
                }else{
                       unlink($local_image); 
                }
        }else{
            //echo "empty<br>";
        }
        /**
        $reget = file_get_contents("https://maps.cc/girl/get.php?url=".urlencode($url));
        $result["get"][] = [
            "url" => $url,
            "result" => $reget,
        ];
        //*/
    }
    if($f == 1){
       $weibo = new mypdo("weibo"); 
       $weibo_user = new mypdo("weibo_user");
       
       $weibo_array = [
            "created_at" => date("Y-m-d H:i:s",strtotime($wb["created_at"])),
            "wb_id" => $wb["id"],            
            "text" => $wb["text"],
            "userid" => $wb["user"]["id"],
            "reposts_count" => $wb["reposts_count"],
            "comments_count"=> $wb["comments_count"],
            "attitudes_count"=> $wb["attitudes_count"],
       ];
       $weibo->IorU($weibo_array);
       $user = $wb["user"];
       $weibo_user_array = [
           "userid" => $user["id"], 
           "screen_name" => $user["screen_name"], 
           "province" => $user["province"], 
           "city" => $user["city"], 
           "location" => $user["location"], 
           "avatar_large" => $user["avatar_large"], 
           "verified" => $user["verified"], 
           "verified_reason" => $user["verified_reason"], 
           "followers_count" => $user["followers_count"],
           "created_at" => date("Y-m-d H:i:s",strtotime($user["created_at"])),
       ];
        $weibo_user->IorU($weibo_user_array);
    }
    return $result;

}


