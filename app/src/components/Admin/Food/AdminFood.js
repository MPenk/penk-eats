
import React, { useCallback, useState, useEffect, useRef } from 'react'
import DropZone from '../../DropZone/DropZone';
import { Card, Button } from 'react-bootstrap'
import { execute } from '../../../api/connection';
import { connect } from 'react-redux';
import { Alert } from 'react-bootstrap';
import { confirmAlert } from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css'
import { useForm } from 'react-hook-form';
function AdminFood(props) {
    const { register, handleSubmit, formState: { errors }, setValue ,reset} = useForm();
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState({ exist: false, message: "" });
    const [image, setImage] = useState();

    const onSubmit = async (data, obj, force) => {
        const result = await execute(force ? "/food?force=true" : "/food", "POST", setError, { ...data, price: parseFloat(data.price),categoryid:parseInt(data.categoryid), img: image ? image : "" });
        if (result) {
            if ('options' in result) {
                if ('force' in result.options) {
                    alertForce();
                }
            } else {
                props.reloadFood();
                reset({
                    name:"",
                    description:"",
                    price:0,
                    categoryid: categories.length > 0 ? categories[0].categoryid : ""
                  });
                  handeRemoveImage();
            }

        }
        return;
    }

    useEffect(async () => {
        const result = await execute("/public/category", "GET", setError);
        if (result) {
            setCategories(result.value);
            setValue("categoryid", result.value.length > 0 ?  result.value[0].categoryid : "")
        }

    }, [])


    const handeRemoveImage = () => {
        setImage(null);
    }

    const handleSetImage = image => {
        setImage(image);
    }
    const alertForce = () => {
        confirmAlert({
            title: 'Jedzenie o takiej nazwie już istnieje',
            message: 'Czy na pewno chcesz dodać jedzenie o takiej nazwie?',
            closeOnEscape: true,
            closeOnClickOutside: true,
            buttons: [
                {
                    label: 'Dodaj',
                    className: "btn-green",
                    onClick: () => handleSubmit((data, e) => onSubmit(data, e, true))()
                },
                {
                    label: 'Anuluj'
                }
            ],
        })
    };


    return (
        <>
            <Card className='Card-Menu Card'>
                {
                    error.exist &&
                    <Alert variant='danger'>
                        {error.message}
                    </Alert>
                }
                <div className='food-img-height'>
                    {image ?
                        <div >
                            <div className=' m-l-10 m-r-10'><div className='close' onClick={handeRemoveImage} ></div></div>
                            <Card.Img className='food-img-height' variant="top" src={`data:image/jpeg;base64,${image}`} />
                        </div>
                        :
                        <DropZone setImage={handleSetImage} />
                    }
                </div>
                <form onSubmit={handleSubmit(onSubmit)} >
                    <Card.Body>
                        <Card.Title>
                            <input type="text" placeholder="Nazwa"
                                {...register("name", {
                                    required: "Wymagane",
                                    minLength: { value: 3, message: "Minimalna długość to 3" },
                                    maxLength: { value: 30, message: "Maksymalna długość to 30" }
                                })} />
                            {errors.name && <Alert className='' variant="danger ">{errors.name.message}</Alert>}

                        </Card.Title>
                        <div className="m-10 w-100">
                            <select
                                {...register("categoryid", {
                                })}>
                                {categories.map(category => (<option key={category.categoryid} value={category.categoryid}> {category.name} </option>))}
                            </select>
                        </div>
                        <Card.Text>
                            <textarea className='w-100' as="textarea" placeholder="Opis"

                                {...register("description", {
                                })}
                            />

                        </Card.Text>
                        <Card.Subtitle className="bottom mb-2 text-muted flex center flex-row">
                            <input min="0.99" type="text" placeholder="Cena" className='price-input'
                                type="number" step="0.01"
                                {...register("price", {
                                    required: "Wymagane",
                                    min: { value: 0.98, message: "Minimalna cana to 0.98 PLN" }
                                })} />

                            <div className='m-l-10 flex'>PLN</div>
                        </Card.Subtitle>
                        {errors.price && <Alert className='' variant="danger ">{errors.price.message}</Alert>}

                        <button className='btn btn-primary' >Dodaj</button>
                    </Card.Body>
                </form>
            </Card >
        </>
    )
}
const mapDispatchToProps = dispatch => ({
})
export default connect(null, mapDispatchToProps)(AdminFood);
