// app.js — usa un módulo propio y módulos nativos de Node
const path = require('node:path');
const { sumar, conIva } = require('./matematicas');

console.log('2 + 3 =', sumar(2, 3));
console.log('100 con IVA =', conIva(100));
console.log('Archivo actual:', path.basename(__filename));
console.log('Ruta armada:', path.join('datos', 'clientes', 'lista.txt'));
