// middlewares/logger.js — registra cada solicitud cuando termina de responderse
module.exports = function logger(req, res, next) {
  const inicio = Date.now();
  res.on('finish', () => {
    if (process.env.NODE_ENV === 'test') return;
    const ms = Date.now() - inicio;
    console.log(`${req.method} ${req.originalUrl} -> ${res.statusCode} (${ms} ms)`);
  });
  next();
};
