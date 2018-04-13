<?php
include_once('simple_html_dom.php');

$url = "https://xingzhexiaoweng.tuchong.com/16693817/#image18603015";
if(file_exists("data/".md5($url).".dat")){
    $file = file_get_contents("data/".md5($url).".dat");
}else{
    $file = file_get_contents($url);
    file_put_contents("data/".md5($url).".dat",$file);
}
//echo $file;
$html = new simple_html_dom();
$html->load($file);
//$html->load_file("https://xingzhexiaoweng.tuchong.com/16693817/#image18603015");
//echo $html;
$ret = $html->find('img');
//echo "<pre>";
//var_dump($ret);
foreach($ret as $ret_one){
 if($ret_one->attr["class"] == "multi-photo-image"){
    $h = pathinfo($ret_one->attr["src"], PATHINFO_EXTENSION);
    if(file_exists("img/".md5($ret_one->attr["src"]).".".pathinfo($ret_one->attr["src"], PATHINFO_EXTENSION))){
        if(file_exists("img/".md5($ret_one->attr["src"]).".".$h."_m.".$h)){

            echo "<a href='https://maps.cc/girl/img/".md5($ret_one->attr["src"]).".".$h."' target='_blank'><img src='https://maps.cc/girl/img/".md5($ret_one->attr["src"]).".".$h."_m.".$h."' ></a><br>";
        }else{
            echo "<img src='https://maps.cc/girl/img/".md5($ret_one->attr["src"]).".".$h."' ><br>";
        }
    }else{
        $img_file = file_get_contents($ret_one->attr["src"]);
        file_put_contents("img/".md5($ret_one->attr["src"]).".".pathinfo($ret_one->attr["src"], PATHINFO_EXTENSION),$img_file);
        echo md5($ret_one->attr["src"]).".".pathinfo($ret_one->attr["src"], PATHINFO_EXTENSION)."<br>";
    }
  }
}
