// 03-event-loop.js — ¿en qué orden se ejecuta cada cosa?
console.log('1. inicio (síncrono)');

setTimeout(() => console.log('5. setTimeout con 0 ms (temporizador)'), 0);

Promise.resolve().then(() => console.log('4. promesa resuelta (microtarea)'));

process.nextTick(() => console.log('3. process.nextTick'));

console.log('2. fin del script (síncrono)');
