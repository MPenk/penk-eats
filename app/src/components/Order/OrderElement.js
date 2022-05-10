import React, { useState } from 'react';
import { Card, Button, InputGroup, FormControl } from 'react-bootstrap'
import { connect } from 'react-redux';
import { Rating } from 'react-simple-star-rating';
import { execute } from '../../api/connection';

function OrderElement(props) {

    const { orderElement } = props;
    const [rating, setRating] = useState(orderElement.rating);
    const [rated, setRated] = useState(false);
    const [error, setError] = useState();
    const fillColorArray = [
        '#f17a45',
        '#f17a45',
        '#f19745',
        '#f19745',
        '#f1a545',
        '#f1a545',
        '#f1b345',
        '#f1b345',
        '#f1d045',
        '#f1d045'
    ]

    const handleRating = async (rate) => {
        const result = await execute("/feedback", "POST", setError, {orderelementid:orderElement.orderelementid, foodid: orderElement.foodid, rating: rate });
        setRating(rate);
        if (result) {
            console.log("Dzieki za ocenę")
            setRated(true);
            setRating(rate);
        }

    }
    return (
        <Card className='Card-Cart w-100 '>
            <Card.Body >
                <div className='flex w-100 flex-wrap flex-space'>
                    <div className='card-text w-100  flex '>
                        <div className='m-r-10'>
                            <Card.Title  >{orderElement.foodname} x {orderElement.quantity}</Card.Title>
                            <Card.Subtitle className="bottom mb-2 text-muted">{orderElement.price * orderElement.quantity}PLN</Card.Subtitle>
                        </div>
                        <div className='m-l-10 w-100 '>
                            Dodatkowe informacje
                            <InputGroup className='w-100'>
                                <FormControl disabled className='w-100' as="textarea" value={orderElement.additionalinformation} />
                            </InputGroup>
                        </div>
                    </div>
                    <div className='flex center w-100'>
                        {(props.status>2 && props.user.permissions==0 ) && <Rating readonly={rated?true:(orderElement.rating?true:false)} onClick={handleRating} ratingValue={rating} transition allowHalfIcon fillColorArray={fillColorArray} />}
                    </div>
                </div>
            </Card.Body>
        </Card>
    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, [])(OrderElement);
