<?php
$app->get('/all', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 1) {
        $model = new User();
        $data = $model->get();
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->get('/{id}', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $id = $args['id'];

    if ($token["userid"] == $id || $token["permissions"] > 1) {
        $model = new User();
        $data = $model->getById($id);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});

$app->get('/{id}/orders', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $id = $args['id'];

    if ($token["userid"] == $id || $token["permissions"] > 1) {
        $model = new User();
        $data = $model->orders($id);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->post('/{id}/promote', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $id = $args['id'];

    if ($token["permissions"] > 1) {
        $model = new User();
        $data = $model->promote($id);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->post('/{id}/degrade', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $id = $args['id'];

    if ($token["permissions"] > 1) {
        $model = new User();
        $data = $model->degrade($id);
        if ($data["success"] == true)
            return returnResponce($data, 200, $response);
        return returnResponce($data, 500, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
