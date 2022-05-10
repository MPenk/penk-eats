<?php
$app->post('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $parsedBody = $request->getParsedBody();
    $userid = $token["userid"];
    if ($token["permissions"] >= 0) {

        $model = new Order();
        $query = $model->post($userid, $parsedBody['address'], $parsedBody['elements']);

        if ($query === true) {
            return returnResponce($query, 200, $response);
        }
        return returnResponce($query, 400, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->get('/active', function ($request, $response, $args) {
    print_r($request);
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 0) {
        $model = new Order();
        $data = $model->getActive();
        if ($data["success"] == true) {
            return returnResponce($data, 200, $response);
        }
        return returnResponce($data, 400, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
$app->get('/{id}', function ($request, $response, $args) {
    $id = $args['id'];
    $model = new Order();
    $data = $model->getById($id);
    if ($data["success"] == true) {
        $token = $request->getAttribute("token");
        if ($token["userid"] == $data['value'][0]['userid'] || $token["permissions"] >= 1) {
            $add = $model->getByIdAdd($id);
            $data["add"] = $add['value'];
            return returnResponce($data, 200, $response);
        }
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
    return returnResponce($data, 400, $response);
});
$app->post('/{id}/move', function ($request, $response, $args) {
    $id = $args['id'];
    $model = new Order();

    $data = $model->move($id);
    if ($data["success"] == true) {
        $token = $request->getAttribute("token");
        if ($token["permissions"] >= 1) {
            return returnResponce($data, 200, $response);
        }
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
    return returnResponce($data, 400, $response);
});
$app->post('/{id}/cancel', function ($request, $response, $args) {
    $id = $args['id'];
    $model = new Order();
    $token = $request->getAttribute("token");
    $data = $model->getByIdAdd($id);
    if ($token["userid"] == $data['value'][0]['userid'] || $token["permissions"] >= 1) {

        $data = $model->cancel($id);
        if ($data["success"] == true) {
            $token = $request->getAttribute("token");
            if ($token["permissions"] >= 1) {
                return returnResponce($data, 200, $response);
            }
            $data = array('message' => 'Unauthorized');
            return returnResponce($data, 401, $response);
        }
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
    return returnResponce($data, 400, $response);
});
$app->get('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    if ($token["permissions"] > 1) {
        $model = new Order();
        $data = $model->get();
        if ($data["success"] == true) {
            $token = $request->getAttribute("token");
            return returnResponce($data, 200, $response);
        }
        return returnResponce($data, 400, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});
