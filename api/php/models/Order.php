<?php
class Order
{

    private $conn;
    private $table = '"PHP".orders';
    private $tableElements = '"PHP".orderelement';
    private $tableFoods = '"PHP".foods';
    private $tableUsers = '"PHP".users';

    private $tableFeedback = '"PHP".feedback';


    public function __construct()
    {
        $db = new DB();
        $this->conn = $db->connect();
    }

    public function getByCategory()
    {
        try {
            $query = "SELECT foodid,foods.name as name ,description,price,img,categories.name as category FROM $this->table INNER JOIN $this->tableCategories 
            ON foods.categoryid = categories.categoryid";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $orders = $stmt->fetchAll();
            return array("success"=> true, "value"=>$orders);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function getById($orderid)
    {
        try {
            $query = "SELECT rating, orderelement.foodid, orders.userid, orderelement.orderelementid,foods.name as foodname, additionalinformation, quantity,foods.price 
            FROM $this->tableElements 
            INNER JOIN $this->tableFoods ON orderelement.foodid = foods.foodid 
            INNER JOIN $this->table ON orderelement.orderid = orders.orderid 
            LEFT JOIN $this->tableFeedback ON  orderelement.orderelementid= feedback.orderelementid
            WHERE orderelement.orderid=:orderid";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['orderid' => $orderid]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $order = $stmt->fetchAll();
            return array("success"=> true, "value"=>$order);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
        
    }
    public function getByIdAdd($orderid)
    {
        try {
            $query = "SELECT orders.orderid, orders.userid, orderdate, address, status, SUM(quantity * price) as sumprice 
            FROM $this->table 
            INNER JOIN $this->tableElements ON orderelement.orderid = orders.orderid 
            INNER JOIN $this->tableFoods ON orderelement.foodid = foods.foodid
            WHERE orders.orderid=:orderid
            GROUP BY orders.orderid";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['orderid' => $orderid]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $order = $stmt->fetchAll();
            return array("success"=> true, "value"=>$order);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
        
    }
    public function move($id)
    {
        try {
            $order  = $this->getByIdAdd($id);
            if(!$order['success'])
                return array("success"=> false);
            if($order['value'][0]['status']<0||$order['value'][0]['status']>=3)
                return array("success"=> false);
            $nextstatus = $order['value'][0]['status'];
            $nextstatus++;
            $query = "UPDATE $this->table SET status = :status WHERE orderid = :id";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['id' => $id,'status' => $nextstatus]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function cancel($id)
    {
        try {
            $order  = $this->getByIdAdd($id);
            if(!$order['success'])
                return array("success"=> false);
            if($order['value'][0]['status']<0||$order['value'][0]['status']>=3)
                return array("success"=> false);
            $query = "UPDATE $this->table SET status = -1 WHERE orderid = :id";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['id' => $id]);
            return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function get()
    {
        try {
            $query = "SELECT status, orders.userid, orders.orderid, users.name, orderelementid,foods.name as foodname, additionalinformation, quantity,foods.price 
            FROM $this->tableElements 
            INNER JOIN $this->tableFoods ON orderelement.foodid = foods.foodid 
            INNER JOIN $this->table ON orderelement.orderid = orders.orderid  
            INNER JOIN $this->tableUsers ON users.userid = orders.userid  ";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $orders = $stmt->fetchAll();
            return array("success"=> true, "value"=>$orders);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
        
    }
    public function getActive()
    {
        try {
            $query = "SELECT orders.orderid, orderdate, address, status, SUM(quantity * price) as sumprice 
            FROM $this->table
            INNER JOIN $this->tableElements ON orderelement.orderid = orders.orderid 
            INNER JOIN $this->tableFoods ON orderelement.foodid = foods.foodid
            WHERE CAST (status AS INTEGER) >=0 AND CAST (status AS INTEGER) <3
            GROUP BY orders.orderid
            ";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $orders = $stmt->fetchAll();
            return array("success"=> true, "value"=>$orders);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
        
    }
    public function post($userid, $address, $elements)
    {
        try {
            $this->conn->beginTransaction();
            $query = "INSERT INTO $this->table (orderdate, userid, address, status) VALUES ( :date, :userid, :address, 0)";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['date' => date('Y-m-d H:i:s'),'userid' => $userid,'address' => $address]);
            $orderid = $this->conn->lastInsertId();
            
            foreach ($elements as $key => $value) {
                $query = "INSERT INTO $this->tableElements (orderid, foodid, additionalinformation, quantity) VALUES ( :orderid, :foodid, :additionalinformation, :quantity)";
                $stmt = $this->conn->prepare($query);
                $stmt->execute(['orderid' => $orderid,'foodid' => $value['foodid'],'additionalinformation' => $value['additionalinformation'],'quantity' => $value['quantity']]);
            }
            $this->conn->commit();

            return true;
        } catch (PDOException $e) {
            //error
            $this->conn->rollBack();
            return  $e->getMessage();
        }
    }

    
}
