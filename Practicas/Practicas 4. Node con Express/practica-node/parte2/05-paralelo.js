// 05-paralelo.js — ejecutar tareas independientes en paralelo
const esperar = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

async function tarea(nombre, ms, falla = false) {
  await esperar(ms);
  if (falla) throw new Error(`La tarea ${nombre} falló`);
  return nombre;
}

async function main() {
  console.time('secuencial');
  await tarea('A', 500);
  await tarea('B', 500);
  console.timeEnd('secuencial');

  console.time('Promise.all');
  const resultados = await Promise.all([tarea('A', 500), tarea('B', 500)]);
  console.timeEnd('Promise.all');
  console.log('Resultados:', resultados);

  // Promise.allSettled: no se cae si una falla
  const estados = await Promise.allSettled([tarea('A', 100), tarea('B', 100, true)]);
  console.log('allSettled:', estados.map((e) => e.status));

  // Promise.race: la primera que termine gana (patrón "timeout")
  try {
    await Promise.race([
      tarea('lenta', 1000),
      esperar(300).then(() => { throw new Error('Tiempo de espera agotado (300 ms)'); }),
    ]);
  } catch (err) {
    console.log('race:', err.message);
  }
}

main();
