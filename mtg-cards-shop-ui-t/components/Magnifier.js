// import React, { useState } from 'react';

// const Magnifier = ({ src, alt }) => {
//   const [zoom, setZoom] = useState(false);
//   const [position, setPosition] = useState({ x: 0, y: 0 });

//   const handleMouseEnter = () => {
//     setZoom(true);
//   };

//   const handleMouseLeave = () => {
//     setZoom(false);
//   };

//   const handleMouseMove = (e) => {
//     const { left, top, width, height } = e.target.getBoundingClientRect();
//     const x = ((e.pageX - left) / width) * 120; // wartość 100% szerokości obrazka
//     const y = ((e.pageY - top) / height) * 20; // wartość 100% wysokości obrazka
//     setPosition({ x, y });
// };

//   return (
//     <div
//       style={{
//         width: '100%',
//         height: '100%',
//         overflow: 'hidden',
//         position: 'relative',
//         cursor: 'zoom-in'
//       }}
//       onMouseEnter={handleMouseEnter}
//       onMouseMove={handleMouseMove}
//       onMouseLeave={handleMouseLeave}
//     >
//       <img
//         src={src}
//         alt={alt}
//         style={{
//           width: '100%',
//           height: '100%',
//           transition: 'transform .1s ease-out',
//           transform: `${zoom ? 'scale(2)' : 'scale(1)'}`,
//           transformOrigin: `${position.x}% ${position.y}%`
//         }}
//       />
//     </div>
//   );
// };

// export default Magnifier;

