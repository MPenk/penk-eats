import React, { useState, useEffect } from 'react';
import { execute } from '../../api/connection';
import { connect } from 'react-redux';
import ReactLoading from "react-loading";
import config from '../../config.json'
import { Alert, Button, Card, Form } from 'react-bootstrap';
import icoMagnifying from '../../img/ico-magnifying.svg';
import icoUndo from '../../img/ico-undo.svg';

function Categories(props) {
    const [isLoading, setisLoading] = useState(false);
    const [error, setError] = useState({ exist: false, message: "" });
    const [search, setSearch] = useState("");
    const [categories, setCategories] = useState([]);
    const [newCategory, setnewCategory] = useState({ name: "" });
    useEffect(async () => {
        reloadCategories();
    }, [])

    const handeRemoveCategory = async (id) => {
        const result = await execute("/category", "DELETE", setError, { categoryid: id });
        if (result) {
            reloadCategories();
        }
        return;
    }
    const handeRestoreCategory = async (id) => {
        const result = await execute("/category/restore", "POST", setError, { categoryid: id });
        if (result) {
            reloadCategories();
        }
        return;
    }
    const handeAddToCategories = async (event) => {
        if (search.length < 3)
            return;

        const result = await execute("/category", "POST", setError, { name: search });
        if (result) {

            reloadCategories();
            setnewCategory({ name: "" });
        }
        return;
    }
    const reloadCategories = async (subscribe) => {
        setisLoading(true);
        const result = await execute("/category", "GET", setError);
        if (result) {
            setCategories(result.value);
            console.log(result)
        }
        setisLoading(false);
    }
    return (
        <div className=''>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            <div className='m-l-a search flex flex-row'>
                <input className='' onChange={(event) => setSearch(event.target.value)} type="text" placeholder='Wyszukiwanie' />
                <img className='ico-mglass' src={icoMagnifying} />
            </div>
            {(props.user.isLogged && props.user.permissions == 2) &&

                <Button className='m-t-10' variant="primary" onClick={handeAddToCategories} >Dodaj</Button>
            }
            {categories.filter(
                category => search ?
                    category.name.toLowerCase().includes(search.toLowerCase()) :
                    true).sort((a, b) => (a.deleted == true && b.deleted != false) ? -1 : ((a.deleted != false && b.deleted == true) ? 1 : 0)).slice(0).reverse().map(category =>
                        <Card key={category.categoryid} className={category.deleted != true ? 'Card-Cart Card' : 'off Card-Cart Card'}>
                            <Card.Body>
                                {props.user.permissions == 2 &&
                                    category.deleted ?
                                    <img className='ico-mglass opacity1' onClick={() => handeRestoreCategory(category.categoryid)} src={icoUndo} />:
                                    <div className='close red' onClick={() => handeRemoveCategory(category.categoryid)} ></div>

                                }
                                <Card.Title>{category.name}</Card.Title>
                            </Card.Body>
                        </Card>
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
export default connect(mapStateToProps, {})(Categories);
