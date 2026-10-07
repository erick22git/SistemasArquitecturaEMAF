// 06-errores-comunes.js — los tropiezos más frecuentes con async/await
const esperar = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

async function obtener() {
  await esperar(100);
  return 42;
}

async function main() {
  // Error 1: olvidar el await
  const mal = obtener();
  console.log('Sin await :', mal);

  const bien = await obtener();
  console.log('Con await :', bien);

  // Error 2: forEach no espera a las funciones async
  console.log('--- forEach ---');
  [1, 2, 3].forEach(async (n) => {
    await esperar(100);
    console.log('forEach', n);
  });
  console.log('forEach "terminó" (pero las tareas siguen corriendo)');
  await esperar(300);

  // Correcto: for...of sí respeta el await (una tarea tras otra)
  console.log('--- for...of ---');
  for (const n of [1, 2, 3]) {
    await esperar(100);
    console.log('for...of', n);
  }
}

main();
