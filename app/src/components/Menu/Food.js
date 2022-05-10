import React, {useState} from 'react';
import {Card , Button} from 'react-bootstrap'
import actions from '../../app/shoppingCart/duck/actions';
import { connect } from 'react-redux';
import { execute } from '../../api/connection';
import { Rating } from 'react-simple-star-rating'
function Food({ food, addToCart, ...props}) {

    const [rating, setRating] = useState(0);
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
    const handeAddToShoppingCart = ()=>{
        addToCart({...food, img:null});
    }
    const handeRemoveFood= async () =>{
        const result = await execute("/food", "DELETE", props.setErrorMenu, { foodid: food.foodid });
        if (result) {
                props.reloadFood();
        }
        return;
    }
    return (
        <Card className='Card-Menu Card'>
             {props.user.permissions==2 &&<div className='close red' onClick={handeRemoveFood} ></div>}
            {food.img && <Card.Img className='food-img-height' variant="top" src={`data:image/jpeg;base64,${food.img}`} />}
            <Card.Body>
                <Card.Title>{food.name}</Card.Title>
                <Rating ratingValue={food.rating?food.rating:0} readonly transition allowHalfIcon fillColorArray={fillColorArray}  />
                <Card.Subtitle className="bottom mb-2 text-muted">{food.category}</Card.Subtitle>
                <Card.Text>
                    {food.description}
                </Card.Text>
                <Card.Subtitle className="bottom mb-2 text-muted">{food.price} PLN</Card.Subtitle>
                <Button className='' variant="primary" onClick={handeAddToShoppingCart} >Dodaj do koszyka</Button>
            </Card.Body>
        </Card>
    )
}
const mapDispatchToProps = dispatch => ({
    addToCart: (food) => dispatch(actions.add(food))
});
const mapStateToProps = state => ({
    user: state.user
})
export default  connect(mapStateToProps, mapDispatchToProps)(Food);
