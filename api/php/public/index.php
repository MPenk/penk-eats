<?php

use GuzzleHttp\Psr7\Query;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Selective\BasePath\BasePathMiddleware;
use Tuupola\Middleware\JwtAuthentication;
use Tuupola\Middleware\CorsMiddleware;
use Slim\Factory\AppFactory;

require __DIR__ . '/../vendor/autoload.php';

require('../config/db.php');
require '../models/User.php';
require '../helpers/Auth.php';
require '../models/Food.php';
require '../models/Feedback.php';
require '../models/Order.php';
require '../models/Category.php';
require '../middleware/CORS.php';

$app = AppFactory::create();

$app->addRoutingMiddleware();
$app->add(new BasePathMiddleware($app));
$app->addErrorMiddleware(true, true, true);
$c = $app->getContainer();
$c['errorHandler'] = function ($c) {
    return function ($request, $response, $exception) use ($c) {
        return $response->withStatus(500)
            ->withHeader('Content-Type', 'application/json');
    };
};
$app->add(new JwtAuthentication([
    "path" => ["/PHP/api"],
    "ignore" => ["/PHP/api/public","/PHP/api/login", "/PHP/api/test"],
    "secret" => "S3kR3Tn3H@sl0",
    "error" => function ($response, $arguments) {
        $data["success"] = false;
        $data["status"] = 401;
        $data["message"] = $arguments["message"];
        return $response
            ->withHeader("Content-Type", "application/json")
            ->getBody()->write(json_encode($data, JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT));
    }
]));
$app->add(new CorsMiddleware([
    "origin" => ["*"],
    "methods" => ["GET", "POST", "PATCH", "DELETE", "OPTIONS"],
    "headers.allow" => ["Origin", "Content-Type", "Authorization","Authorization2", "Accept", "ignoreLoadingBar", "X-Requested-With", "Access-Control-Allow-Origin"],
    "headers.expose" => [],
    "credentials" => true,
    "cache" => 0,
]));

$app->group('/login', function ($app) {

    $app->post('', function ($request, $response, $args) {
        $parsedBody = $request->getParsedBody();
        $model = new User();
        $user = $model->check($parsedBody['username'], $parsedBody['password']);

        if (is_null($user)) {
            $data = array('message' => 'incorrect usernames/password');
            return returnResponce($data, 401, $response);
        }

        $auth = new Auth();
        $token = $auth->generateToken($user);
        $data = array('token' => $token);
        return returnResponce($data, 200, $response);
    });
});

$app->group('/food', function ($app) {
    require('../groups/food.php');  
});
$app->group('/public', function ($app) {
    require('../groups/public.php');  
});

$app->group('/order', function ($app) {

    require('../groups/order.php');  
    
});

$app->group('/feedback', function ($app) {

    require('../groups/feedback.php');  
});
$app->group('/category', function ($app) {
    require('../groups/category.php');  
});

$app->group('/user', function ($app) {
    require('../groups/user.php');  
});

$app->get('/test', function (Request $request, Response $response, $args) {
    $data = array("success" => true, 'message' => 'Wiadomość testowa');
    return returnResponce($data, 200, $response);
});



$app->run();


function returnResponce($data, $status, $response)
{
    $response->getBody()->write(json_encode($data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus($status);
}
