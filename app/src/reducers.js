import { combineReducers } from 'redux';
import userReducer from './app/user/duck';
import cartReducer from './app/shoppingCart/duck';

const rootReducer = combineReducers({
    user: userReducer,
    cart: cartReducer
})

export default rootReducer;