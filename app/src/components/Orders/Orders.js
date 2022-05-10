import React, { useState, useEffect } from 'react';
import { execute } from '../../api/connection';
import { connect } from 'react-redux';
import Order from './Order';
import ReactLoading from "react-loading";
import { Alert } from 'react-bootstrap';

function Orders(props) {
    const [isLoading, setisLoading] = useState(false);
    const [error, setError] = useState({ exist: false, message: "" });
    const [orders, setOrders] = useState([])

    useEffect(() => {
        const inter = setInterval(() =>loadOrders(true), 10000);
        return () =>  {
            clearInterval(inter);
        }
    }, [])

    const loadOrders = async (repeat) => {
        if(props.user.userid){
            !repeat&&setisLoading(true);
            const result = await execute("/user/"+props.user.userid+"/orders", "GET", setError);
                if(result){
                    setOrders(result.value);
                }}
            !repeat&& setisLoading(false);
    }
    useEffect(() => {
            loadOrders();            
    }, [props])
    return (
        <div className=''>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            {orders.slice(0).reverse().sort(function(a,b){
  return new Date(b.orderdate) - new Date(a.orderdate);
}).map(order =>
                <Order
                    key={order.orderid}
                    order = {order}
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
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Orders);
