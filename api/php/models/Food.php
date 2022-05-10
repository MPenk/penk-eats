<?php
class Food
{

    private $conn;
    private $table = '"PHP".foods';
    private $tableCategories = '"PHP".categories';
    private $tableFeedback = '"PHP".feedback';
    


    public function __construct()
    {
        $db = new DB();
        $this->conn = $db->connect();
    }
    public function getByName($foodname)
    {
        try {
            $query = "SELECT foodid FROM $this->table WHERE name=:name";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['name' => $foodname]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetch();
            return array("success"=> true, "value"=>$data);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function get()
    {
        try {
            $query = "SELECT foods.foodid,foods.name as name ,description,price,img,categories.name as category, avg(rating) as rating
             FROM $this->table
              INNER JOIN $this->tableCategories  ON foods.categoryid = categories.categoryid
            LEFT JOIN $this->tableFeedback  ON foods.foodid = feedback.foodid WHERE foods.deleted is not true AND categories.deleted is not true
            GROUP BY foods.foodid,categories.name,foods.name,description";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $foods = $stmt->fetchAll();
            return array("success"=> true, "value"=>$foods);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function post($name, $categoryid, $description, $price, $img, $force = false)
    {
        try {
            if(!$force)
            {
                $test = $this->getByName($name);
                if($test["value"]!=null)
                    return array("success"=> true, "message" =>  "Item exist", "options"=> array("force"=>"forcePOST"));
            }
            $query = "INSERT INTO $this->table (name, categoryid, description, price, img ) VALUES (:name,:categoryid, :description, :price, :img)";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['name' => $name,'categoryid' =>  $categoryid, 'description' => $description, 'price'=>$price, 'img' => $img]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function delete($foodid)
    {
        try {
            $query = "UPDATE $this->table SET deleted = true WHERE foodid = :foodid";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['foodid' => $foodid]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function getFromLimit($from, $limit)
    {
        try {
            $query = "SELECT foodid,foods.name as name ,description,price,img,categories.name as category FROM $this->table INNER JOIN $this->tableCategories 
            ON foods.categoryid = categories.categoryid LIMIT :limit OFFSET :from";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['from' => $from, 'limit' => $limit]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $foods = $stmt->fetchAll();
            return array("success"=> true, "value"=>$foods);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
}
