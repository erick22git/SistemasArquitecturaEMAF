// db-simulada.js — "base de datos" lenta basada en callbacks (convención error-first)
const usuarios = {
  1: { id: 1, nombre: 'Ana' },
  2: { id: 2, nombre: 'Luis' },
};
const pedidos = {
  1: [{ id: 101, total: 250 }, { id: 102, total: 80 }],
  2: [],
};
const descuentos = { 1: 10, 2: 0 }; // porcentaje

function buscarUsuario(id, callback) {
  setTimeout(() => {
    const usuario = usuarios[id];
    if (!usuario) return callback(new Error(`Usuario ${id} no existe`));
    callback(null, usuario);
  }, 300);
}

function buscarPedidos(idUsuario, callback) {
  setTimeout(() => callback(null, pedidos[idUsuario] ?? []), 300);
}

function buscarDescuento(idUsuario, callback) {
  setTimeout(() => callback(null, descuentos[idUsuario] ?? 0), 300);
}

module.exports = { buscarUsuario, buscarPedidos, buscarDescuento };
