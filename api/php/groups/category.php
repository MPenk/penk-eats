<?php


$app->delete('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");

    if ($token["permissions"] > 1) {
        $parsedBody = $request->getParsedBody();
        $model = new Category();
        $data = $model->delete($parsedBody['categoryid']);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});

$app->post('/restore', function ($request, $response, $args) {
    $token = $request->getAttribute("token");

    if ($token["permissions"] > 1) {
        $parsedBody = $request->getParsedBody();
        $model = new Category();
        $data = $model->restore($parsedBody['categoryid']);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});

$app->post('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 1) {
        $parsedBody = $request->getParsedBody();
        $model = new Category();
        try {
            $data = $model->post($parsedBody['name']);
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

$app->get('', function ($request, $response, $args) {
    $model = new Category();
    $data = $model->getAll();
    if ($data["success"] == true)
        return returnResponce($data, 200, $response);
    return returnResponce($data, 500, $response);
});