import React from 'react';
import Head from 'next/head';
import SliderCarousel from '../components/SliderCarousel';

import HomePage from '../components/HomePage';


const Home = () => {
  return (
    <div>
      <Head>
        <title>MTG Cards Shop</title>
      </Head>

      <main>
      
        <SliderCarousel/>
        <HomePage/>
      </main>
    </div>
  );
};



export default Home;