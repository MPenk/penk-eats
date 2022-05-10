import React, {useState} from 'react';
import Food from './Food';
import { connect } from 'react-redux';
import { Alert, Button, Form } from 'react-bootstrap'
import {execute} from '../../api/connection';
import { useNavigate } from 'react-router-dom';
import actions from '../../app/shoppingCart/duck/actions';
import { usePlacesWidget } from "react-google-autocomplete";
function Cart(props) {

    const [address, setAddress] = useState(props.user.defaultaddress);
    const [error, setError] = useState({ exist: false, message: "" });
    let navigate = useNavigate();
    const handlePlaceOrder = async () =>{
        setError({ exist: false, message: "" });
        if(!props.user.isLogged){
            setError({exist: true, message:"Musisz być zalogowany aby móc złożyć zamówienie"});
            return;
        }
        const result = await execute("/order", "POST", setError, {address:address, elements:props.cart.foods});
        if(result){
            localStorage.removeItem('cart');
            props.clearCart();
            navigate("/orders");
        }
    }
    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyD5PzKMJRaga7w6ESW1R2I7javJ1kdwttw",
          componentRestrictions:{country: "pl" },
          options:{
            types: ["geocode", "establishment"],
          },
        onPlaceSelected: (place) => setAddress(place.formatted_address)
      })

    if (props.cart.foods.length < 1)
        return (
            <div>
                Koszyk jest pusty
            </div>
        )
    else
        return (
            <div className=''>
                            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
                {props.cart.foods.map(food =>
                    <Food
                        key={food.foodid}
                        food={food}
                    />
                )}
                <Form.Label>Adres dostawy</Form.Label>
                <input ref={ref} type="text" placeholder="Wprowadź adres" className='form-control m-b-10 ' onChange={(event) => setAddress(event.target.value)} value={address} />
                <Button variant='primary' className='' onClick={handlePlaceOrder} >Złóż zamówienie</Button>
            </div>
        )
}
const mapDispatchToProps = dispatch => ({
    clearCart: () => dispatch(actions.clear())
})

const mapStateToProps = state => ({
    cart: state.cart,
    user: state.user
})
export default connect(mapStateToProps, mapDispatchToProps)(Cart);
