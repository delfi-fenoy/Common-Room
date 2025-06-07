import { iniciarCarrusel, avanzar, retroceder } from './carruseles.js';
import { obtenerPeliculasUltimos } from './api.js'; // nuevo import

// Versión de prueba para recomendadas (sigue usando datos locales)
const peliculasRecomendadas = Array.from({ length: 12 }, (_, i) => ({
  img: "/img/movie2.png",
  titulo: `Peli_Reco ${i + 1}`,
  fecha: `0${i + 1}/01/2025`,
}));

document.addEventListener("DOMContentLoaded", async () => {
  // Carga del backend para "últimos estrenos"
  const peliculasUltimos = await obtenerPeliculasUltimos();

  // Mapear para adaptarlo al formato del carrusel
  const formateadas = peliculasUltimos.map(peli => ({
    img: peli.posterUrl,
    titulo: peli.title,
    fecha: peli.releaseDate,
  }));

  iniciarCarrusel("latest-carousel", formateadas);
  iniciarCarrusel("recommended-carousel", peliculasRecomendadas); // local por ahora

  window.avanzar = avanzar;
  window.retroceder = retroceder;
});

