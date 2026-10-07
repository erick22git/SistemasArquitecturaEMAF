// server.js — punto de arranque: solo levanta el servidor
const app = require('./app');

const PUERTO = process.env.PORT || 3000;

app.listen(PUERTO, () => console.log(`API de la tienda en http://localhost:${PUERTO}`));
