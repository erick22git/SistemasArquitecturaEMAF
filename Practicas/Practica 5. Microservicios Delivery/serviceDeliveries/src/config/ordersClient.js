// Cliente HTTP hacia el microservicio ORDERS (Laravel) usando fetch nativo de Node.
// Reenvia el header Authorization del usuario: orders valida el JWT por su cuenta.
const BASE_URL = (process.env.ORDERS_URL || 'http://localhost:8000/api/v1').replace(/\/$/, '');
const TIMEOUT_MS = 5000;

/** GET /orders/:id -> el pedido, o null si no existe. Lanza error si orders no responde. */
async function getOrder(id, bearer) {
  const res = await fetch(`${BASE_URL}/orders/${id}`, {
    headers: { Authorization: bearer, Accept: 'application/json' },
    signal: AbortSignal.timeout(TIMEOUT_MS)
  });
  if (res.status === 404) return null;
  if (!res.ok) throw new Error(`orders respondio ${res.status}`);
  return res.json();
}

/** PATCH /orders/:id/status -> { ok, status, body }. Lanza error si orders no responde. */
async function updateOrderStatus(id, status, bearer) {
  const res = await fetch(`${BASE_URL}/orders/${id}/status`, {
    method: 'PATCH',
    headers: { Authorization: bearer, Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ status }),
    signal: AbortSignal.timeout(TIMEOUT_MS)
  });
  let body = null;
  try { body = await res.json(); } catch (e) { /* respuesta sin cuerpo JSON */ }
  return { ok: res.ok, status: res.status, body };
}

module.exports = { getOrder, updateOrderStatus };
