// 02-argumentos.js — leer datos desde la línea de comandos
// Uso: node 02-argumentos.js Carlos 2026
const [, , nombre = 'estudiante', anio = '2026'] = process.argv;
console.log(`Hola ${nombre}, bienvenido a COM-350 (${anio})`);
console.log('Argumentos recibidos:', process.argv.slice(2));
