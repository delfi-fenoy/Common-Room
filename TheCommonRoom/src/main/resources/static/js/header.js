document.addEventListener("DOMContentLoaded", () => {
    const userButtons = document.getElementById("user-buttons");
    const token = localStorage.getItem("accessToken");

    // =================== Si NO hay token, mostrar Sign in / Register =================== \\
    if (!token) {
        userButtons.innerHTML = `
            <a href="/signin" class="user-section" title="Iniciar sesión">
                <span class="sign-in-text">Sign in</span>
            </a>
            <a href="/signin?register=true" class="user-section" title="Registrarse">
                <span class="sign-in-text">Register</span>
            </a>
        `;
        return;
    }

    // =================== Si hay token, pedir datos del usuario actual =================== \\
    fetch("/users/me", {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Token inválido");
            return res.json();
        })
        .then(user => {
            // Usuario autenticado: mostrar Perfil + Logout
            userButtons.innerHTML = `
            <a href="/profile/${user.username}" class="user-section user-profile" title="Perfil de usuario">
                <img src="${user.profilePictureUrl || '/img/user.png'}" alt="Foto de ${user.username}" class="user-photo"/>
                <span>${user.username}</span>
            </a>
            <button id="logout-btn" class="user-section" title="Cerrar sesión">
                <span class="sign-in-text">Logout</span>
            </button>
        `;

            // Acción de logout | Ahora si funcionara
            document.getElementById("logout-btn").addEventListener("click", () => {
                const token = localStorage.getItem("accessToken");

                fetch("/logout", {
                    method: "POST",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                })
                    .finally(() => {
                        localStorage.removeItem("accessToken");
                        localStorage.removeItem("refreshToken");
                        window.location.href = "/signin";
                    });
            });

        })
        .catch(() => {
            // 4️⃣ Token vencido o inválido: limpiar y mostrar opciones para no logueados
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            location.reload();
        });
});

function isValidJwt(token) {
    return typeof token === 'string' && token.split('.').length === 3;
}

const token = localStorage.getItem("accessToken");
if (!isValidJwt(token)) {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    // Mostrar botones de login
}

