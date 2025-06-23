import { iniciarCarrusel, avanzar, retroceder } from './carruseles.js';
import { obtenerPeliculasUltimos, obtenerPeliculasPopulares } from './api.js';

document.addEventListener("DOMContentLoaded", async () => {
  const peliculasUltimos = await obtenerPeliculasUltimos();
  const peliculasPopulares = await obtenerPeliculasPopulares();

  const formateadasUltimos = peliculasUltimos.map(peli => ({
    img: peli.posterUrl,
    titulo: peli.title,
    fecha: peli.releaseDate,
    id: peli.id,
  }));

  const formateadasPopulares = peliculasPopulares.map(peli => ({
    img: peli.posterUrl,
    titulo: peli.title,
    fecha: peli.releaseDate,
    id: peli.id,
  }));

  iniciarCarrusel("latest-carousel", formateadasUltimos);
  iniciarCarrusel("recommended-carousel", formateadasPopulares);

  window.avanzar = avanzar;
  window.retroceder = retroceder;
});
