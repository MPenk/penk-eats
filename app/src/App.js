import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import React, { useState, useEffect } from 'react';
import { BrowserRouter, Route, Routes,Navigate } from 'react-router-dom'
import { Menu } from './components/Menu';
import { Home } from './components/Home';
import { Nav } from './components/Nav';
import ShoppingCart from './components/Cart/ShoppingCart';
import NotFound from './components/NotFound/NotFound';
import Login from './components/Login/Login';
import jwt from 'jwt-decode';
import actions  from './app/user/duck/actions'
import { connect } from 'react-redux';
import Logout from './components/Logout/Logout';
import Cart from './components/Cart/Cart';
import Orders from './components/Orders/Orders';
import Account from './components/Account/Account';
import Order from './components/Order/Order';
import Categories from './components/Categories/Categories';
import Register from './components/Register/Register';
import AllOrders from './components/Admin/Orders/AllOrders';
import ActiveOrders from './components/Worker/ActiveOrders/ActiveOrders';
import Accounts from './components/Admin/Accounts/Accounts';

function App(props) {

  useEffect(() => {
    if(localStorage.getItem("token")!=null){
      const user = jwt(localStorage.getItem("token"));
      props.setUser(user);
    }

  },[])

  return (
    <div className="App heightMin flex column">
      <div>
        {/*<BrowserRouter basename={"/~s46362/projekt_java/app"}> */}
        {/*<BrowserRouter basename={"/~s46362/projekt/app"}> */}
        <BrowserRouter basename={"/~s46362/projekt_java/app"}>
        <header className="App-header ">
          <Nav />
        </header>
        <div  style={{ margin: '20px' }}>
        <Routes >
          <Route path='/' element={<Home />} exact />
          <Route path='/menu' element={<Menu />} />
          <Route path='/cart' element={<Cart />} />
          <Route path='/categories' element={<Categories />} />
          <Route path='/orders' element={<Orders />} />
          <Route path='/admin/orders' element={<AllOrders />} />
          <Route path='/admin/users' element={<Accounts />} />
          <Route path='/active' element={<ActiveOrders />} />
          <Route path='/account' element={<Account />} />
          <Route path='/order/:id' element={<Order />} />
          <Route path='/logout' element={<Logout />} />
          <Route path='/login' element={<Login/>} />
          <Route path='/register' element={<Register/>} />
          <Route path='/404' element={<NotFound/>} />
          <Route path="*"element={<Navigate to="/404" />}/>
        </Routes >
        </div>
        <ShoppingCart/>
      </BrowserRouter>
      </div>
      <div className='footer bottom'></div>

    </div>
    
  );
}

const mapDispatchToProps = dispatch => ({
  setUser: (user) => dispatch(actions.set(user))
})
export default  connect(null, mapDispatchToProps)(App);
