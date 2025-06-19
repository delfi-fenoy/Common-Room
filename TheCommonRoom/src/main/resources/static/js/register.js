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
            // Si la respuesta NO es OK, lanzamos error
            if (!res.ok) throw new Error("Error en el registro");
            return res.json();
        })
        .then(data => {
            // ================ Verificamos que lleguen los tokens ================ \\
            if (!data.access_token || !data.refresh_token) {
                throw new Error("La respuesta no contenía tokens de acceso");
            }

            // ================ Mostramos mensaje de éxito al usuario ================ \\
            alert("¡Registro exitoso!");

            // ================ Después de 2 segundos guardamos los tokens y vamos al Home ================ \\
            setTimeout(() => {
                localStorage.setItem('accessToken', data.access_token);
                localStorage.setItem('refreshToken', data.refresh_token);
                window.location.href = '/home';
            }, 2000);
        })
        .catch(err => {
            // Si hubo error, mostrar alert
            alert(err.message);
        });
});
