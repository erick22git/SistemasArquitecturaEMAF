const { Pool } = require('pg');

// Variables definidas en .env (local) o en docker-compose.yml (contenedor)
const pool = new Pool({
  host: process.env.PG_HOST || 'localhost',
  port: Number(process.env.PG_PORT) || 5432,
  user: process.env.PG_USER || 'delivery',
  password: process.env.PG_PASSWORD || '',
  database: process.env.PG_DATABASE || 'bd_entregas',
  max: Number(process.env.PG_POOL_MAX) || 10
});

pool.query('SELECT 1')
  .then(() => console.log(`[deliveries] conectado a PostgreSQL en ${process.env.PG_HOST}:${process.env.PG_PORT}/${process.env.PG_DATABASE}`))
  .catch(err => console.error('[deliveries] error al conectar a PostgreSQL:', err.message));

module.exports = pool;
