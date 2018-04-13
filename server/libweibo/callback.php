<?php
session_start();

include_once( 'config.php' );
include_once( 'saetv2.ex.class.php' );
//include_once('../lib/mysql.lib.php');

$pdo = new PDO('mysql:host=127.0.0.1;dbname=yibaifen;port=3306','root','');
$pdo->exec('set names utf8');

$o = new SaeTOAuthV2( WB_AKEY , WB_SKEY );

if (isset($_REQUEST['code'])) {
	$keys = array();
	$keys['code'] = $_REQUEST['code'];
	$keys['redirect_uri'] = WB_CALLBACK_URL;
	try {
		$token = $o->getAccessToken( 'code', $keys ) ;
	} catch (OAuthException $e) {
	}
}

if ($token) {

    $c = new SaeTClientV2( WB_AKEY , WB_SKEY , $token['access_token'] );
    //$ms  = $c->home_timeline(); // done
    $uid_get = $c->get_uid();
    $uid = $uid_get['uid'];
    $user_message = $c->show_user_by_id( $uid);
    //$res = $mysql->limit(1)->where("wb_id=".$uid)->select("user");
    $sql_check = "select * from user where wb_id = '".$uid."'";
    if ($res = $pdo->query($sql_check)) {
        if ($res->fetchColumn() > 0) {
                $sql_update = "UPDATE user set
                        `username`= :username,
                        `token`= :token,
                        `location`= :location,
                        `verified` = :verified,
                        `token_flush` = now(),
                        `json` = :json 
                        where `wb_id` = :wb_id"; 
                $res_array = [
                        ":username" => $user_message["screen_name"],
                        ":token"    => $token['access_token'],
                        ":location" => $user_message["location"],
                        ":verified" => $user_message["verified"],
                        ":json"     => json_encode($c),
                        ":wb_id" => $uid,
                        ];
                $stmt = $pdo->prepare($sql_update);
                $result = $stmt->execute($res_array);
        }else{
                $sql_insert = "INSERT INTO  user (`username`,`wb_id`,`token`,`location`,`verified`,`created_at`,`createtime`,`token_flush`,`json`) VALUES (:username,:wb_id,:token,:location,:verified,:created_at,now(),now(),:json)";
                $res_array = [
                        ":username" => $user_message["screen_name"],
                        ":wb_id"    => $uid,
                        ":token"    => $token['access_token'],
                        ":location" => $user_message["location"],
                        ":verified" => $user_message["verified"],
                        ":created_at" => strtotime($user_message["created_at"]),
                        ":json"     => json_encode($c),
                        ];
                $stmt = $pdo->prepare($sql_insert);
                $stmt->execute($res_array);
        }
    }
   /** 
    if(empty($res)){
       $sql_insert = "INSERT INTO  user (`username`,`wb_id`,`token`,`location`,`verified`,`created_at`,`createtime`,`token_flush`,`json`) VALUES (:username,:wb_id,:token,:location,:verified,:created_at,now(),now(),:json)";
       $res_array = [
         ":username" => $user_message["screen_name"],
         ":wb_id"    => $uid,
         ":token"    => $token['access_token'],
         ":location" => $user_message["location"],
         ":verified" => $user_message["verified"],
         ":created_at" => strtotime($user_message["created_at"]),
         ":json"     => json_encode($c),
      ];
      $stmt = $pdo->prepare($sql_insert);
      $stmt->execute($res_array);

    }else{
        $sql_update = "UPDATE user set ";
        $res_array = [
            "username" => $user_message["screen_name"],
            "token"    => $token['access_token'],
            "location" => $user_message["location"],
            "verified" => $user_message["verified"],
            "token_flush"=> time(),
            "json"     => json_encode($c),
        ]);
    }
    //*/

    $_SESSION['token'] = $token;
    $_SESSION["uid"] = $uid;
	setcookie( 'weibojs_'.$o->client_id, http_build_query($token) );
?>
授权完成,<a href="/">进入你的微博列表页面</a><br />
<?php
} else {
?>
授权失败。
<?php
}
?>
