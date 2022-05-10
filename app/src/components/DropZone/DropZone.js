import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import uploadIcon from '../../img/upload.png'


const DropZone = ({ setImage }) => {

    const toBase64 = file => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });

    const onDrop = useCallback(async acceptedFiles => {
        var base = await toBase64(acceptedFiles[0]);
        var base2 = await resizeImage(base);
        var base64 = base2.split(',')[1];
        setImage(base64);
    }, []);
    const resizeImage = async (base64Str) => {

        var img = new Image();
        img.src = base64Str;
        var canvas =  await document.createElement('canvas');
        var MAX_WIDTH = 300;
        var MAX_HEIGHT = 150;
        var width = img.width;
        var height = img.height;
  
        if (width > height) {
          if (width > MAX_WIDTH) {
            height *= MAX_WIDTH / width;
            width = MAX_WIDTH;
          }
        } else {
          if (height > MAX_HEIGHT) {
            width *= MAX_HEIGHT / height;
            height = MAX_HEIGHT;
          }
        }
        canvas.width = width;
        canvas.height = height;
        var ctx = await canvas.getContext('2d');
        await ctx.drawImage(img, 0, 0, width, height);
        return await canvas.toDataURL();
      }
    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop, multiple: false, accept: 'image/jpeg, image/png' });

    return (
        <div className='drop-container m-l-10 m-r-10' {...getRootProps()}>
            <input {...getInputProps()} />
            <img className="upload-icon" src={uploadIcon}></img>
        </div>
    )
}
export default DropZone;