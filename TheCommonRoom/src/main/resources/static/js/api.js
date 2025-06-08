export async function obtenerPeliculasUltimos(page = 1, size = 10) {
    try {
        const res = await fetch(`/movie/list?page=${page}&size=${size}`);
        if (!res.ok) {
            throw new Error(`Error en la petición: ${res.status} ${res.statusText}`);
        }
        return await res.json();
    } catch (error) {
        console.error('Error al obtener películas:', error);
        return null; // O algún valor por defecto que manejes en el frontend
    }
}
