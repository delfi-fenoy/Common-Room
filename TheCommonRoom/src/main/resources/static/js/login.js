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

    // Opcional: deshabilitar botón para evitar múltiples clicks
    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.disabled = true;

    fetch('/auth/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("Credenciales inválidas");
            return res.json();
        })
        .then(data => {
            // Validar que venga token y sea JWT válido
            if (!data.access_token || !isValidJwt(data.access_token)) {
                throw new Error("Token inválido recibido");
            }

            // Guardar tokens en localStorage
            localStorage.setItem('accessToken', data.access_token);
            localStorage.setItem('refreshToken', data.refresh_token);

            // Redirigir a home
            window.location.href = '/home';
        })
        .catch(err => alert(err.message))
        .finally(() => {
            // Reactivar botón siempre
            submitBtn.disabled = false;
        });
});
