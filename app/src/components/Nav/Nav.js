import React from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';

function Nav(props) {
    const { user } = props;
    return (
        <>
            <ul>
                <li>
                    <NavLink to="/" className="Nav-el">Strona główna</NavLink >
                </li>
                <li>
                    <NavLink to="/menu" className="Nav-el" >Menu</NavLink >
                </li>
                {user.isLogged &&
                    <li>
                        <NavLink to={user.permissions>0?"/active":"/orders"} className="Nav-el" >Zamówienia</NavLink >
                    </li>
                }
                {(user.isLogged && user.permissions ==2)&&
                    <li>
                        <NavLink to="/categories" className="Nav-el" >Kategorie</NavLink >
                    </li>
                }
                {user.isLogged ?
                    <li >
                      <NavLink to="/account" className="Nav-el" > {user.name}, </NavLink ><NavLink to="/logout" className="Nav-el" > wyloguj</NavLink >
                    </li>
                    :
                    <li>
                        <NavLink to="/login" className="Nav-el" >Zaloguj</NavLink >
                    </li>
                }

            </ul>
        </>

    )
}
const mapStateToProps = state => ({
    user: state.user
})
export default connect(mapStateToProps, {})(Nav);