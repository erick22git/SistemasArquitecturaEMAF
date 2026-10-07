// 02-callbacks.js — tres operaciones dependientes con callbacks anidados
const { buscarUsuario, buscarPedidos, buscarDescuento } = require('./db-simulada');

console.log('Inicio');

buscarUsuario(1, (err, usuario) => {
  if (err) return console.error('Error:', err.message);

  buscarPedidos(usuario.id, (err, lista) => {
    if (err) return console.error('Error:', err.message);

    buscarDescuento(usuario.id, (err, descuento) => {
      if (err) return console.error('Error:', err.message);

      const total = lista.reduce((suma, p) => suma + p.total, 0);
      const aPagar = total * (1 - descuento / 100);
      const resumen = `${usuario.nombre}: ${lista.length} pedidos, Bs ${total}`;
      console.log(`${resumen}, con ${descuento}% queda en Bs ${aPagar}`);
    });
  });
});

console.log('Fin del script (el programa NO esperó los resultados)');
