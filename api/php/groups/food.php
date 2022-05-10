<?php

$app->post('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 1) {
        $parsedBody = $request->getParsedBody();
        $model = new Food();
        try {
            $force = $request->getQueryParam('force', false);
            $data = $model->post($parsedBody['name'], $parsedBody['categoryid'], $parsedBody['description'], $parsedBody['price'], $parsedBody['img'], $force ? true : false);
        } catch (Throwable $th) {
            $data = array("success" => false, "message" =>  "Bad params");
            return returnResponce($data, 400, $response);
        }
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->delete('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 1) {
        $parsedBody = $request->getParsedBody();
        $model = new Food();
        $data = $model->delete($parsedBody['foodid']);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
