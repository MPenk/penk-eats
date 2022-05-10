import React, { useState, useEffect } from 'react';
import Food from './Food';
import ReactLoading from "react-loading";
import { connect } from 'react-redux';
import AdminFood from '../Admin/Food/AdminFood';
import { execute } from '../../api/connection';
import { Alert, Form } from 'react-bootstrap';
function Menu(props) {

    const [foods, setFood] = useState([]);
    const [isLoading, setisLoading] = useState(false);
    const [error, setError] = useState({ exist: false, message: "" });
    const [categories, setCategories] = useState([]);
    const [filter, setfilter] = useState(-1);

    useEffect(async () => {
        let isApiSubscribed = true;
        reloadFood(isApiSubscribed);
        return () => isApiSubscribed = false;
    }, [])
    useEffect(async () => {
        const result = await execute("/public/category", "GET", setError);
        if (result) {
            setCategories(result.value);
        }
    }, [])
    const reloadFood = async (subscribe) => {
        setisLoading(true);
        const result = await execute("/public/food", "GET", setError);
        if (result) {
            setFood(result.value);
        }
        setisLoading(false);
    }
    return (
        <>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            <Form.Group className="mb-3 flex center">
                <div className='m-r-10'>Kategoria: </div>
                            <Form.Select required
                                onChange={(event) => setfilter(event.target.value)}>
                                    <option value={-1}>Wszyskie</option>
                                {categories.map(category => (<option key={category.categoryid} value={category.name}> {category.name} </option>))}
                            </Form.Select>
                        </Form.Group>
            <div className='Flex-container Flex-wrap'>

                {props.user.permissions === "2" &&
                    <AdminFood
                        errorMenu={error}
                        setErrorMenu={setError}
                        reloadFood={reloadFood}
                    />
                }
                {foods.filter(
                food => filter!=-1 ?
                food.category===filter :
                    true).sort((a,b) => (a.category > b.category) ? 1 : ((b.category > a.category) ? -1 : 0)).map(food =>
                    <Food
                        key={food.foodid}
                        reloadFood={reloadFood}
                        setErrorMenu={setError}
                        food={food}
                    />
                )}
                {
                    isLoading &&
                    <div className=' flex center'>
                        <ReactLoading type="spinningBubbles" color="#000" />
                    </div>
                }

            </div>
        </>

    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Menu);
