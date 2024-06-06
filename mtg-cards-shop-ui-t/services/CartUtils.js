

import { toast } from 'react-toastify';


export const handleAddToCart = (product) => {
    fetch(`http://localhost:8080/api/cart/add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ productId: product.id, quantity: 1 }),
    })
    .then(response => {
      if (response.ok) {
        toast.success(`${product.name} added to cart!`, {
          position: "top-center",
          autoClose: 2000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      } else {
       
        toast.error(`Failed to add ${product.name} to cart.`, {
          position: "top-center",
          autoClose: 2000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      }
    })
    .catch(error => {
      console.error(`Error adding ${product.name} to cart:`, error);
      toast.error(`Failed to add ${product.name} to cart.`, {
        position: "top-center",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    });
};


