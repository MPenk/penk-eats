import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';

function ShoppingCart(props) {
    const [visible, setVisible] = useState(true)
    let location = useLocation();
    let navigate = useNavigate();

    useEffect(() => {
        if (location.pathname == "/cart" || props.cart.foods.length<1)
            setVisible(false);
        else
            setVisible(true);
    }, [location, props.cart.foods.length]);
    const handleClickOrder=()=>{
        navigate("/cart");
    }
    return (
        <>
            {visible &&
                <div onClick={handleClickOrder} className='Shopping-cart pointer'>
                    <div className='number-circle'><div>{props.cart.foods.length}</div></div>
                    Koszyk
                </div>
            }
        </>
    )
}
const mapStateToProps = state => ({
    cart: state.cart
})
export default connect(mapStateToProps, {})(ShoppingCart);
