// POSIBLEMENTE BORRAR, PREGUNTAR

const form = document.querySelector('.login-box');

form.addEventListener('submit', e => {
    e.preventDefault();

    const data = {
        username: form.username.value,
        password: form.password.value,
    };

    fetch('/auth/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data),
        credentials: 'include'  // muy importante para que la sesión se mantenga
    })
        .then(res => {
            if(res.ok) {
                window.location.href = '/home'; // redirigir para que se vea el header actualizado
            } else {
                return res.json().then(err => {
                    alert(err.message || 'Error en login');
                });
            }
        });
});
