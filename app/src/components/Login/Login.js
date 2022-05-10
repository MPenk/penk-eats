import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { NavLink, useNavigate } from 'react-router-dom'
import jwt from 'jwt-decode';
import actions from '../../app/user/duck/actions'
import { Alert } from 'react-bootstrap';
import { execute } from '../../api/connection';
import { useForm } from 'react-hook-form';
import config from '../../config.json'

function Login(props) {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [userData, setUserData] = useState({ username: "", password: "" });
    const [error, setError] = useState({ exist: false, message: "" });

    const onSubmit = async data => {
        const result = await execute("/login", "POST", setError, data);
        if (result) {
            localStorage.setItem('token', result.token);
            const user = jwt(result.token);
            props.setUser(user);
            navigate('/');
        }
    }
    let navigate = useNavigate();

    useEffect(() => {

        if (props.user.userid) {
            navigate("/account");
        }

    }, [props.user.userid])


    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSubmit();
        }
    }

    return (
        <div className='flex column center'>
            {
                error.exist &&
                <Alert variant='danger'>
                    {(config.ERRORS_DICTIONARY.some(el => el.key === error.message) )?config.ERRORS_DICTIONARY.find(el => el.key === error.message).value:error.message}
                </Alert>
            }
            <form onSubmit={handleSubmit(onSubmit)}  className='form'>

                <div className='m-inp-div'>
                    <div>Nazwa</div>
                    <input type="text" onKeyDown={handleKeyDown} placeholder="Wprowadź nazwę"
                        {...register("username", {
                            required: "Wymagane",
                            minLength: { value: 3, message: "Minimalna długość to 3" },
                            maxLength: { value: 30, message: "Maksymalna długość to 30" }
                        })} />
                    {errors.username && <Alert className='' variant="danger ">{errors.username.message}</Alert>}
                </div>

                <div className='m-inp-div'>
                    <div>Hasło</div>
                    <input type="password" onKeyDown={handleKeyDown} placeholder="Wprowadź hasło"
                        {...register("password", {
                            required: "Wymagane",
                        })} />
                    {errors.password && <Alert className='' variant="danger ">{errors.password.message}</Alert>}

                </div>
                <button className='btn btn-primary'>Zaloguj</button>
                <div className='m-10'>
                    <NavLink to="/register" >Rejestracja</NavLink >
                </div>
            </form>


        </div>
    )
}

const mapDispatchToProps = dispatch => ({
    setUser: (user) => dispatch(actions.set(user))
})
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, mapDispatchToProps)(Login);
