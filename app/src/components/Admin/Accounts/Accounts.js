import React, { useState, useEffect } from 'react';
import { execute } from '../../../api/connection';
import { connect } from 'react-redux';
import { useParams } from 'react-router-dom';
import config from '../../../config.json'
import OrderElement from '../../Order/OrderElement';
import { Button, Card } from 'react-bootstrap';

import icoMagnifying from '../../../img/ico-magnifying.svg';

function Acconuts(props) {
    const [error, setError] = useState({ exist: false, message: "" });
    const [user, setUser] = useState(props.user)
    const [search, setSearch] = useState("");
    const [users, setUsers] = useState([])
    const params = useParams();
    useEffect(() => {
        loadUsers()
    }, [props.user.userid])

    const handleAddPermission = async (id) => {
        const result = await execute("/user/" + id + "/promote", "POST", setError);
        if (result) {
            loadUsers();
        }
    }
    const handleRemovePermission = async (id) => {
        const result = await execute("/user/" + id + "/degrade", "POST", setError);
        if (result) {
            loadUsers();
        }
    }
    const loadUsers = async () => {
        if (props.user.userid) {
            const result = await execute("/user/all", "GET", setError);
            if (result) {
                setUsers(result.value);
            }
        }
    }

    return (
        <div className='flex column'>
            <div className='m-l-a search flex flex-row'>
                <input className='' onChange={(event) => setSearch(event.target.value)} type="text" placeholder='Wyszukiwanie' />
                <img className='ico-mglass' src={icoMagnifying} />
            </div>
            {users.filter(
                user => search ?
                    user.name.toLowerCase().includes(search.toLowerCase()) :
                    true).map(user =>
                        <Card key={user.userid} className='Card-Cart  '>
                            <Card.Body >
                                <h5>
                                    Użytkownik: {user.name}
                                </h5>

                                <h5>
                                    Rodzaj konta: {user.permissions && config.PERMISSIONS_DICTIONARY.find(el => el.key === user.permissions).value}
                                </h5>
                                {props.user.permissions > 1 && (user.permissions >= 0 && user.permissions < 2) && <Button variant='primary' className='m-10' onClick={() => handleAddPermission(user.userid)} >Awansuj</Button>}
                                {props.user.permissions > 1 && (user.permissions == 1) && <Button variant='danger' className='m-10' onClick={() => handleRemovePermission(user.userid)} >Degraduj</Button>}
                            </Card.Body>
                        </Card>
                    )}
        </div>
    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Acconuts);
