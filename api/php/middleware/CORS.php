<?php
use Slim\Psr7\Response;
use ReallySimpleJWT\Token;

class CORS
{

    public function __invoke($request, $handler) {
        $response = $handler->handle($request);
        return $response->withHeader('Access-Control-Allow-Origin', '*');
    }

}