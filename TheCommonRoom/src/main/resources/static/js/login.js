const form = document.querySelector('.login-box');

function isValidJwt(token) {
    // Un JWT válido tiene 3 partes separadas por puntos
    return typeof token === 'string' && token.split('.').length === 3;
}

form.addEventListener('submit', e => {
    e.preventDefault();

    const data = {
        username: form.username.value,
        password: form.password.value,
    };

    // Deshabilitar botón para evitar múltiples clicks
    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.disabled = true;

    fetch('/auth/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) {
                // Mostramos el modal con el mensaje de error
                showErrorModal("Credenciales inválidas");
                throw new Error('Error en credenciales');
            }
            return res.json();
        })
        .then(data => {
            // Validar que venga token y sea JWT válido
            if (!data.access_token || !isValidJwt(data.access_token)) {
                showErrorModal("Token inválido recibido");
                throw new Error('Token inválido');
            }

            // Guardar tokens en localStorage
            localStorage.setItem('accessToken', data.access_token);
            localStorage.setItem('refreshToken', data.refresh_token);

            // Redirigir a home
            window.location.href = '/home';
        })
        .catch(err => {showErrorModal(err.message);})
        .finally(() => {
            // Reactivar botón siempre
            submitBtn.disabled = false;
        });
});
