import React, { useState, useEffect } from 'react';
import { execute } from '../../../api/connection';
import { connect } from 'react-redux';
import { useParams } from 'react-router-dom';
import config from '../../../config.json'
import OrderElement from '../../Order/OrderElement';


function AllOrders(props) {
    const [error, setError] = useState({ exist: false, message: "" });
    const [user, setUser] = useState(props.user)
    const [order, setOrder] = useState([])
    const params = useParams();
    useEffect(async () => {
        if (props.user.userid) {
            const result = await execute("/order", "GET", setError);
            if (result) {
                setOrder(result.value);
            }
        }

        return () => {

        }
    }, [props.user.userid])
    return (
        <div className=''>
            {order.map(orderElement =>
                    <OrderElement
                        key={orderElement.orderelementid}
                        orderElement = {orderElement}
                    />
                )}
        </div>
    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(AllOrders);
