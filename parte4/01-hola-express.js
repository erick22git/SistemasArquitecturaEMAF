// 01-hola-express.js — el servidor más pequeño con Express
const express = require('express');
const app = express();
const PUERTO = process.env.PORT || 3000;

app.get('/', (req, res) => {
  res.send('Hola desde Express');
});

app.listen(PUERTO, () => console.log(`Servidor en http://localhost:${PUERTO}`));
