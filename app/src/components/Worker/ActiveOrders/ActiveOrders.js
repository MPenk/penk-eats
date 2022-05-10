import React, { useState, useEffect } from 'react';
import { execute } from '../../../api/connection';
import { connect } from 'react-redux';
import Order from '../../Orders/Order';
import ReactLoading from "react-loading";
import { Alert } from 'react-bootstrap';

function Orders(props) {
    const [isLoading, setisLoading] = useState(false);
    const [error, setError] = useState({ exist: false, message: "" });
    const [orders, setOrders] = useState([])

    useEffect(() => {
        console.log("Start interval")
        const inter = setInterval(() =>loadOrders(), 5000);

        return () =>  {
            clearInterval(inter);
        }
    }, [])
    useEffect(async () => {
        loadOrders();
    }, [props])


    const loadOrders = async () =>{
        if (props.user.userid) {
            setisLoading(true);
            const result = await execute("/order/active", "GET", setError);
            if (result) {
                setOrders(result.value);
            }
        }
        setisLoading(false);
    }
   if(props.user.permissions>0){
    return (
        <div className=''>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            {
                orders.length == 0 &&
                <h1>Brak aktywnych zamówień</h1>
            }
            {orders.slice(0).reverse().map(order =>
                <Order
                    key={order.orderid}
                    order={order}
                />
            )}
            {
                isLoading &&
                <div className=' flex center'>
                    <ReactLoading type="spinningBubbles" color="#000" />
                </div>
            }
        </div>
    )
   }
   else{
       return(
           <h2>Brak uprawnień</h2>
       )
   }
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Orders);
