// 04-async-await.js — el mismo problema, escrito como si fuera código síncrono
const { promisify } = require('node:util');
const db = require('./db-simulada');

const buscarUsuario = promisify(db.buscarUsuario);
const buscarPedidos = promisify(db.buscarPedidos);
const buscarDescuento = promisify(db.buscarDescuento);

async function resumenUsuario(id) {
  const usuario = await buscarUsuario(id);
  const lista = await buscarPedidos(usuario.id);
  const descuento = await buscarDescuento(usuario.id);
  const total = lista.reduce((suma, p) => suma + p.total, 0);
  const aPagar = total * (1 - descuento / 100);
  const resumen = `${usuario.nombre}: ${lista.length} pedidos, Bs ${total}`;
  return `${resumen}, con ${descuento}% queda en Bs ${aPagar}`;
}

async function main() {
  try {
    console.log(await resumenUsuario(1));
    console.log(await resumenUsuario(99)); // este usuario no existe: lanza un error
    console.log('Esta línea nunca se ejecuta');
  } catch (err) {
    console.error('Error capturado con try/catch:', err.message);
  } finally {
    console.log('Fin de main()');
  }
}

main();
