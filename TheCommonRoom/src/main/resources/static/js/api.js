export async function obtenerPeliculasUltimos(page = 1, size = 10) {
    try {
        const res = await fetch(`/movie/recent?page=${page}&size=${size}`);
        if (!res.ok) throw new Error(`Error: ${res.status}`);
        return await res.json();
    } catch (e) {
        console.error('Error al obtener últimas:', e);
        return [];
    }
}

export async function obtenerPeliculasPopulares(page = 1, size = 10) {
    try {
        const res = await fetch(`/movie/popular?page=${page}&size=${size}`);
        if (!res.ok) throw new Error(`Error: ${res.status}`);
        return await res.json();
    } catch (e) {
        console.error('Error al obtener populares:', e);
        return [];
    }
}
