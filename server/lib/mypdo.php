<?php
/**

2018.4.10 by endler@qq.com
为了自动判断insert or update 而写

$mypod = new mypod("weibo");

$in_array = [

    "wb_id" => "222"
];
$mypod->IorU($in_array,"wb_id",111);
//*/
class mypdo{
        private $pdo = null; 
        private $table = "";
        public function __construct($table){
                $this->pdo = new PDO('mysql:host=127.0.0.1;dbname=maps;port=3306','root','');
                $this->pdo->exec('set names utf8');
                
                $this->table = $table;
        }

        public function IorU($param_array,$keyname="",$keyvalue=""){
                if(empty($keyname) or empty($keyvalue)){
                        return $this->in($param_array,$keyname,$keyvalue); 
                        //return -1;
                }
                $has = $this->find($keyname,$keyvalue);
                if($has == 1){
                        return $this->update($param_array,$keyname,$keyvalue); 
                }else{
                        return $this->in($param_array,$keyname,$keyvalue);
                }

        }
        public function update($param_array,$keyname,$keyvalue){
            $sql = 'UPDATE `'.$this->table.'` set '; 
            $d = "";
            $res_array = [];
            foreach($param_array as $key=>$param){
               
               if($param == 'now()'){
                    $sql .= $d."`".$key."` = now() " ;
                    $d = ",";
                    continue;
               }
               if($param == '+1'){
                    $sql .= $d."`".$key."` = `".$key."` + '1' " ;
                    $d = ",";
                    continue;
               }

               $sql .= $d." `".$key."` =:".$key." ";
               
               $res_array[":".$key] = $param; 
               $d = ","; 
            }
            $sql .= " where `".$keyname."` = :keyname ";
            //echo $sql;
            $res_array[":keyname"] = $keyvalue;
            try{
            $stmt = $this->pdo->prepare($sql);
            $result = $stmt->execute($res_array);
            }catch(Exception $e){
            
            }
        }
        public function in($param_array,$keyname,$keyvalue){
            $keystr = "";
            $valuestr = "";
            $res_array = [];
            $d = "";
            //print_R($param_array);
            foreach($param_array as $key=>$param){
                    $keystr .= $d." `".$key."` ";
                    if($param === 'now()'){
                            $valuestr .= $d." now() " ;
                            $d = ",";
                            continue;
                    }
                    $valuestr .= $d." :".$key." ";
                    $res_array[":".$key] = $param;
                    $d = ","; 
            }
            $sql = "INSERT INTO `".$this->table."` (".$keystr.") VALUES (".$valuestr.")";
            //echo $sql;
            //echo $valuestr;
            $stmt = $this->pdo->prepare($sql);
            $result = $stmt->execute($res_array);
        }
        public function find($keyname,$keyvalue)
        {
                if(empty($keyname) or empty($keyvalue)){
                    return -1;
                }
                
                $sql = "select * from `".$this->table."`  where `".$keyname."` = :keyvalue limit 1";
                $res_array[":keyvalue"] = $keyvalue;
                $stmt = $this->pdo->prepare($sql);
                $stmt->execute($res_array);
                if ($stmt->rowCount() > 0) {
                        return 1; 
                }else{
                        return 0;
                }

        }
        public function getlist($where,$page = 1 ,$num = 20){
                $sql = "select * from `".$this->table."` where  ".$where."  order by id desc limit ?,?";
                
                $stmt = $this->pdo->prepare($sql);
                //$stmt->bindValue(1,$keyvalue);
                $stmt->bindValue(1, $num * ($page -1), PDO::PARAM_INT);
                $stmt->bindValue(2, $num, PDO::PARAM_INT);
                $stmt->execute();
                //print_R($stmt->errorInfo());
                return $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
        }
        public function idsin($idname,$ids){
               $in = implode(',',$ids);
               $stmt = $this->pdo->prepare('SELECT * FROM `'.$this->table.'` WHERE `'.$idname.'` IN ('.$in.')');
               //print_R($in);
               //$stmt->bindParam(':id', $in);
               $stmt->execute();
               //print_R($stmt->errorInfo());
               return $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
        }
        public function getmin($col){
               if(!in_array($col,["count"])){
                    return -1;
               }
               $sql = "select * from `".$this->table."` order by `".$col."` limit 1"; 
               $sth = $this->pdo->prepare($sql);
               $sth->execute();
               return $result = $sth->fetch(PDO::FETCH_ASSOC);

        }
}
