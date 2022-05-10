import React, { useState, useEffect } from 'react';
import { execute } from '../../api/connection';
import { connect } from 'react-redux';
import { useParams } from 'react-router-dom';
import config from '../../config.json'
import OrderElement from './OrderElement';
import { Alert, Button, Card } from 'react-bootstrap';


function Order(props) {
    const [error, setError] = useState({ exist: false, message: "" });
    const [user, setUser] = useState(props.user)
    const [elements, setElements] = useState([])
    const [order, setOrder] = useState([])
    const params = useParams();
    useEffect(async () => {
        downloadOrder();
    }, [props.user.userid])

    useEffect(() => {
        const inter = setInterval(() => downloadOrder(), 10000);

        return () => {
            clearInterval(inter);
        }
    }, [])
    const downloadOrder = async (repeat) => {
        if (props.user.userid) {
            const result = await execute("/order/" + params.id, "GET", setError);
            if (result) {
                setElements(result.value);
                setOrder(result.add[0]);
            }
        }
    }

    const handleCancleOrder = async () => {
        const result = await execute("/order/" + params.id + "/cancel", "POST", setError);
        if (result) {
            downloadOrder();
        }
    }
    const handleMoveOrder = async () => {
        const result = await execute("/order/" + params.id + "/move", "POST", setError);
        if (result) {
            downloadOrder();
        }
    }
    return (
        <div className=''>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            {
                !!order ?
                    <Card className='Card-Cart   '>
                        <Card.Body className='flex w-100'>
                            <Card.Title>Zamówienie z dnia {order.orderdate}</Card.Title>
                            <Card.Text>
                                Cena: {order.sumprice} PLN
                            </Card.Text>
                            <Card.Text>
                                Adres: {order.address}
                            </Card.Text>
                            <Card.Subtitle className="bottom mb-2 w-100 text-muted">Status: {order.status && config.STATUS_DICTIONARY.find(el => el.key === order.status).value} </Card.Subtitle>
                            {elements.map(orderElement =>
                                <OrderElement
                                    key={orderElement.orderelementid}
                                    orderElement={orderElement}
                                status = {order.status}
                                />
                            )}

                            <div className='flex'>
                                {props.user.permissions > 0 && (order.status >= 0 && order.status < 3) && <Button variant='primary' className='m-r-10' onClick={handleMoveOrder} >Przesuń do następnego kroku</Button>}
                                {(order.status >= 0 && order.status < 3) && <Button variant='danger' className=' m-l-10' onClick={handleCancleOrder} >Anuluj zamówienie</Button>}
                            </div>
                        </Card.Body>
                    </Card> :
                    <h2>Brak uprawnień</h2>
            }
        </div>
    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Order);
