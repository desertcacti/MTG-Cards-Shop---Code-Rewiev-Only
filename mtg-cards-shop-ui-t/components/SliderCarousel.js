

import React, { useEffect, useState } from 'react';
import Slider from 'react-slick';
import axios from 'axios';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import '/styles/SliderCarousel.css';
import Image from "next/image";
import { BiSolidRightArrow, BiSolidLeftArrow } from "react-icons/bi";

const SliderCarousel = () => {
  const [images, setImages] = useState([]);

  useEffect(() => {
    const imageNames = ['slide1.png', 'slide2.png', 'slide3.png', 'slide4.png', 'slide5.png'];
    const fetchImages = async () => {
      try {
        const imageUrls = await Promise.all(
          imageNames.map(name =>
            axios.get(`http://localhost:8080/api/media/${name}`, { responseType: 'blob' })
              .then(response => URL.createObjectURL(response.data))
              .catch(error => {
                console.error('Error fetching image:', error);
                return ''; // Zwróć pusty string lub domyślny obraz w przypadku błędu
              })
          )
        );
        setImages(imageUrls);
      } catch (error) {
        console.error('Error in fetching images:', error);
      }
    };

    fetchImages();
  }, []);

  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    nextArrow: <BiSolidRightArrow />,
    prevArrow: <BiSolidLeftArrow />
  };

  return (
    <div className="slider-container ">
      <Slider {...settings}>
        {images.map((src, index) => (
          <div key={index}>
            <Image
              src={src} // Użyj URL stworzonego z obiektu Blob
              alt={`Slide ${index + 1}`}
              width={800}
              height={400}
              layout="responsive"
              unoptimized
            />
          </div>
        ))}
      </Slider>
    </div>
  );
};

export default SliderCarousel;