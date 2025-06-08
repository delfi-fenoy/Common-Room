const estados = {}; // Guarda índice y data de cada carrusel

function renderCarrusel(id) {
  const container = document.getElementById(id);
  const estado = estados[id];
  if (!estado) return;
  
  container.innerHTML = "";
  const visibles = estado.data.slice(estado.index, estado.index + 4);

  visibles.forEach(movie => {
    const div = document.createElement("div");
    div.className = "movie-card";
    div.innerHTML = `
      <img src="${movie.img}" alt="${movie.titulo}">
      <div class="movie-info">${movie.titulo}</div>
    `;
    container.appendChild(div);

    void div.offsetWidth; // Forzar reflow para animación
    div.classList.add("visible");
  });
}

export function iniciarCarrusel(id, data) {
  estados[id] = { index: 0, data };

  renderCarrusel(id);

  setInterval(() => {
    estados[id].index += 4;
    if (estados[id].index >= estados[id].data.length) estados[id].index = 0;
    renderCarrusel(id);
  }, 10000);
}

export function avanzar(id) {
  if (!estados[id]) return;
  estados[id].index += 4;
  if (estados[id].index >= estados[id].data.length) estados[id].index = 0;
  renderCarrusel(id);
}

export function retroceder(id) {
  if (!estados[id]) return;
  estados[id].index -= 4;
  if (estados[id].index < 0) estados[id].index = estados[id].data.length - 4;
  renderCarrusel(id);
}
