// =================== VALIDACIÓN DE TOKEN JWT =================== //
// Verifica si un string tiene formato válido de JWT
function isValidJwt(token) {
    return typeof token === 'string' && token.split('.').length === 3;
}

// =================== VALIDAR CAMPOS VACÍOS =================== //
// Reutilizable para evitar repetir lógica
function validarCampoVacio(valor, mensaje) {
    if (!valor || !valor.trim()) {
        showErrorModal(mensaje);
        return false;
    }
    return true;
}

// =================== ENVÍO DEL FORMULARIO DE LOGIN =================== //
const loginForm = document.querySelector('.login-box');
if (loginForm) {
    loginForm.addEventListener('submit', e => {
        e.preventDefault();

        const data = {
            username: loginForm.username.value,
            password: loginForm.password.value,
        };

        // Validaciones básicas
        if (!validarCampoVacio(data.username, "El campo 'Username' está incompleto.")) return;
        if (!validarCampoVacio(data.password, "El campo 'Password' está incompleto.")) return;

        const submitBtn = loginForm.querySelector('button[type="submit"]');
        submitBtn.disabled = true;

        // Envío al backend
        fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(async res => {
                if (!res.ok) {
                    try {
                        const err = await res.json();
                        throw new Error(err.error || "Credenciales inválidas");
                    } catch {
                        throw new Error("Error inesperado en la respuesta del servidor");
                    }
                }
                return res.json();
            })
            .then(data => {
                // Validación de tokens
                if (!isValidJwt(data.access_token)) {
                    showErrorModal("Access token inválido recibido");
                    throw new Error("Access token inválido");
                }
                if (!isValidJwt(data.refresh_token)) {
                    showErrorModal("Refresh token inválido recibido");
                    throw new Error("Refresh token inválido");
                }

                // Guardar tokens y redirigir
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
}