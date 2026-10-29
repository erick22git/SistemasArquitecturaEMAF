/*
  CRUD DELIVERIES (Express + pg)
  - GET    /api/v1/deliveries?status=      admin: todas | repartidor: solo las suyas
  - GET    /api/v1/deliveries/order/:id    autenticado
  - GET    /api/v1/deliveries/:id          autenticado (incluye el pedido en la clave "order")
  - POST   /api/v1/deliveries              admin
  - PUT    /api/v1/deliveries/:id/status   repartidor dueño o admin (propaga a orders)
  - DELETE /api/v1/deliveries/:id          admin
*/
const router = require('express').Router();
const Delivery = require('../models/delivery');
const ordersClient = require('../config/ordersClient');
const { verifyToken, requireRole } = require('./verifyToken');

// Orden del flujo de la entrega: solo se puede avanzar
const FLOW = ['asignado', 'recogido', 'en_camino', 'entregado'];
// Estado que se propaga al pedido (recogido/en_camino -> en_camino; entregado -> entregado)
const ORDER_STATUS = { recogido: 'en_camino', en_camino: 'en_camino', entregado: 'entregado' };

const isId = (value) => /^\d+$/.test(String(value));
const wrap = (fn) => (req, res, next) => fn(req, res, next).catch(next);

// GET /api/v1/deliveries?status=en_camino
router.get('/', verifyToken, requireRole('admin', 'repartidor'), wrap(async (req, res) => {
  const filter = { status: req.query.status };
  if (req.user.role === 'repartidor') filter.courier_id = req.user.id;
  res.json(await Delivery.findAll(filter));
}));

// GET /api/v1/deliveries/order/:orderId  (antes de /:id para que no lo capture)
router.get('/order/:orderId', verifyToken, wrap(async (req, res) => {
  if (!isId(req.params.orderId)) return res.status(400).json({ message: 'Id invalido' });
  const delivery = await Delivery.findByOrderId(req.params.orderId);
  if (!delivery) return res.status(404).json({ message: 'El pedido no tiene entrega asignada' });
  res.json(delivery);
}));

// GET /api/v1/deliveries/:id -> la entrega y el pedido consultado a orders
router.get('/:id', verifyToken, wrap(async (req, res) => {
  if (!isId(req.params.id)) return res.status(400).json({ message: 'Id invalido' });
  const delivery = await Delivery.findById(req.params.id);
  if (!delivery) return res.status(404).json({ message: 'Entrega no encontrada' });

  try {
    const order = await ordersClient.getOrder(delivery.order_id, req.headers['authorization']);
    res.json({ ...delivery, order });
  } catch (err) {
    res.status(502).json({ message: 'No se pudo consultar el servicio orders', error: err.message });
  }
}));

// POST /api/v1/deliveries { order_id, courier_id, courier_name, notes? }
router.post('/', verifyToken, requireRole('admin'), wrap(async (req, res) => {
  const { order_id, courier_id, courier_name, notes } = req.body;
  if (!order_id || !courier_id || !courier_name) {
    return res.status(400).json({ message: 'order_id, courier_id y courier_name son obligatorios' });
  }
  if (!isId(order_id)) return res.status(400).json({ message: 'order_id invalido' });

  if (await Delivery.findByOrderId(order_id)) {
    return res.status(409).json({ message: 'El pedido ya tiene una entrega asignada' });
  }

  let order;
  try {
    order = await ordersClient.getOrder(order_id, req.headers['authorization']);
  } catch (err) {
    return res.status(502).json({ message: 'No se pudo consultar el servicio orders', error: err.message });
  }
  if (!order) return res.status(404).json({ message: 'El pedido no existe' });
  if (!['confirmado', 'en_preparacion'].includes(order.status)) {
    return res.status(409).json({
      message: `El pedido esta '${order.status}': solo se asignan pedidos confirmado o en_preparacion`
    });
  }

  try {
    res.status(201).json(await Delivery.create({ order_id, courier_id, courier_name, notes }));
  } catch (err) {
    if (err.code === '23505') { // unique_violation (uq_delivery_order)
      return res.status(409).json({ message: 'El pedido ya tiene una entrega asignada' });
    }
    throw err;
  }
}));

// PUT /api/v1/deliveries/:id/status { status }
router.put('/:id/status', verifyToken, requireRole('repartidor', 'admin'), wrap(async (req, res) => {
  if (!isId(req.params.id)) return res.status(400).json({ message: 'Id invalido' });
  const { status } = req.body;
  if (!['recogido', 'en_camino', 'entregado'].includes(status)) {
    return res.status(400).json({ message: 'status debe ser recogido, en_camino o entregado' });
  }

  const delivery = await Delivery.findById(req.params.id);
  if (!delivery) return res.status(404).json({ message: 'Entrega no encontrada' });

  if (req.user.role === 'repartidor' && delivery.courier_id !== req.user.id) {
    return res.status(403).json({ message: 'Esta entrega no esta asignada a usted' });
  }
  // Solo se avanza en el flujo: asignado -> recogido -> en_camino -> entregado
  if (FLOW.indexOf(status) <= FLOW.indexOf(delivery.status)) {
    return res.status(409).json({ message: `No se puede pasar de '${delivery.status}' a '${status}'` });
  }

  const updated = await Delivery.updateStatus(delivery.id, status);

  // Propagar a orders con el mismo token del usuario
  const target = ORDER_STATUS[status];
  const bearer = req.headers['authorization'];
  let orderSync;
  try {
    const current = await ordersClient.getOrder(delivery.order_id, bearer);
    if (current && current.status === target) {
      orderSync = { ok: true, status: target, message: 'El pedido ya estaba en ese estado' };
    } else {
      const r = await ordersClient.updateOrderStatus(delivery.order_id, target, bearer);
      orderSync = r.ok
        ? { ok: true, status: target }
        : { ok: false, httpStatus: r.status, message: r.body && r.body.message };
    }
  } catch (err) {
    orderSync = { ok: false, message: `orders no respondio: ${err.message}` };
  }

  res.json({ ...updated, orderSync });
}));

// DELETE /api/v1/deliveries/:id
router.delete('/:id', verifyToken, requireRole('admin'), wrap(async (req, res) => {
  if (!isId(req.params.id)) return res.status(400).json({ message: 'Id invalido' });
  const ok = await Delivery.remove(req.params.id);
  if (!ok) return res.status(404).json({ message: 'Entrega no encontrada' });
  res.json({ success: true, message: 'Entrega eliminada', id: Number(req.params.id) });
}));

module.exports = router;
