// =================== Variables globales =================== //
let reviews = []; // Variable global para guardar las reseñas cargadas

// =================== CARGA INICIAL DE REVIEWS =================== //
document.addEventListener("DOMContentLoaded", () => {
    const reviewsContainer = document.getElementById("reviews-container");
    const movieId = reviewsContainer?.dataset?.movieId;

    if (!movieId || movieId === "ID_DE_TU_PELICULA_AQUI") {
        reviewsContainer.innerHTML = "<p class='empty-reviews'>Error: ID de película no disponible para cargar reseñas.</p>";
        return;
    }

    loadReviewsForMovie(movieId);
});

// =================== FETCH DE REVIEWS DESDE EL BACKEND =================== //
function loadReviewsForMovie(movieId) {
    const reviewsContainer = document.getElementById("reviews-container");
    reviewsContainer.innerHTML = "<p class='empty-reviews'>Cargando reseñas...</p>";

    fetch(`/movies/${movieId}/reviews`)
        .then(async res => {
            if (!res.ok) {
                if (res.status === 404) return [];
                const err = await res.json();
                throw new Error(err.error || "Error al cargar reseñas");
            }
            return res.json();
        })
        .then(data => {
            reviews = data;               // Guardás la lista globalmente
            renderReviews(reviews);       // Mostrás las reviews en pantalla
            attachDropdownListeners();    // Asignás los eventos a botones
        })
        .catch(err => {
            reviewsContainer.innerHTML = `<p class='empty-reviews'>${err.message}</p>`;
        });
}

// =================== RENDERIZADO DE REVIEWS =================== //
function renderReviews(reviews) {
    const container = document.getElementById("reviews-container");
    const addReviewBtn = document.getElementById("add-review-btn");
    container.innerHTML = "";

    const currentUserId = getCurrentUserId();
    let myReview = null;
    const otherReviews = [];

    reviews.forEach(r => {
        if (r.userPreview?.id === currentUserId) {
            myReview = r;
        } else {
            otherReviews.push(r);
        }
    });

    // Ocultar botón si ya hizo una review
    if (myReview && addReviewBtn) {
        addReviewBtn.style.display = "none";
    }

    // Mostrar primero mi reseña
    if (myReview) {
        container.appendChild(buildReviewCard(myReview, true));
    }

    // Mostrar las demás reseñas
    if (otherReviews.length === 0 && !myReview) {
        container.innerHTML = "<p class='empty-reviews'>No hay reseñas aún.</p>";
        return;
    }

    otherReviews.forEach(r => {
        container.appendChild(buildReviewCard(r, false));
    });

    attachDropdownListeners();
}

// =================== CONSTRUCCIÓN DE TARJETA DE RESEÑA =================== //
function buildReviewCard(r, isMyReview) {
    const div = document.createElement("div");
    div.classList.add("review-card");

    const username = r.userPreview?.username || 'Usuario Anónimo';
    const profilePictureUrl = r.userPreview?.profilePictureUrl;
    const profilePicHtml = profilePictureUrl
        ? `<img class="review-avatar" src="${profilePictureUrl}" alt="Foto de ${username}">`
        : `<div class="review-avatar avatar-circle-fallback"></div>`;

    const commentText = r.comment?.trim() || "<em>Sin comentario</em>";
    const rating = r.rating || 0;

    let ratingStarsHtml = '';
    for (let i = 0; i < 5; i++) {
        if (rating >= i + 1) {
            ratingStarsHtml += '<i class="fas fa-star"></i>';
        } else if (rating >= i + 0.5) {
            ratingStarsHtml += '<i class="fas fa-star-half-alt"></i>';
        } else {
            ratingStarsHtml += '<i class="far fa-star"></i>';
        }
    }

    const createdAtFull = r.createdAt ? new Date(r.createdAt).toLocaleString("es-AR", {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', second: '2-digit'
    }) : '';
    const likes = r.likesCount || 0;

    // Cambio acá también para el atributo data-is-my-review
    div.innerHTML =
        `<div class="review-header">
            <a href="/profile/${username}" class="profile-link-container">
                ${profilePicHtml}
                <div class="review-info">
                    <span class="review-by">Review by</span>
                    <span class="username">${username}</span>
                </div>
            </a>
            <div class="options" data-review-id="${r.id}" data-is-my-review="${isMyReview}">
                ${isMyReview ? `
                    <div class="options-button"><span></span></div>
                    <ul class="dropdown-menu">
                        <li data-action="modify"><i class="fas fa-pencil-alt"></i> Modificar</li>
                        <li data-action="delete"><i class="fas fa-trash-alt"></i> Eliminar</li>
                    </ul>` : ''}
            </div>
        </div>
        <div class="review-content-section">
            <div class="review-rating-stars">${ratingStarsHtml}</div>
            <p class="review-comment">${commentText}</p>
        </div>
        <div class="review-footer">
            <div class="review-likes">
                <i class="fas fa-heart"></i>
                <span>${likes} ${likes === 1 ? "like" : "likes"}</span>
            </div>
            <span class="review-date">${createdAtFull}</span>
        </div>`;

    return div;
}

