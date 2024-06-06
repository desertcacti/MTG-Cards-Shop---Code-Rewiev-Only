

import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useRouter } from 'next/router';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { handleAddToCart } from '../../services/CartUtils';

const ColorPage = () => {
  const [cards, setCards] = useState([]);
  const router = useRouter();
  const { color } = router.query;

  const colorMap = {
    green: '#38c172',
    white: '#ffffff',
    black: '#000000',
    red: '#e3342f',
    blue: '#3490dc',
  };

  // Funkcja do pobierania obrazów z Google Drive (zakładamy dostępny endpoint)
  const getImageUrl = (imageName) => {
    return `http://localhost:8080/api/media/${imageName}`;
  }

  useEffect(() => {
    if (color) {
      fetch(`http://localhost:8080/api/products/type/${color}`)
        .then(response => response.json())
        .then(data => setCards(data))
        .catch(error => console.error(`Error fetching data for ${color} cards:`, error));
    }
  }, [color]);

  const getButtonStyle = (color) => {
    return color === 'white'
      ? {
          backgroundColor: '#ffffff',
          color: '#000000',
          padding: '8px 16px',
          borderRadius: '4px',
          fontWeight: 'bold',
          border: '1px solid #000000',
        }
      : {
          backgroundColor: colorMap[color],
          color: '#ffffff',
          padding: '8px 16px',
          borderRadius: '4px',
          fontWeight: 'bold',
        };
  };

  return (
    <div className="flex flex-col items-start ">

      {/* Color title and small symbol */}
      <div className="mt-3 mb-5 custom-width-79 border-b-2 mx-auto flex items-center" 
      style={{ borderColor: colorMap[color] }}>
        
        <p className="text-xl silkscreen-bold font-bold ml-4 mt-2 mr-3 mb-2" 
        style={{ color: colorMap[color] }}>
          {color.charAt(0).toUpperCase() + color.slice(1)}</p>
        
          {/* Small symbol image */}
        <div className="rounded-full overflow-hidden">
          <Image
            src={getImageUrl(`small${color.charAt(0).toUpperCase() + color.slice(1)}Symbol.png`)}
            height={20}
            width={20}
            alt={`${color} symbol`}
            unoptimized
          />
        </div>
      </div>

      <div className="flex ml-40 ">
        {/* Large Symbol pic */}
        <div className="">
          <Image
            src={getImageUrl(`${color.charAt(0).toUpperCase() + color.slice(1)}Symbol.png`)}
            height={250}
            width={250}
            alt={`${color} symbol large`}
            unoptimized
          />
        </div>
         {/* Vertical line in color */}
        <div className="ml-9">
          <div style={{ backgroundColor: colorMap[color], width: '2px', height: '100%', position: 'absolute' }}></div>
        </div>

         {/* Grid with products*/}
        <div className="grid grid-cols-3 gap-4 ml-12 ">
          {cards.map(card => (
            <div key={card.id} className="flex flex-col items-center p-4 mb-4 w-64" style={{ borderColor: colorMap[color], borderWidth: '2px', borderStyle: 'solid' }}>
              
              <div className="overflow-hidden w-full h-80 mb-3">
                <Image
                  src={`http://localhost:8080/api/products/${card.id}/image`}
                  alt={`Image of ${card.name}`}
                  width={250}
                  height={250}
                  objectFit="contain"
                  unoptimized
                />
              </div>

              <h3 className="text-lg font-semibold mb-1 silkscreen-regular " style={{ color: colorMap[color] }}>{card.name}</h3>
              <p className="text-gray-600 mb-2 silkscreen-regular">{`${card.price.toFixed(2)} PLN`}</p>
              <button 
                className='silkscreen-regular'
                onClick={() => handleAddToCart(card)}
                style={getButtonStyle(color)}
              >
                Add to Cart
              </button>
            </div>
          ))}
        </div>

      </div>

      <ToastContainer />
    </div>
  );
};

export default ColorPage;
