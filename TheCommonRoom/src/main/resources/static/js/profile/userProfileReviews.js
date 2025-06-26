// =================== VARIABLES GLOBALES =================== //
// Guarda todas las reseñas del usuario
let reviews = [];

// =================== CARGA INICIAL =================== //
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews");
    const usernameElem = document.querySelector(".user-username");
    const editProfileBtn = document.querySelector(".edit-profile-btn");

    if (!reviewsContainer || !usernameElem || !editProfileBtn) return;

    const profileUsername = usernameElem.textContent.trim();
    const currentUser = getCurrentUserInfo();

    if (!currentUser || currentUser.username !== profileUsername) {
        editProfileBtn.style.display = "none";
    }

    loadReviewsForUser(profileUsername);
});

// =================== OBTENER ID DE USUARIO DESDE JWT =================== //
function getCurrentUserInfo() {
    const token = localStorage.getItem("accessToken");

    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return {
            userId: payload.userId,
            username: payload.sub,
            isAdmin: payload.role === 'ADMIN'
        };
    } catch (e) {
        console.error("JWT inválido");
        return null;
    }
}

// =================== CARGA DE REVIEWS DESDE BACKEND =================== //
function loadReviewsForUser(username) {
    const reviewsContainer = document.getElementById("reviews");
    reviewsContainer.innerHTML = "<p class='empty-reviews'>Cargando reseñas...</p>";

    fetch(`/users/${username}/reviews`)
        .then(res => res.ok ? res.json() : res.json().then(err => Promise.reject(err)))
        .then(data => {
            reviews = data;
            renderReviews(reviews);
        })
        .catch(err => {
            reviewsContainer.innerHTML = `<p class='empty-reviews'>${err.message}</p>`;
        });
}

// =================== MOSTRAR LAS REVIEWS =================== //
function renderReviews(reviews) {
    const container = document.getElementById("reviews");
    container.innerHTML = "";

    const userInfo = getCurrentUserInfo();
    const currentUserId = userInfo?.userId;
    const isAdmin = userInfo?.isAdmin;

    reviews.forEach(r => {
        const isMyReview = String(r.userPreview?.id) === String(currentUserId);
        container.appendChild(buildReviewCard(r, isMyReview, isAdmin));
    });

    attachDropdownListeners();
}

// =================== CONSTRUIR UNA TARJETA DE RESEÑA =================== //
function buildReviewCard(r, isMyReview, isAdmin) {
    const div = document.createElement("div");
    div.classList.add("review-card");

    const movie = r.moviePreview || {};
    const poster = movie.posterUrl || '/img/default-poster.jpg';
    const movieTitle = movie.title || 'Película desconocida';
    const movieYear = movie.releaseDate ? `(${movie.releaseDate.slice(0, 4)})` : '';
    const comment = r.comment?.trim() || "<em>Sin comentario</em>";
    const rating = r.rating || 0;
    const likes = r.likesCount || 0;

    // Formato de fecha y hora
    const fecha = new Date(r.createdAt);
    const fechaStr = fecha.toLocaleDateString("es-AR");
    const horaStr = fecha.toLocaleTimeString("es-AR");

    // Estrellas de rating
    let stars = "";
    for (let i = 0; i < 5; i++) {
        if (rating >= i + 1) {
            stars += '<i class="fas fa-star"></i>';
        } else if (rating >= i + 0.5) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }

    // HTML de la tarjeta
    div.innerHTML = `
        <div class="review-media">
            <a href="/moviesheet/${movie.id}">
                <img src="${poster}" alt="Póster" class="image-placeholder">
            </a>
        </div>

        <div class="review-content-area">
            <div class="review-header">
                <div class="review-info">
                    <a href="/moviesheet/${movie.id}" class="titleMovie">${movieTitle} ${movieYear}</a>
                </div>
                <div class="options" data-review-id="${r.id}" data-is-my-review="${isMyReview}">
                    ${(isMyReview || isAdmin) ? `
                        <div class="options-button"><span></span></div>
                        <ul class="dropdown-menu">
                            ${isMyReview ? `<li data-action="modify"><i class="fas fa-pencil-alt"></i> Modificar</li>` : ''}
                            <li data-action="delete"><i class="fas fa-trash-alt"></i> Eliminar</li>
                        </ul>` : ''}
                </div>
            </div>

            <div class="review-body">
                <div class="star-rating">${stars}</div>
                <p class="review-text">${comment}</p>
            </div>

            <div class="review-footer">
                <div class="likes">
                    <i class="fas fa-heart"></i>
                    <span class="like-count">${likes} ${likes === 1 ? 'like' : 'likes'}</span>
                </div>
                <div class="review-date">
                    <span class="date">${fechaStr}</span>
                    <span class="time">${horaStr}</span>
                </div>
            </div>
        </div>
    `;

    return div;
}

// =================== ESCUCHAR MENÚ DE 3 PUNTOS =================== //
function attachDropdownListeners() {
    // Limpiar listeners viejos
    document.querySelectorAll('.options-button').forEach(btn => {
        const cloned = btn.cloneNode(true);
        btn.parentNode.replaceChild(cloned, btn);
    });

    document.querySelectorAll('.dropdown-menu li').forEach(item => {
        const cloned = item.cloneNode(true);
        item.parentNode.replaceChild(cloned, item);
    });

    document.querySelectorAll('.review-card .options').forEach(optionsContainer => {
        const btn = optionsContainer.querySelector('.options-button');
        const menu = optionsContainer.querySelector('.dropdown-menu');

        if (btn && menu) {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                document.querySelectorAll('.dropdown-menu.show').forEach(m => m.classList.remove('show'));
                menu.classList.toggle('show');
            });

            menu.querySelectorAll('li').forEach(item => {
                item.addEventListener('click', async (e) => {
                    e.stopPropagation();
                    const action = item.dataset.action;
                    const reviewId = optionsContainer.dataset.reviewId;

                    if (action === 'modify') {
                        const reviewToEdit = reviews.find(r => r.id.toString() === reviewId);
                        if (reviewToEdit) {
                            showEditReviewModal(reviewToEdit);
                        } else {
                            showErrorModal('No se encontró la reseña para modificar.');
                        }
                    } else if (action === 'delete') {
                        if (confirm('¿Estás seguro de que querés eliminar esta reseña?')) {
                            try {
                                const res = await fetch(`/reviews/${reviewId}`, {
                                    method: "DELETE",
                                    headers: {
                                        "Authorization": "Bearer " + localStorage.getItem("accessToken")
                                    }
                                });

                                if (!res.ok) {
                                    const err = await res.json();
                                    throw new Error(err.error || "No se pudo eliminar.");
                                }

                                location.reload();
                            } catch (err) {
                                showErrorModal(err.message);
                            }
                        }
                    }

                    menu.classList.remove("show");
                });
            });
        }
    });

    // Cerrar menú al hacer clic fuera
    document.addEventListener('click', (e) => {
        document.querySelectorAll('.dropdown-menu.show').forEach(m => {
            if (!m.contains(e.target)) m.classList.remove("show");
        });
    });
}
