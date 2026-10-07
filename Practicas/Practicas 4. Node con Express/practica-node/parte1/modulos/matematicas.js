// matematicas.js — un módulo propio (CommonJS)
const IVA = 0.13;

function sumar(a, b) {
  return a + b;
}

function conIva(monto) {
  return +(monto * (1 + IVA)).toFixed(2);
}

module.exports = { sumar, conIva };
