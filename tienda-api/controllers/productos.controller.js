// controllers/productos.controller.js — traduce HTTP <-> servicio (sin reglas de negocio)
const service = require('../services/productos.service');
const { HttpError } = require('../middlewares/errores');

function leerId(req) {
  const id = Number(req.params.id);
  if (!Number.isInteger(id) || id <= 0) {
    throw new HttpError(400, 'El id debe ser un entero positivo');
  }
  return id;
}

// En Express 5, si una función async lanza un error, llega solo al middleware de errores
exports.listar = async (req, res) => {
  res.json(await service.listar(req.query));
};

exports.obtener = async (req, res) => {
  res.json(await service.obtener(leerId(req)));
};

exports.crear = async (req, res) => {
  res.status(201).json(await service.crear(req.body));
};

exports.actualizar = async (req, res) => {
  res.json(await service.actualizar(leerId(req), req.body));
};

exports.eliminar = async (req, res) => {
  await service.eliminar(leerId(req));
  res.status(204).end();
};
