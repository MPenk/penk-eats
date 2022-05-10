import React, { useState, useEffect } from 'react';
import { execute } from '../../api/connection';
import { connect } from 'react-redux';
import config from '../../config.json'
import { NavLink, useNavigate } from 'react-router-dom';


function Account(props) {
    const [error, setError] = useState({ exist: false, message: "" });
    const [user, setUser] = useState({ ...props.user })
    let navigate = useNavigate();
    useEffect(async () => {
        if (!props.user.userid)
            setUser({ ...props.user });
        if (props.user.userid && !user.defaultaddress) {
            const result = await execute("/user/" + props.user.userid, "GET", setError);
            if (result) {
                console.log(result);
                setUser({ ...user, ...result.value[0] });
            }
        }

        return () => {

        }
    }, [user])
    return (
        <div className=''>
            <h5>
                Użytkownik: {user.name}
            </h5>

            <h5>
                Rodzaj konta: {user.permissions && config.PERMISSIONS_DICTIONARY.find(el => el.key === user.permissions).value}
            </h5>

            <h5>
                Podstawowy adres: {user.defaultaddress}
            </h5>


            {

                <div className='flex center '>
                    <h4>
                        <NavLink to="/orders" className="m-10 btn btn-primary" >Moje zamówienia</NavLink >
                    </h4>
                    {
                        user.permissions > 1 &&
                        <h4>
                            <NavLink to="/admin/users" className="m-10 btn btn-primary" >Użytkownicy</NavLink >
                        </h4>
                    }
                </div>
            }
        </div>
    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Account);
