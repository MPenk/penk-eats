<?php


$app->post('', function ($request, $response, $args) {
    $token = $request->getAttribute("token");
    $parsedBody = $request->getParsedBody();
    $userid = $token["userid"];
    if ($token["permissions"] >= 0) {

        $model = new Feedback();
        $data = $model->post( $parsedBody['orderelementid'],$userid, $parsedBody['foodid'], $parsedBody['rating']);

        if ($data["success"] == true) {
            return returnResponce($data, 200, $response);
        }
        return returnResponce($data, 400, $response);
    } else {
        $data = array('message' => 'Unauthorized');
        return returnResponce($data, 401, $response);
    }
});

