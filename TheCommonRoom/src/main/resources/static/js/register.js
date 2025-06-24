// =================== EVENTO DE ENVÍO DEL FORMULARIO =================== //
document.getElementById("form-register").addEventListener("submit", function (e) {
    e.preventDefault();

    // =================== ARMADO DE DATOS =================== //
    const data = {
        username: document.getElementById("new-username").value,
        email: document.getElementById("new-email").value,
        password: document.getElementById("new-password").value,
        profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
    };

    // =================== VALIDACIONES =================== //
    if (!data.username.trim()) {
        showErrorModal("El campo 'Username' está incompleto.");
        return;
    }

    if (!data.email.trim()) {
        showErrorModal("El campo 'Email' está incompleto.");
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(data.email)) {
        showErrorModal("El email ingresado no es válido.");
        return;
    }

    if (!data.password.trim()) {
        showErrorModal("El campo 'Password' está incompleto.");
        return;
    }

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
                const err = await res.json();
                throw new Error(err.username || err.email || err.error || "Error en el registro");
            }
            return res.json();
        })
        .then(data => {
            if (!data.access_token || !data.refresh_token) {
                showErrorModal("La respuesta no contenía tokens de acceso");
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