// 03-promesas.js — el mismo problema con promesas
const { promisify } = require('node:util');
const db = require('./db-simulada');

// promisify convierte una función con callback (error-first) en una que devuelve una promesa
const buscarUsuario = promisify(db.buscarUsuario);
const buscarPedidos = promisify(db.buscarPedidos);
const buscarDescuento = promisify(db.buscarDescuento);

console.log('Inicio');

buscarUsuario(1)
  .then((usuario) => buscarPedidos(usuario.id).then((lista) => ({ usuario, lista })))
  .then((datos) =>
    buscarDescuento(datos.usuario.id).then((descuento) => ({ ...datos, descuento }))
  )
  .then(({ usuario, lista, descuento }) => {
    const total = lista.reduce((suma, p) => suma + p.total, 0);
    const aPagar = total * (1 - descuento / 100);
    const resumen = `${usuario.nombre}: ${lista.length} pedidos, Bs ${total}`;
    console.log(`${resumen}, con ${descuento}% queda en Bs ${aPagar}`);
  })
  .catch((err) => console.error('Error:', err.message)) // un solo catch para toda la cadena
  .finally(() => console.log('Fin de la cadena'));

console.log('Fin del script (la cadena sigue ejecutándose)');
