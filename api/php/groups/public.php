<?php
$app->post('/user', function ($request, $response, $args) {
    $parsedBody = $request->getParsedBody();
    $model = new User();
    $data = $model->post($parsedBody['username'], $parsedBody['password'], $parsedBody['address']);
    if ($data["success"] == true)
        return returnResponce($data, 200, $response);
    return returnResponce($data, 500, $response);
});
$app->get('/food', function ($request, $response, $args) {
    $model = new Food();
    $data = $model->get();
    if ($data["success"] == true)
        return returnResponce($data, 200, $response);
    return returnResponce($data, 400, $response);
});

$app->get('/food/{from}/{limit}', function ($request, $response, $args) {
    $from = $args['from'];
    $to = $args['limit'];
    $model = new Food();
    $data = $model->getFromLimit($from, $to);
    if ($data["success"] == true)
        return returnResponce($data, 200, $response);
    return returnResponce($data, 400, $response);
});

$app->get('/category', function ($request, $response, $args) {
    $model = new Category();
    $data = $model->get();
    if ($data["success"] == true)
        return returnResponce($data, 200, $response);
    return returnResponce($data, 500, $response);
});