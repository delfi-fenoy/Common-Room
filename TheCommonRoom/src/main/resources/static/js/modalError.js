function showErrorModal(message) {
    const modal = document.getElementById('error-modal');
    const messageContainer = document.getElementById('error-message');
    messageContainer.textContent = message;
    modal.style.display = 'flex';  // Centrar con CSS
}

function closeErrorModal() {
    document.getElementById('error-modal').style.display = 'none';
}
