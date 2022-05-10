import types from "./types"

const INITIAL_STATE = {
    isLogged: false,
    userid: null,
    name: null,
    permissions: null
}

const userReducer = (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case types.SET_USER:
            return {
                ...state,
                isLogged: true,
                userid: action.userid,
                name: action.name,
                permissions: action.permissions,
                defaultaddress: action.defaultaddress
            }
        case types.CLEAR_USER:
            return {
                isLogged: false,
                userid: null,
                name: null,
                permissions: null,
                defaultaddress: null
            }
        default:
            return state
    }
}

export default userReducer;