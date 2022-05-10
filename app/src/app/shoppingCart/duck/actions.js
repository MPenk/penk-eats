import types from "./types";

const add = item => ({
    type: types.ADD_FOOD, item
})

const remove = item => ({
    type: types.REMOVE_FOOD, item
})

const clear = () => ({
    type: types.CLEAR_FOOD
})

const reduce = item =>({
    type: types.REDUCE_FOOD, item
})
const addInfo = item =>({
    type: types.ADD_INFO_FOOD, item
})

export default{
    add,
    remove,
    clear,
    reduce,
    addInfo
}