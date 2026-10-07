// middlewares/errores.js — errores HTTP propios y manejo centralizado
class HttpError extends Error {
  constructor(status, mensaje) {
    super(mensaje);
    this.status = status;
  }
}

function notFound(req, res, next) {
  next(new HttpError(404, `No existe ${req.method} ${req.originalUrl}`));
}

// Un middleware de errores tiene 4 parámetros: Express lo reconoce por eso
function manejarErrores(err, req, res, next) {
  if (err.type === 'entity.parse.failed') {
    return res.status(400).json({ error: 'El cuerpo no es un JSON válido' });
  }
  const status = err.status ?? 500;
  if (status === 500) console.error(err); // el detalle queda en el servidor, no en la respuesta
  const mensaje = status === 500 ? 'Error interno del servidor' : err.message;
  res.status(status).json({ error: mensaje });
}

module.exports = { HttpError, notFound, manejarErrores };
