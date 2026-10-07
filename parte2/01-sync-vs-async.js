// 01-sync-vs-async.js — lectura bloqueante vs no bloqueante
const fs = require('node:fs');

fs.writeFileSync('datos.txt', 'Contenido de prueba\n');

console.log('--- Síncrono ---');
const texto = fs.readFileSync('datos.txt', 'utf8');
console.log('Leído:', texto.trim());
console.log('Línea posterior a readFileSync');

console.log('--- Asíncrono ---');
fs.readFile('datos.txt', 'utf8', (err, data) => {
  if (err) return console.error('Error:', err.message);
  console.log('Leído (callback):', data.trim());
});
console.log('Línea posterior a readFile');
