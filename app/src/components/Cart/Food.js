import React from 'react';
import { Card, Button, InputGroup, FormControl } from 'react-bootstrap'
import actions from '../../app/shoppingCart/duck/actions';
import { connect } from 'react-redux';
function Food({ food, removeFromCart, reduceInCart, addToCart, addInfoToCart }) {

    const handeRemoveFromShoppingCart = () => {
        removeFromCart({ foodid: food.foodid, name: food.name });
    }
    const handeReduceInShoppingCart = () => {
        reduceInCart({ foodid: food.foodid, name: food.name });
    }
    const handeAddToShoppingCart = () => {
        addToCart({ foodid: food.foodid, name: food.name });
    }

    const handleSetAddicionalInformation = (e) => {
        addInfoToCart({ ...food, additionalinformation: e.target.value });
    }
    return (
        <Card className='Card-Cart '>
            <Card.Body >
                <div className='flex w-100 flex-wrap  center'>
                    <div className='card-text flex-1  flex center '>
                        <div className='m-10'>
                            <Card.Title  >{food.name} x {food.quantity}</Card.Title>
                            <Card.Subtitle className="bottom mb-2 text-muted">{Number((food.price * food.quantity).toFixed(2))}PLN</Card.Subtitle>
                        </div>
                        <div className='m-10 w-100 '>
                            Dodatkowe informacje
                            <InputGroup className='w-100'>
                                <FormControl onChange={handleSetAddicionalInformation} className='w-100' as="textarea" value={food.additionalinformation} />
                            </InputGroup>
                        </div>
                    </div>
                    <div className='cart-buttons  m-l-10 flex center'>
                        <Button variant='warning' className='m-2' onClick={handeReduceInShoppingCart} >-1</Button>
                        <Button variant='success' className='m-2 m-10' onClick={handeAddToShoppingCart} >+1</Button>
                        <Button variant='danger' className='m-2' onClick={handeRemoveFromShoppingCart} >Usuń</Button>
                    </div>
                </div>
            </Card.Body>
        </Card>
    )
}
const mapDispatchToProps = dispatch => ({
    removeFromCart: (food) => dispatch(actions.remove(food)),
    reduceInCart: (food) => dispatch(actions.reduce(food)),
    addToCart: (food) => dispatch(actions.add(food)),
    addInfoToCart: (food) => dispatch(actions.addInfo(food))

})

export default connect(null, mapDispatchToProps)(Food);
