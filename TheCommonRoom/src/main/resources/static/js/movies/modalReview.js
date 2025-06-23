// =================== Variables globales =================== //
let selectedRating = 0;
let currentMovieId = null;
let currentMovieYear = null;

// =================== Mostrar modal de review =================== //
function showReviewModal(movieId, title, posterUrl, year) {
    const modal = document.getElementById('review-modal');
    const modalBox = modal.querySelector('.review-modal-box');

    currentMovieId = movieId;
    currentMovieYear = year;
    selectedRating = 0;

    // Seteo de datos en el modal
    document.getElementById('review-modal-title').textContent = 'Review +';
    document.getElementById('review-movie-poster').src = posterUrl || '/img/default-poster.jpg';
    document.getElementById('review-movie-title').textContent = title;
    document.getElementById('review-movie-year').textContent = year ? `(${year})` : '';
    document.getElementById('review-comment').value = '';

    // Renderizamos las estrellas con rating 0 (vacías)
    renderStars(selectedRating);

    // Mostrar modal con animación
    modal.style.display = 'flex';
    modalBox.classList.remove('animate-out');
    void modalBox.offsetWidth; // reflow para reiniciar animación
    modalBox.classList.add('animate-in');
}

// =================== Cerrar modal de review =================== //
function closeReviewModal() {
    const modal = document.getElementById('review-modal');
    const modalBox = modal.querySelector('.review-modal-box');

    modalBox.classList.remove('animate-in');
    modalBox.classList.add('animate-out');

    modalBox.addEventListener('animationend', () => {
        modal.style.display = 'none';
        modalBox.classList.remove('animate-out');
    }, { once: true });
}

// =================== RENDER ESTRELLAS FUNCIONAL CON TU CSS =================== //
function renderStars(ratingToDisplay) {
    const container = document.getElementById("star-container");
    container.innerHTML = "";

    for (let i = 1; i <= 5; i++) {
        const star = document.createElement("span");
        star.className = "review-star";
        star.innerHTML = "★";
        star.dataset.value = i;

        // Aplica clases de estilo
        if (ratingToDisplay >= i) {
            star.classList.add("star-full");
        } else if (ratingToDisplay > (i - 1) && ratingToDisplay < i) {
            star.classList.add("star-half");
        } else {
            star.classList.add("star-empty");
        }

        // Hover para previsualizar media estrella
        star.onmousemove = (event) => {
            const rect = star.getBoundingClientRect();
            const x = event.clientX - rect.left;
            const tempRating = x < rect.width / 2 ? i - 0.5 : i;
            updateStarsPreview(tempRating);
        };

        // Mouse fuera
        star.onmouseleave = () => updateStarsPreview(selectedRating);

        // Click para guardar
        star.onclick = (event) => {
            const rect = star.getBoundingClientRect();
            const x = event.clientX - rect.left;
            selectedRating = x < rect.width / 2 ? i - 0.5 : i;
            renderStars(selectedRating);
        };

        container.appendChild(star);
    }
}

// =================== PREVIEW EN HOVER =================== //
function updateStarsPreview(previewRating) {
    const container = document.getElementById("star-container");
    const stars = container.children;

    for (let i = 0; i < stars.length; i++) {
        const star = stars[i];
        const starValue = i + 1;
        star.classList.remove("star-full", "star-half", "star-empty");

        if (previewRating >= starValue) {
            star.classList.add("star-full");
        } else if (previewRating > (starValue - 1) && previewRating < starValue) {
            star.classList.add("star-half");
        } else {
            star.classList.add("star-empty");
        }
    }
}

// =================== Enviar review =================== //
function submitReview() {
    if (selectedRating === 0) {
        showErrorModal("El rating es obligatorio");
        return;
    }

    const comment = document.getElementById('review-comment').value.trim();
    const token = localStorage.getItem("accessToken");

    const reviewData = {
        movieId: currentMovieId,
        rating: selectedRating,
        comment: comment
    };

    fetch('/reviews', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(reviewData),
    })
        .then(response => {
            if (!response.ok) throw new Error('Error al enviar la reseña');
            return response.json();
        })
        .then(data => {
            closeReviewModal();
            if (typeof loadReviewsForMovie === 'function') {
                loadReviewsForMovie(currentMovieId);
            }
        })
        .catch(err => {
            showErrorModal(err.message || 'Error desconocido');
        });
}

// =================== Inicialización =================== //
document.addEventListener('DOMContentLoaded', () => {
    renderStars(selectedRating);

    const addReviewBtn = document.getElementById('add-review-btn');
    if (addReviewBtn) {
        addReviewBtn.addEventListener('click', () => {
            const movieId = addReviewBtn.dataset.movieId;
            const title = addReviewBtn.dataset.movieTitle;
            const posterUrl = addReviewBtn.dataset.moviePoster;
            const year = addReviewBtn.dataset.movieYear;
            showReviewModal(movieId, title, posterUrl, year);
        });
    }

    const closeBtn = document.querySelector('.review-modal-close');
    if (closeBtn) {
        closeBtn.addEventListener('click', closeReviewModal);
    }

    const submitBtn = document.querySelector('.review-btn-submit');
    if (submitBtn) {
        submitBtn.addEventListener('click', submitReview);
    }
});
