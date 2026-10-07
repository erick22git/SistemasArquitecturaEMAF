// 02-rutas.js — parámetros de ruta, query string, cuerpo JSON y códigos de estado
const express = require('express');
const app = express();
const PUERTO = process.env.PORT || 3000;

app.use(express.json()); // middleware: convierte el cuerpo JSON en req.body

const esNumero = (v) => v !== undefined && v.trim() !== '' && !Number.isNaN(Number(v));

// Parámetro de ruta: GET /saludo/Ana
app.get('/saludo/:nombre', (req, res) => {
  res.json({ mensaje: `Hola, ${req.params.nombre}` });
});

// Query string: GET /sumar?a=2&b=3
app.get('/sumar', (req, res) => {
  const { a, b } = req.query;
  if (!esNumero(a) || !esNumero(b)) {
    return res.status(400).json({ error: 'Los parámetros a y b deben ser numéricos' });
  }
  res.json({ a: Number(a), b: Number(b), resultado: Number(a) + Number(b) });
});

// Cuerpo JSON: POST /eco
app.post('/eco', (req, res) => {
  res.status(201).json({ recibido: req.body });
});

// Si ninguna ruta anterior respondió, llega aquí
app.use((req, res) => {
  res.status(404).json({ error: `No existe ${req.method} ${req.originalUrl}` });
});

app.listen(PUERTO, () => console.log(`Servidor en http://localhost:${PUERTO}`));
