import config from '../config.json'
export const execute = async (path, requestMethod, setError, data) => {
    //console.log(path, requestMethod,  data);
    setError({ exist: false, message: "" })
    let resData = await fetch(config.API_URL + path, {
        headers: {
            'Accept': '*/*',
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa("student:student"),
            'X-Authorization': 'Bearer ' + localStorage.getItem("token")
        },
        method: requestMethod,
        body: JSON.stringify(data)
    }).then((response) => {
        if (!response.ok)
            throw response;
        return response.json();
    }).catch(async err => {
        try {

            err.json().then(response => {
                setError({ exist: true, message: response.message })
            });
            return false;
        } catch (error) {
            console.log(err)
            console.log(error);
        }
    })
    return resData;
}