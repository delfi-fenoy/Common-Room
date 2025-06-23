// =================== CARGA DE REVIEWS DEL USUARIO =================== //
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews");
    const usernameElem = document.querySelector(".user-username");
    if (!reviewsContainer || !usernameElem) return;

    const username = usernameElem.textContent.trim();

    fetch(`/users/${username}/reviews`)
        .then(async res => {
            if (!res.ok) {
                if (res.status === 404) return [];
                const err = await res.json();
                throw new Error(err.error || "Error al cargar reseñas");
            }
            return res.json();
        })
        .then(reviews => renderReviews(reviews))
        .catch(err => {
            reviewsContainer.innerHTML = `<p class='empty-reviews'>${err.message}</p>`;
        });
});


// =================== RENDERIZADO DE REVIEWS =================== //
function renderReviews(reviews) {
    const container = document.getElementById("reviews");
    container.innerHTML = "";

    if (!reviews || reviews.length === 0) {
        container.innerHTML = "<p class='empty-reviews'>Este usuario no tiene reseñas aún.</p>";
        return;
    }

    reviews.forEach(r => {
        const movie = r.moviePreview || {};
        const poster = movie.posterUrl || '/img/default-poster.jpg';
        const movieTitle = movie.title || 'Película desconocida';
        const movieYear = movie.releaseDate ? movie.releaseDate.slice(0, 4) : '';
        const commentText = r.comment?.trim() ? r.comment : "<em>Sin comentario</em>";
        const rating = r.rating || 0;
        const fullStars = Math.floor(rating);
        const halfStar = (rating % 1) >= 0.5 ? "½" : "";
        const ratingStars = "★".repeat(fullStars) + halfStar;
        const createdAtDate = r.createdAt ? new Date(r.createdAt).toLocaleString("es-AR") : '';

        const reviewCard = document.createElement("div");
        reviewCard.classList.add("review-card");

        reviewCard.innerHTML = `
            <div class="review-layout">
                <div class="review-left">
                    <img src="${poster}" alt="Póster ${movieTitle}" class="review-poster">
                </div>
                <div class="review-right">
                    <div class="review-title-row">
                        <a href="/moviesheet/${movie.id}" class="review-movie-title">${movieTitle} - (${movieYear})</a>
                        <button class="review-menu-btn">...</button>
                    </div>
                    <div class="review-meta">
                        <span class="review-rating">${ratingStars}</span> |
                        <span class="review-date">${createdAtDate}</span>
                    </div>
                    <div class="review-comment">${commentText}</div>
                    <div class="review-footer">
                        <span class="review-likes">💙 me gustas totales</span>
                    </div>
                </div>
            </div>
        `;

        container.appendChild(reviewCard);
    });
}


