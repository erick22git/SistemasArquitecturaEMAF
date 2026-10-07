// services/productos.service.js — reglas de negocio y acceso a datos (en memoria)
const { HttpError } = require('../middlewares/errores');

let siguienteId = 4;
const productos = [
  { id: 1, nombre: 'Teclado', precio: 120 },
  { id: 2, nombre: 'Mouse', precio: 60 },
  { id: 3, nombre: 'Monitor', precio: 900 },
];

const esperar = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

function validar(datos) {
  const { nombre, precio } = datos ?? {};
  if (typeof nombre !== 'string' || nombre.trim() === '') {
    throw new HttpError(400, 'El nombre es obligatorio');
  }
  if (typeof precio !== 'number' || Number.isNaN(precio) || precio < 0) {
    throw new HttpError(400, 'El precio debe ser un número mayor o igual a 0');
  }
  return { nombre: nombre.trim(), precio };
}

async function listar({ q, max } = {}) {
  await esperar(50); // simula la latencia de una base de datos real
  if (max !== undefined && Number.isNaN(Number(max))) {
    throw new HttpError(400, 'El parámetro max debe ser numérico');
  }
  let lista = productos;
  if (q) lista = lista.filter((p) => p.nombre.toLowerCase().includes(q.toLowerCase()));
  if (max !== undefined) lista = lista.filter((p) => p.precio <= Number(max));
  return lista;
}

async function obtener(id) {
  await esperar(50);
  const producto = productos.find((p) => p.id === id);
  if (!producto) throw new HttpError(404, `Producto ${id} no encontrado`);
  return producto;
}

async function crear(datos) {
  const { nombre, precio } = validar(datos);
  await esperar(50);
  const nuevo = { id: siguienteId++, nombre, precio };
  productos.push(nuevo);
  return nuevo;
}

async function actualizar(id, datos) {
  const producto = await obtener(id);
  Object.assign(producto, validar(datos));
  return producto;
}

async function eliminar(id) {
  await esperar(50);
  const posicion = productos.findIndex((p) => p.id === id);
  if (posicion === -1) throw new HttpError(404, `Producto ${id} no encontrado`);
  productos.splice(posicion, 1);
}

module.exports = { listar, obtener, crear, actualizar, eliminar };