// =================== OBTENER ID DE USUARIO DESDE JWT =================== //
function getCurrentUserId() {
    const token = localStorage.getItem("accessToken");
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.userId;
        } catch (e) {
            console.error("Error al parsear JWT:", e);
            return null;
        }
    }
    return null;
}

// =================== VERIFICAR SI EL JWT ES VÁLIDO =================== //
function isValidJwt(token) {
    return typeof token === 'string' && token.split('.').length === 3;
}

// =================== LISTENERS PARA EL MENÚ MODIFICAR / ELIMINAR =================== //
function attachDropdownListeners() {
    // Limpiar listeners anteriores
    document.querySelectorAll('.review-card .options .options-button').forEach(btn => {
        const clonedBtn = btn.cloneNode(true);
        btn.parentNode.replaceChild(clonedBtn, btn);
    });
    document.querySelectorAll('.review-card .dropdown-menu li').forEach(item => {
        const clonedItem = item.cloneNode(true);
        item.parentNode.replaceChild(clonedItem, item);
    });

    // Asignar listeners a los botones de opciones
    document.querySelectorAll('.review-card .options').forEach(optionsContainer => {
        const optionsButton = optionsContainer.querySelector('.options-button');
        const dropdownMenu = optionsContainer.querySelector('.dropdown-menu');

        if (optionsButton && dropdownMenu && optionsContainer.dataset.isMyReview === 'true') {
            optionsButton.addEventListener('click', (event) => {
                event.stopPropagation();
                document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                    if (menu !== dropdownMenu) {
                        menu.classList.remove('show');
                    }
                });
                dropdownMenu.classList.toggle('show');
            });

            dropdownMenu.querySelectorAll('li').forEach(item => {
                item.addEventListener('click', (event) => {
                    event.stopPropagation();
                    const action = item.dataset.action;
                    const reviewId = optionsContainer.dataset.reviewId;
                    console.log(`Acción: ${action} para reseña ID: ${reviewId}`);

                    if (action === 'modify') {
                        const reviewToEdit = reviews.find(r => r.id.toString() === reviewId);
                        if (reviewToEdit) {
                            showEditReviewModal(reviewToEdit);
                        } else {
                            showErrorModal('No se encontró la reseña para modificar.');
                        }
                    } else if (action === 'delete') {
                        if (confirm('¿Estás seguro de que querés eliminar esta reseña?')) {
                            fetch(`/reviews/${reviewId}`, {
                                method: "DELETE",
                                headers: {
                                    "Authorization": "Bearer " + localStorage.getItem("accessToken")
                                }
                            })
                                .then(res => {
                                    if (res.ok) {
                                        location.reload(); // 🔁 Recarga la página completa
                                    } else {
                                        return res.json().then(err => {
                                            throw new Error(err.error || "Error al eliminar reseña");
                                        });
                                    }
                                })
                                .catch(err => {
                                    showErrorModal(err.message || "No se pudo eliminar la reseña.");
                                });
                        }
                    }

                    dropdownMenu.classList.remove('show');
                });
            });
        } else if (optionsButton) {
            optionsButton.style.display = 'none';
        }
    });

    // Cerrar el menú si se hace clic fuera
    document.addEventListener('click', (event) => {
        document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
            const optionsContainer = menu.closest('.options');
            if (optionsContainer && !optionsContainer.contains(event.target)) {
                menu.classList.remove('show');
            }
        });
    });
}
