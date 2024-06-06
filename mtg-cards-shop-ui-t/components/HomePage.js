
import React, { useState, useEffect } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaArrowUp, FaArrowDown } from 'react-icons/fa';
import { handleAddToCart } from '../services/CartUtils';
import { useRouter } from 'next/router';


const HomePage = () => {
  const router = useRouter();
  const { confirmation } = router.query; // Używaj useRouter do odczytywania parametrów z URL

  const [products, setProducts] = useState([]);
  const [sortOrder, setSortOrder] = useState('');
  

  useEffect(() => {
    // Wyświetl odpowiedni komunikat w zależności od parametru
    if (confirmation === 'success') {
      toast.success('Konto zostało poprawnie potwierdzone i możesz się teraz zalogować.');
    } else if (confirmation === 'failed') {
      toast.error('Wystąpił błąd podczas potwierdzania konta.');
    }

    let url = 'http://localhost:8080/api/products';
    if (sortOrder !== '') {
      url += `?sort=${sortOrder}`;
    }

    fetch(url)
      .then(response => response.json())
      .then(data => {
        const sortedData = sortOrder === 'desc' 
          ? data.sort((a, b) => b.price - a.price) 
          : data.sort((a, b) => a.price - b.price);

        setProducts(sortedData.slice(0, 15));
      })
      .catch(error => {
        console.error('Błąd pobierania danych:', error);
        toast.error('Problem z ładowaniem produktów.');
      });
  }, [sortOrder, confirmation]); 

  return (
    <div className="flex flex-col items-center"> {/* Kontener główny */}

      {/* Kontrolki do sortowania */}
      <div className="mb-6 mt-2">
        <button 
          onClick={() => setSortOrder('asc')} 
          className="bg-gray-800 hover:bg-blue-700 text-customyellow font-bold py-2 px-4 mx-2 rounded focus:outline-none focus:shadow-outline"
        >
          <FaArrowUp />
        </button>
        <button 
          onClick={() => setSortOrder('desc')} 
          className="bg-gray-800 hover:bg-blue-700 text-customyellow  font-bold py-2 px-4 mx-2 rounded focus:outline-none focus:shadow-outline"
        >
          <FaArrowDown />
        </button>
      </div>

      {/* Wyświetlanie produktów */}
      <div className="flex flex-wrap justify-center"> {/* Kontener na produkty */}
        {products.map(product => (
          <div key={product.id} className="flex flex-col items-center p-4 border-2 border-yellow-500 mb-4 mx-2 w-64">
            <div className="overflow-hidden w-full h-80 mb-3">
              <img
                src={`http://localhost:8080/api/products/${product.id}/image`}
                alt={`Product picture ${product.name}`}
                className="object-cover w-full h-full"
              />
            </div>
            <h3 className="text-lg font-semibold mb-1 topbar-text-yellow silkscreen-regular">{product.name}</h3>

            <p className="text-gray-600 mb-2 silkscreen-regular ">
              {`${product.price.toFixed(2)} PLN`}
            </p>
            <button 
              onClick={() => handleAddToCart(product)} 
              className="bg-gray-800 hover:bg-blue-700 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline silkscreen-regular text-customyellow"
            >
              Add to Cart
            </button>
          </div>
        ))}
      </div>
      
      <ToastContainer />
    </div>  
    
  );
};

export default HomePage;


