document.getElementById("form-register").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        username: document.getElementById("new-username").value,
        email: document.getElementById("new-email").value,
        password: document.getElementById("new-password").value,
        profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
    };

    fetch("/auth/register", { // corregido el endpoint
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("Error en el registro");
            return res.json();
        })
        .then(data => {
            // Guardar los tokens al registrarse (opcional)
            localStorage.setItem('accessToken', data.token);
            localStorage.setItem('refreshToken', data.refreshToken);

            alert("Registrado con éxito");
            flipCard(); // Mostrar formulario de login
        })
        .catch(err => alert(err.message));
});
