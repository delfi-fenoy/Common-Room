// =================== Variables globales =================== //
// Rating seleccionado y datos actuales de la película
let selectedRating = 0;
let currentMovieId = null;
let currentMovieYear = null;


// =================== Mostrar y ocultar modal =================== //
// Abre el modal de reseña con datos precargados
function showReviewModal(movieId, title, posterUrl, year) {
    const modal = document.getElementById("review-modal");
    const box = modal.querySelector(".modal-review-box");

    // Reinicia animaciones y hace visible el modal
    box.classList.remove("animate-out");
    void box.offsetWidth;

    // Rellena datos de película
    document.getElementById("review-movie-title").textContent = title;
    document.getElementById("review-movie-year").textContent = `(${year || '20XX'})`;
    document.getElementById("review-movie-poster").src = posterUrl || "/img/default-poster.jpg";
    document.getElementById("review-comment").value = "";
    document.getElementById("review-comment").placeholder = "";

    // Guarda datos y reinicia estrellas
    selectedRating = 0;
    currentMovieId = movieId;
    currentMovieYear = year;
    renderStars(selectedRating);

    // Muestra el modal
    modal.style.display = "flex";
    box.classList.add("animate-in");
}

// Cierra el modal con animación de salida
function closeReviewModal() {
    const modal = document.getElementById("review-modal");
    const box = modal.querySelector(".modal-review-box");

    box.classList.remove("animate-in");
    box.classList.add("animate-out");

    box.addEventListener("animationend", () => {
        modal.style.display = "none";
        box.classList.remove("animate-out");
    }, { once: true });
}


// =================== Estrellas (rating) =================== //
// Genera las estrellas según el rating actual
function renderStars(ratingToDisplay) {
    const container = document.getElementById("star-container");
    container.innerHTML = "";

    for (let i = 1; i <= 5; i++) {
        const star = document.createElement("span");
        star.className = "star";
        star.innerHTML = "★";
        star.dataset.value = i;

        // Determina estado visual (llena, media o vacía)
        if (ratingToDisplay >= i) {
            star.classList.add("star-full");
        } else if (ratingToDisplay > (i - 1) && ratingToDisplay < i) {
            star.classList.add("star-half");
        } else {
            star.classList.add("star-empty");
        }

        // Hover - previsualiza
        star.onmousemove = (event) => {
            const rect = star.getBoundingClientRect();
            const x = event.clientX - rect.left;
            const tempRating = x < rect.width / 2 ? i - 0.5 : i;
            updateStarsPreview(tempRating);
        };

        // Mouse fuera - vuelve al rating guardado
        star.onmouseleave = () => updateStarsPreview(selectedRating);

        // Click - guarda el rating definitivo
        star.onclick = (event) => {
            const rect = star.getBoundingClientRect();
            const x = event.clientX - rect.left;
            selectedRating = x < rect.width / 2 ? i - 0.5 : i;
            renderStars(selectedRating);
        };

        container.appendChild(star);
    }
}

// Actualiza visualmente las estrellas (sin guardar)
function updateStarsPreview(previewRating) {
    const stars = document.querySelectorAll('#star-container .star');
    stars.forEach((star) => {
        const starValue = parseInt(star.dataset.value);
        star.classList.remove("star-full", "star-half", "star-empty");

        if (previewRating >= starValue) {
            star.classList.add("star-full");
        } else if (previewRating > (starValue - 1) && previewRating < starValue) {
            star.classList.add("star-half");
        } else {
            star.classList.add("star-empty");
        }
    });
}


// =================== Enviar reseña =================== //
// Envia los datos al backend con fetch
function submitReview() {
    if (selectedRating === 0) {
        showErrorModal("El rating es obligatorio");
        return;
    }

    const comment = document.getElementById("review-comment").value.trim();
    const token = localStorage.getItem("accessToken");

    fetch("/reviews", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            rating: selectedRating,
            comment: comment || null,
            movieId: currentMovieId
        })
    })
        .then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    // Si es un error esperado del back, mostramos el mensaje que vino
                    if (err && err.error) {
                        showErrorModal(err.error);
                    } else if (err && err.message) {
                        showErrorModal(err.message);
                    } else {
                        showErrorModal("Error desconocido al enviar la reseña");
                    }
                    throw new Error("Error al enviar reseña");
                });
            }
            return res.json();
        })
        .then(() => {
            closeReviewModal();
            location.reload();
        })
        .catch(err => {
            console.error("Error en submitReview:", err);
        });
}


// =================== Modal de error reutilizable =================== //
// Abre el modal de error con un mensaje
function showErrorModal(message) {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.modal-box');
    const messageContainer = document.getElementById('error-message');

    modalBox.classList.remove('animate-out');
    void modalBox.offsetWidth;

    messageContainer.textContent = message;
    modal.style.display = 'flex';
    modalBox.classList.add('animate-in');
}

// Cierra el modal de error
function closeErrorModal() {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.modal-box');

    modalBox.classList.remove('animate-in');
    modalBox.classList.add('animate-out');

    modalBox.addEventListener('animationend', () => {
        modal.style.display = 'none';
        modalBox.classList.remove('animate-out');
    }, { once: true });
}
