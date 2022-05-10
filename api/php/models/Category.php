<?php
class Category
{

    private $conn;
    private $table = '"PHP".categories';



    public function __construct()
    {
        $db = new DB();
        $this->conn = $db->connect();
    }

    public function get()
    {
        try {
            $query = "SELECT 
            categoryid,
            name
           FROM $this->table WHERE deleted is not true";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetchAll();
            return array("success" => true, "value" => $data);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
    public function getAll()
    {
        try {
            $query = "SELECT 
            categoryid,
            name,
            deleted
           FROM $this->table ";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetchAll();
            return array("success" => true, "value" => $data);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
    public function getByName($name)
    {
        try {
            $query = "SELECT categoryid FROM $this->table WHERE name=:name";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['name' => $name]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetch();
            return array("success"=> true, "value"=>$data);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function post($name)
    {
        try {
                $test = $this->getByName($name);
                if($test["value"]!=null)
                    return array("success"=> false, "message" =>  "Item exist");
            $query = "INSERT INTO $this->table (name) VALUES (:name)";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['name' => $name]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function getFromLimit($from, $limit)
    {
        try {
            $query = "SELECT 
            categoryid,
            name
           FROM $this->table LIMIT :limit OFFSET :from";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['from' => $from, 'limit' => $limit]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $data = $stmt->fetchAll();
            return array("success" => true, "value" => $data);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }

    public function delete($id)
    {
        try {
            $query = "UPDATE $this->table SET deleted = true WHERE categoryid = :id";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['id' => $id]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function restore($id)
    {
        try {
            $query = "UPDATE $this->table SET deleted = false WHERE categoryid = :id";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['id' => $id]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
}
