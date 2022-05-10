import types from "./types";

const set = item => ({
    type: types.SET_USER, name: item.name, userid: item.userid, permissions: item.permissions, defaultaddress: item.defaultaddress
})

const clear = item => ({
    type: types.CLEAR_USER, item
})

export default{
    set,
    clear
}