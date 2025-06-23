// =================== MOSTRAR MODAL ERROR =================== //
function showErrorModal(message) {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.error-modal-box');
    const messageContainer = document.getElementById('error-message');

    modalBox.classList.remove('animate-out');
    void modalBox.offsetWidth;

    messageContainer.textContent = message;
    modal.style.display = 'flex';
    modalBox.classList.add('animate-in');
}

// =================== MOSTRAR CERRAR ERROR =================== //
function closeErrorModal() {
    const modal = document.getElementById('error-modal');
    const modalBox = modal.querySelector('.error-modal-box');

    modalBox.classList.remove('animate-in');
    modalBox.classList.add('animate-out');

    modalBox.addEventListener('animationend', () => {
        modal.style.display = 'none';
        modalBox.classList.remove('animate-out');
    }, { once: true });
}
