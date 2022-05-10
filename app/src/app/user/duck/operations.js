/*

import config from "../../../config.json";
import actions from "./actions";

const fetchSetUser = async (loginData) => {
    console.log(JSON.stringify(loginData));
    return null;
    const response = await fetch(config.API_URL + "/user/", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify(loginData)
    })
    if (!response.ok) {
        return { success: false, message: response.text() }

    }
    const json = await response.json();
    return { success: true, value: json }

}

export const setUser = (user) => dispatch(actions.set(user));
*/