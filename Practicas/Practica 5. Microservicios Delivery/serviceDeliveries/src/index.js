require('dotenv').config();
const express = require('express');
require('./config/db'); // abre el pool de conexiones a PostgreSQL
const deliveryRoutes = require('./routes/deliveryRoutes');

const app = express();
app.set('port', process.env.PORT || 3004);

// middlewares
app.use(express.json());
app.use((req, res, next) => {
  console.log(`[deliveries] ${req.method} ${req.originalUrl}`);
  next();
});

// rutas (el prefijo /api/v1 coincide con las "location" de nginx)
app.use('/api/v1/deliveries', deliveryRoutes);

// 404 en formato JSON
app.use((req, res) => res.status(404).json({ message: 'Ruta no encontrada en deliveries' }));

// errores no controlados en formato JSON
app.use((err, req, res, next) => {
  console.error('[deliveries] error:', err.message);
  res.status(500).json({ message: 'Error interno en deliveries', error: err.message });
});

app.listen(app.get('port'), () => {
  console.log('[deliveries] servidor escuchando en el puerto', app.get('port'));
});
