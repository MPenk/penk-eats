<?php
class User
{

    private $conn;
    private $table = '"PHP".users';
    private $tableOrders = '"PHP".orders';
    private $tableElements = '"PHP".orderelement';
    private $tableFoods = '"PHP".foods';


    public function __construct()
    {
        $db = new DB();
        $this->conn = $db->connect();
    }

    public function getById($userId)
    {
        try {
            $query = "SELECT userid, name, permissions, defaultaddress FROM $this->table WHERE userid=:id";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['id' => $userId]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $user = $stmt->fetch();
            return array("success" => true, "value" => array($user));
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
    public function getByName($userName)
    {
        try {
            $query = "SELECT userid FROM $this->table WHERE name=:name";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['name' => $userName]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $user = $stmt->fetch();
            return array("success" => true, "value" => $user);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }
    public function get()
    {
        try {
            $query = "SELECT userid, name, permissions, defaultaddress FROM $this->table";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $user = $stmt->fetchAll();
            return array("success" => true, "value" => $user);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }

    public function post($username, $password, $addres)
    {
        try {

            $test = $this->getByName($username);
            if ($test["value"] != null)
                return array("success" => false, "message" =>  "User exist");
            $query = "INSERT INTO $this->table (name, password, defaultaddress, permissions) VALUES (:name,:password, :address, 0)";
            $stmt = $this->conn->prepare($query);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $stmt->execute(['name' => $username, 'password' =>  password_hash($password, PASSWORD_DEFAULT), 'address' => $addres]);
            return array("success" => true);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }

    public function orders($userid)
    {
        try {
            $query = "SELECT orders.orderid, orderdate, address, status, SUM(quantity * price) as sumprice 
            FROM $this->tableOrders
            INNER JOIN $this->tableElements ON orderelement.orderid = orders.orderid 
            INNER JOIN $this->tableFoods ON orderelement.foodid = foods.foodid
            WHERE userid=:userid
            GROUP BY orders.orderid";
            $stmt = $this->conn->prepare($query);
            $stmt->execute(['userid' => $userid]);
            $stmt->setFetchMode(PDO::FETCH_ASSOC);
            $orders = $stmt->fetchAll();
            return array("success" => true, "value" => $orders);
        } catch (PDOException $e) {
            return array("success" => false, "message" =>  $e->getMessage());
        }
    }

    public function check($username, $password)
    {

        $query = "SELECT * FROM $this->table WHERE name=:name LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->execute(['name' => $username]);
        $user = $stmt->fetch();
        if (!isset($user['password']))
            return null;
        if (password_verify($password, $user['password'])) {
            $user = array('userid' => $user['userid'], 'permissions' => $user['permissions'], 'name' => $user['name'], 'defaultaddress' => $user['defaultaddress']);
            return $user;
        }
        return null;
    }

    public function promote($userid)
    {
        try {
        $user  = $this->getById($userid);
            if(!$user['success'])
                return array("success"=> false);
            if($user['value'][0]['permissions']>=2)
                return array("success"=> false);

                $nextperm = $user['value'][0]['permissions'];
            $nextperm++;
        $query = "UPDATE $this->table SET permissions = :nextperm WHERE userid=:userid";
        $stmt = $this->conn->prepare($query);
        $stmt->execute(['nextperm' => $nextperm , 'userid'=>$userid]);
        return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
    public function degrade($userid)
    {
        try {
        $user  = $this->getById($userid);
            if(!$user['success'])
                return array("success"=> false);
            if($user['value'][0]['permissions']!=1)
                return array("success"=> false);

        $query = "UPDATE $this->table SET permissions = 0 WHERE userid=:userid";
        $stmt = $this->conn->prepare($query);
        $stmt->execute(['userid'=>$userid]);
        return array("success"=> true);
        } catch (PDOException $e) {
            return array("success"=> false, "message" =>  $e->getMessage());
        }
    }
}
