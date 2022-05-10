<?php
class Feedback
{

    private $conn;
    private $table = '"PHP".feedback';



    public function __construct()
    {
        $db = new DB();
        $this->conn = $db->connect();
    }

    public function getById($orderelementid)
    {
        try {
            $query = "SELECT 
              feedbackid
            FROM 
            $this->table 
             WHERE
             orderelementid = :orderelementid";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['orderelementid' => $orderelementid]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetch();
            return array("success" => true, "value" => $data);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
    public function post($orderelementid, $userId, $foodid, $rating)
    {
        try {
            $test = $this->getById($orderelementid);
            if ($test["value"]!=null)
                return array("success" => false, "message" =>  "Rating exist");
            $query = "INSERT INTO 
             $this->table
            (
              orderelementid,
              userid,
              foodid,
              rating,
              date
            )
            VALUES (
              :orderelementid ,
             :userid ,
              :foodid ,
              :rating ,
             :date 
            );";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['orderelementid' => $orderelementid,
            'userid' => $userId,
            'foodid' => $foodid,
            'rating' => $rating,
            'date' => date('Y-m-d H:i:s')]);
            return array("success" => true);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
}
