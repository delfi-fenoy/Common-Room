function showErrorModal(message) {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.modal-box');
    const messageContainer = document.getElementById('error-message');

    // Limpiamos clases por si quedo la animacion anterior
    modalBox.classList.remove('animate-out');
    void modalBox.offsetWidth; // Reiniciar el reflow

    // Aplicamos nueva animación
    messageContainer.textContent = message;
    modal.style.display = 'flex';
    modalBox.classList.add('animate-in');
}

function closeErrorModal() {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.modal-box');

    // Revertimos animación
    modalBox.classList.remove('animate-in');
    modalBox.classList.add('animate-out');

    // Esperar a que termine la animación antes de ocultar
    modalBox.addEventListener('animationend', () => {
        modal.style.display = 'none';
        modalBox.classList.remove('animate-out');
    }, { once: true });
}
