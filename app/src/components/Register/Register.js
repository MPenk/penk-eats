import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { useNavigate } from 'react-router-dom'
import jwt from 'jwt-decode';
import actions from '../../app/user/duck/actions'
import { Alert } from 'react-bootstrap';
import { execute } from '../../api/connection';
import AutoComplete from "react-google-autocomplete";
import { usePlacesWidget } from "react-google-autocomplete";
import { useForm } from 'react-hook-form';
function Login(props) {
    const { register, handleSubmit, formState: { errors }, setValue } = useForm();

    const [address, setAddress] = useState({ address: "" });
    const [error, setError] = useState({ exist: false, message: "" });
    let navigate = useNavigate();

    const onSubmit = async data => {

        const result = await execute("/public/user", "POST", setError, { ...data, address: address.address });
        if (result) {
            navigate('/login');
        }
    }
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
    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyD5PzKMJRaga7w6ESW1R2I7javJ1kdwttw",
        componentRestrictions: { country: "pl" },
        options: {
            types: ["geocode", "establishment"],
        },
        onPlaceSelected: (place) => setAddress(place.formatted_address)
    })

    return (
        <div className='flex column center'>
            {
                error.exist &&
                <Alert variant='danger'>
                    {error.message}
                </Alert>
            }
            <form onSubmit={handleSubmit(onSubmit)} className='form'>
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
                            minLength: { value: 8, message: "Minimalna długość to 8" },
                        })} />
                    {errors.password && <Alert className='m-10' variant="danger ">{errors.password.message}</Alert>}

                </div>

                <div className='m-inp-div'>
                    <div>Address</div>
                    <input type="text" className='' onKeyDown={handleKeyDown} ref={ref} onChange={(event) => setAddress(event.target.value)} />
                </div>

                <button className='btn btn-primary' >Zarejestruj</button>

            </form>
        </div>
    )
}

const mapDispatchToProps = dispatch => ({
})
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, mapDispatchToProps)(Login);
