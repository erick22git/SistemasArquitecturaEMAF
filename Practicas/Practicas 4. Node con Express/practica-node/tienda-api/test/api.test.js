// test/api.test.js — pruebas automáticas con el ejecutor nativo de Node (node --test)
process.env.NODE_ENV = 'test';

const { test, before, after } = require('node:test');
const assert = require('node:assert/strict');
const app = require('../app');

let servidor;
let base;

before(async () => {
  await new Promise((resolve) => {
    servidor = app.listen(0, resolve); // puerto 0: el sistema elige uno libre
  });
  base = `http://localhost:${servidor.address().port}`;
});

after(() => servidor.close());

async function pedir(ruta, opciones) {
  const respuesta = await fetch(base + ruta, opciones);
  const texto = await respuesta.text();
  return { status: respuesta.status, cuerpo: texto ? JSON.parse(texto) : null };
}

const conJson = (metodo, datos) => ({
  method: metodo,
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(datos),
});

test('GET /salud responde ok', async () => {
  const { status, cuerpo } = await pedir('/salud');
  assert.equal(status, 200);
  assert.equal(cuerpo.estado, 'ok');
});

test('GET /productos lista los productos', async () => {
  const { status, cuerpo } = await pedir('/productos');
  assert.equal(status, 200);
  assert.equal(cuerpo.length, 3);
});

test('GET /productos?q=mou filtra por nombre', async () => {
  const { cuerpo } = await pedir('/productos?q=mou');
  assert.deepEqual(cuerpo.map((p) => p.nombre), ['Mouse']);
});

test('GET /productos?max=100 filtra por precio', async () => {
  const { cuerpo } = await pedir('/productos?max=100');
  assert.deepEqual(cuerpo.map((p) => p.nombre), ['Mouse']);
});

test('GET /productos/1 devuelve el producto', async () => {
  const { status, cuerpo } = await pedir('/productos/1');
  assert.equal(status, 200);
  assert.equal(cuerpo.nombre, 'Teclado');
});

test('GET /productos/999 devuelve 404', async () => {
  const { status } = await pedir('/productos/999');
  assert.equal(status, 404);
});

test('GET /productos/abc devuelve 400', async () => {
  const { status } = await pedir('/productos/abc');
  assert.equal(status, 400);
});

test('POST /productos crea y devuelve 201', async () => {
  const datos = { nombre: 'Webcam', precio: 200 };
  const { status, cuerpo } = await pedir('/productos', conJson('POST', datos));
  assert.equal(status, 201);
  assert.equal(cuerpo.id, 4);
});

test('POST /productos sin nombre devuelve 400', async () => {
  const { status } = await pedir('/productos', conJson('POST', { precio: 10 }));
  assert.equal(status, 400);
});

test('POST /productos con JSON roto devuelve 400', async () => {
  const { status, cuerpo } = await pedir('/productos', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: '{ esto no es json',
  });
  assert.equal(status, 400);
  assert.match(cuerpo.error, /JSON/);
});

test('PUT /productos/2 actualiza', async () => {
  const datos = { nombre: 'Mouse gamer', precio: 150 };
  const { status, cuerpo } = await pedir('/productos/2', conJson('PUT', datos));
  assert.equal(status, 200);
  assert.equal(cuerpo.precio, 150);
});

test('DELETE /productos/3 devuelve 204 y luego 404', async () => {
  const borrado = await pedir('/productos/3', { method: 'DELETE' });
  assert.equal(borrado.status, 204);
  const otraVez = await pedir('/productos/3');
  assert.equal(otraVez.status, 404);
});

test('una ruta inexistente devuelve 404 en JSON', async () => {
  const { status, cuerpo } = await pedir('/nada');
  assert.equal(status, 404);
  assert.ok(cuerpo.error);
});
