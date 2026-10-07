// 01-servidor-http.js — un servidor web con el módulo nativo node:http
const http = require('node:http');

const PUERTO = process.env.PORT || 3000;

function responder(res, estado, datos) {
  res.writeHead(estado, { 'Content-Type': 'application/json; charset=utf-8' });
  res.end(JSON.stringify(datos));
}

const servidor = http.createServer((req, res) => {
  console.log(`${req.method} ${req.url}`);

  if (req.method === 'GET' && req.url === '/') {
    res.writeHead(200, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('Hola desde Node.js puro');
  }

  if (req.method === 'GET' && req.url === '/api/hora') {
    return responder(res, 200, { hora: new Date().toISOString() });
  }

  if (req.method === 'POST' && req.url === '/api/eco') {
    // El cuerpo llega en trozos: hay que armarlo a mano
    let cuerpo = '';
    req.on('data', (trozo) => (cuerpo += trozo));
    req.on('end', () => {
      try {
        responder(res, 200, { recibido: JSON.parse(cuerpo) });
      } catch {
        responder(res, 400, { error: 'JSON inválido' });
      }
    });
    return;
  }

  responder(res, 404, { error: 'Ruta no encontrada' });
});

servidor.listen(PUERTO, () => console.log(`Servidor en http://localhost:${PUERTO}`));
