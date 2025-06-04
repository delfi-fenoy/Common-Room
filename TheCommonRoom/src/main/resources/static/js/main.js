import { iniciarCarrusel, avanzar, retroceder } from './carruseles.js';

// Dos arrays con diferentes imágenes para diferenciar
const peliculasUltimos = Array.from({ length: 12 }, (_, i) => ({
  img: "/static/img/movie.png",
  titulo: `Peli_Ult ${i + 1}`,
  fecha: `0${i + 1}/01/2025`,
}));

const peliculasRecomendadas = Array.from({ length: 12 }, (_, i) => ({
  img: "/static/img/movie2.png",
  titulo: `Peli_Reco ${i + 1}`,
  fecha: `0${i + 1}/01/2025`,
}));

document.addEventListener("DOMContentLoaded", () => {
  iniciarCarrusel("latest-carousel", peliculasUltimos);
  iniciarCarrusel("recommended-carousel", peliculasRecomendadas);

  window.avanzar = avanzar;
  window.retroceder = retroceder;
});
