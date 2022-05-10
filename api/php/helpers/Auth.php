<?php

require "../vendor/autoload.php";

use ReallySimpleJWT\Token;


class Auth
{


    private $secret = 'S3kR3Tn3H@sl0';

    public function generateToken($user)
    {
        $payload = [
            'iat' => time(),
            'uid' => 1,
            'exp' => time() + 3600,
            'iss' => 'localhost',
            'userid' => $user['userid'],
            'permissions' => $user['permissions'],
            'defaultaddress' => $user['defaultaddress'],
            'name' => $user['name']
        ];
        $token = Token::customPayload($payload,  $this->secret);
        return $token;
    }
}
