import { applyMiddleware, createStore } from "redux";
import rootReducer from "./reducers";
import {composeWithDevTools} from 'redux-devtools-extension'
import thunk from 'redux-thunk'

const store = createStore(rootReducer, composeWithDevTools(applyMiddleware(thunk)))
store.subscribe(()=>{
    localStorage.setItem('cart', JSON.stringify(store.getState().cart))
})
export default store;