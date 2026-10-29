const pool = require('../config/db');

// Modelo "a mano" con consultas SQL parametrizadas ($1, $2...) para evitar inyeccion SQL
const Delivery = {
  async findAll({ courier_id, status } = {}) {
    let sql = 'SELECT * FROM deliveries WHERE 1=1';
    const params = [];
    if (courier_id) { params.push(courier_id); sql += ` AND courier_id = $${params.length}`; }
    if (status) { params.push(status); sql += ` AND status = $${params.length}`; }
    sql += ' ORDER BY assigned_at DESC';
    const { rows } = await pool.query(sql, params);
    return rows;
  },

  async findById(id) {
    const { rows } = await pool.query('SELECT * FROM deliveries WHERE id = $1', [id]);
    return rows[0] || null;
  },

  async findByOrderId(orderId) {
    const { rows } = await pool.query('SELECT * FROM deliveries WHERE order_id = $1', [orderId]);
    return rows[0] || null;
  },

  async create({ order_id, courier_id, courier_name, notes }) {
    const { rows } = await pool.query(
      `INSERT INTO deliveries (order_id, courier_id, courier_name, notes)
       VALUES ($1, $2, $3, $4) RETURNING *`,
      [order_id, courier_id, courier_name, notes || null]
    );
    return rows[0];
  },

  // Al entregar se registra delivered_at
  async updateStatus(id, status) {
    const { rows } = await pool.query(
      `UPDATE deliveries
          SET status = $1::varchar,
              delivered_at = CASE WHEN $1::varchar = 'entregado' THEN NOW() ELSE delivered_at END
        WHERE id = $2 RETURNING *`,
      [status, id]
    );
    return rows[0] || null;
  },

  async remove(id) {
    const result = await pool.query('DELETE FROM deliveries WHERE id = $1', [id]);
    return result.rowCount > 0;
  }
};

module.exports = Delivery;
