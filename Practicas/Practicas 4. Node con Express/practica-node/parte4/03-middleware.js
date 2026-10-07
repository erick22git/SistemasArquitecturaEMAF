// 03-middleware.js — los middlewares se ejecutan en orden, uno tras otro
const express = require('express');
const app = express();

app.use((req, res, next) => {
  console.log('1) middleware A: entra', req.method, req.url);
  next(); // pasa al siguiente
  console.log('1) middleware A: sale');
});

app.use((req, res, next) => {
  console.log('2) middleware B');
  next();
});

app.get('/', (req, res) => {
  console.log('3) manejador final de la ruta');
  res.send('ok');
});

app.listen(3000, () => console.log('Servidor en http://localhost:3000'));
