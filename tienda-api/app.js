// app.js — configura la aplicación (se exporta para poder probarla sin abrir un puerto fijo)
const express = require('express');
const logger = require('./middlewares/logger');
const { notFound, manejarErrores } = require('./middlewares/errores');
const productosRoutes = require('./routes/productos.routes');

const app = express();

app.use(express.json());
app.use(logger);

app.get('/salud', (req, res) => {
  res.json({ estado: 'ok', hora: new Date().toISOString() });
});

app.use('/productos', productosRoutes);

app.use(notFound); // siempre después de todas las rutas
app.use(manejarErrores); // siempre al final: recibe (err, req, res, next)

module.exports = app;
