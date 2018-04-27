<?php
/////////////////////////////////////////////////////////////////////////////
// 这个文件是 server 项目的一部分
// 作者 ：蟠桃
// 邮箱 ：zhiweip@oupeng.com
// 说明 ：
// 
// 时间 ：2018/4/26  下午1:26
/////////////////////////////////////////////////////////////////////////////
/**
 * boardid : comment_bbs
 * clkNum : 0
 * digest : 不朽的奥黛丽·赫本
 * docid : BN1OU51M9001U51N
 * downTimes : 217
 * img : http://easyread.ph.126.net/c8CNR5I6tGFhyQz8z3xkUw==/7917098346989689582.jpg
 * imgType : 0
 * imgsrc : http://easyread.ph.126.net/c8CNR5I6tGFhyQz8z3xkUw==/7917098346989689582.jpg
 * picCount : 0
 * pixel : 700*757
 * program : HY
 * recType : 0
 * replyCount : 5
 * replyid : BN1OU51M9001U51N
 * source : 堆糖网
 * title : 不朽的奥黛丽·赫本
 * upTimes : 909
 */
$image = [
    "boardid" => "comment_bbs",
    "clkNum" => 0,
    "digest" => "不朽的奥黛丽·赫本",
    "docid" => "BN1OU51M9001U51N",
    "downTimes" => 217,
    "img" =>  "http://easyread.ph.126.net/c8CNR5I6tGFhyQz8z3xkUw==/7917098346989689582.jpg",
    "imgType" => 0,
    "imgsrc" => "http://easyread.ph.126.net/c8CNR5I6tGFhyQz8z3xkUw==/7917098346989689582.jpg",
    "picCount" => 0,
    "pixel" => 700*757,
    "program" => "HY",
    "recType" => 0,
    "replyCount" => 5,
    "replyid" =>  "BN1OU51M9001U51N",
    "source" => "堆糖网",
    "title"  => "不朽的奥黛丽·赫本",
    "upTimes" => 909,
];
for($i = 0;$i < 100;$i ++){
    $image_list["美女"][] = $image;
}

echo json_encode($image_list);
