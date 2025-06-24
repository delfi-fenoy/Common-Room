// =================== ENVÍO DEL FORMULARIO DE REGISTRO =================== //
const registerForm = document.getElementById("form-register");
if (registerForm) {
    registerForm.addEventListener("submit", function (e) {
        e.preventDefault();

        // =================== ARMADO DE DATOS =================== //
        const data = {
            username: document.getElementById("new-username").value,
            email: document.getElementById("new-email").value,
            password: document.getElementById("new-password").value,
            profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
        };

        // =================== VALIDACIONES =================== //
        if (!validarCampoVacio(data.username, "El campo 'Username' está incompleto.")) return;
        if (!validarCampoVacio(data.email, "El campo 'Email' está incompleto.")) return;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(data.email)) {
            showErrorModal("El email ingresado no es válido.");
            return;
        }

        if (!validarCampoVacio(data.password, "El campo 'Password' está incompleto.")) return;
        if (data.password.length < 8) {
            showErrorModal("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        // =================== SOLICITUD AL BACKEND =================== //
        fetch("/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(async res => {
                if (!res.ok) {
                    try {
                        const err = await res.json();
                        throw new Error(err.username || err.email || err.error || "Error en el registro");
                    } catch {
                        throw new Error("Error inesperado en la respuesta del servidor");
                    }
                }
                return res.json();
            })
            .then(data => {
                if (!isValidJwt(data.access_token) || !isValidJwt(data.refresh_token)) {
                    showErrorModal("La respuesta no contenía tokens válidos");
                    throw new Error("Tokens inválidos");
                }

                alert("¡Registro exitoso!");
                localStorage.setItem('accessToken', data.access_token);
                localStorage.setItem('refreshToken', data.refresh_token);
                window.location.href = '/home';
            })
            .catch(err => {
                showErrorModal(err.message || "Error al registrarse.");
            });
    });
}