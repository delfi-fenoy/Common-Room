// =================== Al cargar el DOM, traer reseñas de la película =================== \\
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews-container");
    // Obtener el ID de la película del atributo data-movie-id del div
    const movieId = reviewsContainer.dataset.movieId;

    if (!movieId) {
        console.error("No se pudo obtener el ID de la película. Asegúrate de que th:data-movie-id esté configurado.");
        const container = document.querySelector(".reviews");
        container.innerHTML = "<p class='empty-reviews'>Error: ID de película no disponible para cargar reseñas.</p>";
        return;
    }

    fetch(`/movies/${movieId}/reviews`)
        .then(res => {
            if (!res.ok) {
                // Manejar errores de respuesta HTTP (ej. 404 Not Found, 500 Internal Server Error)
                if (res.status === 404) {
                    console.warn(`No se encontraron reseñas para la película con ID: ${movieId}`);
                    return []; // Retorna un array vacío para que renderReviews muestre "No hay reseñas aún."
                }
                throw new Error(`Error en la respuesta del servidor: ${res.status} ${res.statusText}`);
            }
            return res.json();
        })
        .then(reviews => renderReviews(reviews))
        .catch(err => {
            console.error("Error al cargar reseñas:", err);
            // Mostrar un mensaje de error amigable al usuario
            const container = document.querySelector(".reviews");
            container.innerHTML = "<p class='empty-reviews'>Error al cargar las reseñas. Inténtalo de nuevo más tarde.</p>";
        });
});

// =================== Renderizar todas las reseñas obtenidas =================== \\
function renderReviews(reviews) {
    const container = document.querySelector(".reviews");
    container.innerHTML = "";

    if (!reviews || reviews.length === 0) {
        container.innerHTML = "<p class='empty-reviews'>No hay reseñas aún.</p>";
        return;
    }

    reviews.forEach(r => {
        const div = document.createElement("div");
        div.classList.add("review-card");

        // Accede a los datos del usuario a través de r.userPreview
        const username = r.userPreview ? r.userPreview.username : 'Usuario Anónimo';
        const profilePic = r.userPreview && r.userPreview.profilePictureUrl ? r.userPreview.profilePictureUrl : '/img/user.png';
        const commentText = r.comment && r.comment.trim() !== '' ? r.comment : "<em>Sin comentario</em>";
        const ratingStars = "★".repeat(Math.max(0, Math.floor(r.rating || 0)));
        const createdAtDate = r.createdAt ? new Date(r.createdAt).toLocaleString("es-AR") : '';

        div.innerHTML = `
            <div class="review-header">
                <img src="${profilePic}" alt="Foto de ${username}" class="review-avatar" />
                <div class="review-user-info">
                    <span class="review-username">${username}</span>
                    <span class="review-rating">${ratingStars}</span>
                </div>
            </div>
            <div class="review-comment">
                ${commentText}
            </div>
            <div class="review-footer">
                <span>${createdAtDate}</span>
            </div>
        `;
        container.appendChild(div);
    });
}