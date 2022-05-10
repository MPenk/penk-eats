import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { useNavigate } from 'react-router-dom'
import actions from '../../app/user/duck/actions'


function Logout(props) {


    let navigate = useNavigate();

    useEffect(() => {
        setTimeout(() => {
            localStorage.removeItem("token");
            props.clearUser();
            navigate('/');
        }, 1000);
    }, [])

    return (
        <div className='Flex-container center'>
            Wylogowywanie...
        </div>
    )
}

const mapDispatchToProps = dispatch => ({
    clearUser: () => dispatch(actions.clear())
})
export default connect(null, mapDispatchToProps)(Logout);
