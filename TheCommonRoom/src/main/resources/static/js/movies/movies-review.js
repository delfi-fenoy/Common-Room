// =================== CARGA DE REVIEWS =================== //
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews-container");
    const movieId = reviewsContainer.dataset.movieId;

    if (!movieId) {
        reviewsContainer.innerHTML = "<p class='empty-reviews'>Error: ID de película no disponible.</p>";
        return;
    }

    fetch(`/movies/${movieId}/reviews`)
        .then(res => res.ok ? res.json() : Promise.reject("Error al cargar reseñas"))
        .then(reviews => renderReviews(reviews))
        .catch(err => {
            reviewsContainer.innerHTML = `<p class='empty-reviews'>${err}</p>`;
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

    const token = localStorage.getItem("accessToken");
    let currentUser = null;
    let currentRole = null;

    // Si hay token, obtener info del usuario actual para comparar
    if (token) {
        fetch("/users/me", {
            headers: { "Authorization": "Bearer " + token }
        })
            .then(res => res.ok ? res.json() : Promise.reject())
            .then(user => {
                currentUser = user.username;
                currentRole = user.role;
                renderAllReviews(reviews, currentUser, currentRole);
            })
            .catch(() => renderAllReviews(reviews, null, null));
    } else {
        renderAllReviews(reviews, null, null);
    }
}

function renderAllReviews(reviews, currentUser, currentRole) {
    const container = document.querySelector(".reviews");

    reviews.forEach(r => {
        const div = document.createElement("div");
        div.classList.add("review-card");

        const username = r.userPreview?.username || 'Anónimo';
        const profilePic = r.userPreview?.profilePictureUrl || '/img/user.png';
        const commentText = r.comment?.trim() || "<em>Sin comentario</em>";
        const rating = r.rating || 0;
        const ratingStars = "★".repeat(Math.floor(rating)) + (rating % 1 >= 0.5 ? "½" : "");
        const createdAtDate = r.createdAt ? new Date(r.createdAt).toLocaleDateString("es-AR") : '';

        // Crear innerHTML base
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
                        ${(username === currentUser || currentRole === "ADMIN") ? `
                            <div class="review-menu-wrapper">
                                <button class="review-options">⋮</button>
                                <div class="review-menu" style="display: none;">
                                    <button class="review-edit">✏️ Modificar</button>
                                    <button class="review-delete">🗑️ Eliminar</button>
                                </div>
                            </div>
                        ` : ""}
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

        // Eventos para modificar y eliminar
        if (username === currentUser || currentRole === "ADMIN") {
            const menuBtn = div.querySelector(".review-options");
            const menuBox = div.querySelector(".review-menu");
            const editBtn = div.querySelector(".review-edit");
            const deleteBtn = div.querySelector(".review-delete");

            // Mostrar/ocultar submenú
            menuBtn.onclick = () => {
                menuBox.style.display = menuBox.style.display === "none" ? "block" : "none";
            };

            // EDITAR reseña
            editBtn.onclick = () => {
                const movieId = r.moviePreview?.id;
                const title = r.moviePreview?.title;
                const poster = r.moviePreview?.posterUrl || "/img/default-poster.jpg";
                const year = r.moviePreview?.releaseDate?.substring(0, 4) || "20XX";

                showReviewModal(movieId, title, poster, year, r.rating, r.comment, r.id); // ← ahora con más datos
                menuBox.style.display = "none";
            };

            // ELIMINAR reseña
            deleteBtn.onclick = () => {
                const confirmDelete = confirm("¿Estás seguro de que querés borrar esta reseña?");
                if (!confirmDelete) return;

                fetch(`/reviews/${r.id}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": "Bearer " + localStorage.getItem("accessToken")
                    }
                })
                    .then(res => {
                        if (res.ok) {
                            alert("Reseña eliminada.");
                            location.reload();
                        } else {
                            return res.json().then(err => {
                                throw new Error(err.message || "No se pudo borrar la reseña.");
                            });
                        }
                    })
                    .catch(err => showErrorModal(err.message));
            };
        }

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
