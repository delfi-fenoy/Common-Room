// =================== VALIDACIÓN DE TOKEN JWT =================== //
// Comprueba si un string es un JWT válido
function isValidJwt(token) {
    return typeof token === 'string' && token.split('.').length === 3;
}

// =================== EVENTO DE ENVÍO DEL FORMULARIO =================== //
const form = document.querySelector('.login-box');
form.addEventListener('submit', e => {
    e.preventDefault();

    const data = {
        username: form.username.value,
        password: form.password.value,
    };

    // =================== VALIDACIONES BÁSICAS =================== //
    if (!data.username.trim()) {
        showErrorModal("El campo 'Username' está incompleto.");
        return;
    }

    if (!data.password.trim()) {
        showErrorModal("El campo 'Password' está incompleto.");
        return;
    }

    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.disabled = true;

    // =================== SOLICITUD AL BACKEND =================== //
    fetch('/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(async res => {
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.error || "Credenciales inválidas");
            }
            return res.json();
        })
        .then(data => {
            if (!data.access_token || !isValidJwt(data.access_token)) {
                showErrorModal("Token inválido recibido");
                throw new Error("Token inválido");
            }

            localStorage.setItem('accessToken', data.access_token);
            localStorage.setItem('refreshToken', data.refresh_token);
            window.location.href = '/home';
        })
        .catch(err => {
            showErrorModal(err.message || "Error al iniciar sesión.");
        })
        .finally(() => {
            submitBtn.disabled = false;
        });
});