// =================== VARIABLES GLOBALES =================== //
let originalUsername = null;

// Función de utilidad para mostrar errores (asegúrate de que esta función exista en tu código)
function showErrorModal(message) {
    alert("Error: " + message); // Puedes reemplazar esto con tu propia implementación de modal de error
}

// =================== ABRIR MODAL =================== //
function showEditProfileModal() {
    const modal = document.getElementById("edit-profile-modal");
    modal.style.display = "flex";

    // Tomar valores actuales del perfil para placeholders
    // Asegúrate de que estos selectores (".user-username", etc.) existan en tu HTML principal
    const username = document.querySelector(".user-username")?.textContent?.trim() || "";
    const description = document.querySelector(".user-description")?.textContent?.trim() || "";
    const profilePictureUrl = document.querySelector(".user-picture")?.getAttribute("src") || "";

    originalUsername = username;

    // Limpiar inputs y setear placeholders
    const inputUsername = document.getElementById("edit-username");
    const inputEmail = document.getElementById("edit-email");
    const inputDescription = document.getElementById("edit-description");
    const inputPicture = document.getElementById("edit-picture");

    inputUsername.value = "";
    inputUsername.placeholder = "Actual: " + username;

    inputEmail.value = "";
    inputEmail.placeholder = "Actual: (no visible)"; // Si querés podés traer email del backend con fetch

    inputDescription.value = "";
    inputDescription.placeholder = "Actual: " + description;

    inputPicture.value = "";
    inputPicture.placeholder = "Actual: " + profilePictureUrl;

    // Limpiar campos de contraseña cada vez que se abre el modal
    document.getElementById("current-password").value = "";
    document.getElementById("new-password").value = "";
    document.getElementById("confirm-password").value = "";
}

// =================== CERRAR MODAL =================== //
function closeEditProfileModal() {
    document.getElementById("edit-profile-modal").style.display = "none";
    document.getElementById("editModalCard").classList.remove("flipped");
}

// =================== GIRAR MODAL =================== //
function flipEditModal() {
    document.getElementById("editModalCard").classList.toggle("flipped");
}

// =================== GUARDAR CAMBIOS DE PERFIL =================== //
// Escuchamos el click en el botón "Confirm" del modal frontal
document.querySelector(".edit-modal-front .confirm-button").addEventListener("click", async function (e) {
    e.preventDefault(); // Prevenimos el comportamiento por defecto del botón si lo tuviera

    const inputUsername = document.getElementById("edit-username");
    const inputEmail = document.getElementById("edit-email");
    const inputDescription = document.getElementById("edit-description");
    const inputPicture = document.getElementById("edit-picture");

    const username = inputUsername.value.trim();
    const email = inputEmail.value.trim();
    const description = inputDescription.value.trim();
    const profilePictureUrl = inputPicture.value.trim();

    const data = {};
    // Solo enviar los campos si han sido modificados (no vacíos y diferentes al placeholder, si aplica)
    if (username !== "" && username !== inputUsername.placeholder.replace("Actual: ", "")) data.username = username;
    if (email !== "" && email !== inputEmail.placeholder.replace("Actual: ", "")) data.email = email;
    if (description !== "" && description !== inputDescription.placeholder.replace("Actual: ", "")) data.description = description;
    if (profilePictureUrl !== "" && profilePictureUrl !== inputPicture.placeholder.replace("Actual: ", "")) data.profilePictureUrl = profilePictureUrl;

    // Si no hay datos para enviar, simplemente cerramos el modal
    if (Object.keys(data).length === 0) {
        alert("No se realizaron cambios.");
        closeEditProfileModal();
        return;
    }

    try {
        const res = await fetch(`/users/${originalUsername}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            const responseData = await res.json().catch(() => null);

            alert("Perfil actualizado correctamente");
            closeEditProfileModal();
            if (responseData && responseData.username && responseData.username !== originalUsername) {
                if (responseData.token) localStorage.setItem("accessToken", responseData.data.access_toke);
                if (responseData.refreshToken) localStorage.setItem("refreshToken", responseData.data.refresh_token);
                window.location.href = `/profile/${responseData.username}`;
            } else {
                location.reload();
            }
        } else {
            const err = await res.json();
            showErrorModal(err.username || err.email || err.error || "Error al guardar los cambios");
        }
    } catch (error) {
        console.error("Fetch error:", error);
        showErrorModal("Error de conexión");
    }
});

// =================== CAMBIAR CONTRASEÑA =================== //
// Escuchamos el click en el botón "Confirm" del modal trasero
document.querySelector(".edit-modal-back .confirm-button").addEventListener("click", async function (e) {
    e.preventDefault(); // Prevenimos el comportamiento por defecto del botón si lo tuviera

    const current = document.getElementById("current-password").value.trim();
    const nueva = document.getElementById("new-password").value.trim();
    const confirmar = document.getElementById("confirm-password").value.trim();

    if (nueva !== confirmar) {
        showErrorModal("Las contraseñas no coinciden");
        return;
    }

    // Validación básica para evitar envío de contraseñas vacías
    if (current === "" || nueva === "" || confirmar === "") {
        showErrorModal("Por favor, rellena todos los campos de contraseña.");
        return;
    }

    const data = {
        oldPassword: current,
        newPassword: nueva,
        confirmPassword: confirmar
    };

    try {
        const res = await fetch(`/users/${originalUsername}/change-password`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert("Contraseña cambiada correctamente");
            closeEditProfileModal();
            location.reload();
        } else {
            const err = await res.json();
            showErrorModal(err.password || err.error || "Error al cambiar la contraseña");
        }
    } catch (error) {
        console.error("Fetch error:", error);
        showErrorModal("Error de conexión");
    }
});

// =================== ELIMINAR PERFIL (Opcional, si agregaste este botón) =================== //
// Asegúrate de que este botón tenga el ID o la clase correcta en tu HTML
document.querySelector(".delete-profile-button").addEventListener("click", async function (e) {
    e.preventDefault();

    if (!confirm("¿Estás seguro de que quieres eliminar tu perfil? Esta acción es irreversible.")) {
        return;
    }

    try {
        const res = await fetch(`/users/${originalUsername}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        });

        if (res.ok) {
            alert("Perfil eliminado correctamente.");
            // Redirigir a la página de inicio o logout
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            window.location.href = "/"; // O a tu página de login/registro
        } else {
            const err = await res.json();
            showErrorModal(err.error || "Error al eliminar el perfil.");
        }
    } catch (error) {
        console.error("Fetch error:", error);
        showErrorModal("Error de conexión al eliminar el perfil.");
    }
});