// 04-bloqueo.js — qué pasa cuando se bloquea el event loop
const inicio = Date.now();

setTimeout(() => {
  console.log(`El temporizador de 100 ms se ejecutó a los ${Date.now() - inicio} ms`);
}, 100);

// Bucle que ocupa el único hilo durante 2 segundos
while (Date.now() - inicio < 2000) {
  // no hace nada: solo bloquea
}

console.log('Terminó el bucle bloqueante');
