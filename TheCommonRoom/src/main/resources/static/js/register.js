document.getElementById("form-register").addEventListener("submit", function (e) {
    e.preventDefault(); // Evita que la página recargue al hacer click en "Register"

    // ================ Se arma el objeto de datos para enviar al backend ================ \\
    const data = {
        username: document.getElementById("new-username").value,
        email: document.getElementById("new-email").value,
        password: document.getElementById("new-password").value,
        profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
    };

    // ================ Se hace la solicitud de registro al backend ================ \\
    fetch("/auth/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) {
                // Mostramos el modal con el mensaje de error
                showErrorModal("Error en el registro");
                throw new Error('Error en credenciales');
            }
            return res.json();
        })
        .then(data => {
            // ================ Verificamos que lleguen los tokens ================ \\
            if (!data.access_token || !data.refresh_token) {
                showErrorModal("La respuesta no contenía tokens de acceso");
                throw new Error('Token inválido');
            }

            // ================ Mostramos mensaje de éxito al usuario ================ \\
            alert("¡Registro exitoso!");

            // ================ Guardamos los tokens y vamos al Home inmediatamente ================ \\
            localStorage.setItem('accessToken', data.access_token);
            localStorage.setItem('refreshToken', data.refresh_token);
            window.location.href = '/home';
        })
        .catch(err => {showErrorModal(err.message);})
});
