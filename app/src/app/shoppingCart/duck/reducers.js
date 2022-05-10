import types from "./types"

const INITIAL_STATE = { foods: localStorage.getItem("cart")?JSON.parse(localStorage.getItem("cart")).foods:[] }

const cartReducer = (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case types.ADD_FOOD:
            let food = state.foods.find(element => element.foodid === action.item.foodid);
            if (!food) {
                action.item.quantity = 1;
                action.item.additionalinformation = "";
                return {
                    ...state,
                    foods: [...state.foods, action.item]
                }
            }
            return {
                ...state,
                foods: state.foods.map(
                    (content) => content.foodid === action.item.foodid ? { ...content, quantity: (content.quantity + 1) }
                        : content
                )
            }
        case types.CLEAR_FOOD:
            return {
                foods: []
            }
        case types.REMOVE_FOOD:
            return {
                foods: state.foods.filter(item => item.foodid !== action.item.foodid)
            }
        case types.REDUCE_FOOD:
            return {
                ...state,
                foods: state.foods
                    .filter(item => !(item.foodid === action.item.foodid && item.quantity === 1))
                    .map((content) => (content.foodid === action.item.foodid ? { ...content, quantity: (content.quantity - 1) } : content))
            }

        case types.ADD_INFO_FOOD:
            return {
                ...state,
                foods: state.foods.map((content) => (
                    content.foodid === action.item.foodid ? { ...content, additionalinformation: (action.item.additionalinformation) } : content
                ))
            }
        default:
            return state
    }
}

export default cartReducer;