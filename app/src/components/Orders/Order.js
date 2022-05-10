import React from 'react';
import { useNavigate } from 'react-router-dom';
import {Card , Button} from 'react-bootstrap'
import actions from '../../app/shoppingCart/duck/actions';
import { connect } from 'react-redux';
import config from '../../config.json'

function Order({order}) {

    let navigate = useNavigate();

    const handleClickOrder=()=>{
        navigate("/order/"+order.orderid);
    }

    return (
        <Card onClick={handleClickOrder} className='Card-Cart Card pointer '>
            <Card.Body>
                <Card.Title>Zamówienie z dnia {order.orderdate}</Card.Title>
                <Card.Text>
                   Cena: {order.sumprice} PLN
                </Card.Text>
                <Card.Text>
                                Adres: {order.address}
                            </Card.Text>
                <Card.Subtitle className="bottom mb-2 text-muted">Status: {config.STATUS_DICTIONARY.find(el=> el.key === order.status).value} </Card.Subtitle>
            </Card.Body>
        </Card>
    )
}

export default  Order;
