// =================== CARGA DE REVIEWS =================== //
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews-container");
    const movieId = reviewsContainer.dataset.movieId;

    if (!movieId) {
        const container = document.querySelector(".reviews");
        container.innerHTML = "<p class='empty-reviews'>Error: ID de película no disponible para cargar reseñas.</p>";
        return;
    }

    fetch(`/movies/${movieId}/reviews`)
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
            const container = document.querySelector(".reviews");
            container.innerHTML = `<p class='empty-reviews'>${err.message}</p>`;
        });
});


// =================== RENDERIZADO DE REVIEWS =================== //
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

        const username = r.userPreview ? r.userPreview.username : 'Usuario Anónimo';
        const profilePic = r.userPreview?.profilePictureUrl || '/img/user.png';
        const commentText = r.comment?.trim() ? r.comment : "<em>Sin comentario</em>";
        const rating = r.rating || 0;
        const ratingStars = "★".repeat(Math.floor(rating)) + (rating % 1 >= 0.5 ? "½" : "");
        const createdAtDate = r.createdAt ? new Date(r.createdAt).toLocaleDateString("es-AR") : '';

        div.innerHTML = `
            <div class="review-header">
                <a href="/profile/${username}">
                    <img src="${profilePic}" alt="Foto de ${username}" class="review-avatar" />
                </a>
                <div class="review-user-info">
                    <div class="review-top-bar">
                        <a href="/profile/${username}" class="review-username-link">
                            <span class="review-username">${username}</span>
                        </a>
                        <button class="review-options">...</button>
                    </div>
                    <div class="review-meta">
                        <span class="review-rating">${ratingStars}</span>
                        <span class="review-date">| ${createdAtDate}</span>
                    </div>
                </div>
            </div>
            <div class="review-comment">${commentText}</div>
            <div class="review-footer">
                <span class="review-likes">♥ 0 me gusta</span>
            </div>
        `;

        container.appendChild(div);
    });
}

// =================== BOTÓN +REVIEW =================== //
document.addEventListener("DOMContentLoaded", () => {
    const addReviewBtn = document.getElementById("add-review-btn");
    if (!addReviewBtn) return;

    addReviewBtn.addEventListener("click", () => {
        const token = localStorage.getItem("accessToken");

        if (!token || !isValidJwt(token)) {
            showErrorModal("Tenés que iniciar sesión para escribir una reseña.");
            return;
        }

        const movieId = addReviewBtn.dataset.movieId;
        const title = addReviewBtn.dataset.movieTitle;
        const poster = addReviewBtn.dataset.moviePoster;
        const year = document.getElementById("review-movie-year")?.textContent?.match(/\d{4}/)?.[0] || "20XX";

        showReviewModal(movieId, title, poster, year);
    });
});
