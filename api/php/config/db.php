<?php 

class DB{
    private $host = 'localhost';
    private $port = '5432';
    private $user = '******';
    private $password = '******';
    private $database = '******';

    public function connect(){
        $conn_string =  "pgsql:host=$this->host;port=$this->port;dbname=$this->database";

        $conn = new PDO($conn_string , $this->user, $this->password);
        $conn->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);


        return $conn;
    }
}
?>