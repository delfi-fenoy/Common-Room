// =================== CONFIGURACIÓN =================== //
const userGrid = document.getElementById("user-grid");
const prevBtn = document.getElementById("prevPage");
const nextBtn = document.getElementById("nextPage");

let users = [];
let currentPage = 1;
const usersPerPage = 20;

// =================== FETCH DE USUARIOS =================== //
async function fetchUsers() {
    try {
        const res = await fetch("/users/all");
        if (!res.ok) throw new Error("Error al obtener usuarios");

        users = await res.json();
        renderPage();
    } catch (error) {
        userGrid.innerHTML = `<p class="text-center">No se pudieron cargar los usuarios.</p>`;
        console.error(error);
    }
}

// =================== RENDER DE PÁGINA =================== //
function renderPage() {
    userGrid.innerHTML = "";

    const start = (currentPage - 1) * usersPerPage;
    const end = start + usersPerPage;
    const usersToDisplay = users.slice(start, end);

    usersToDisplay.forEach(user => {
        const card = document.createElement("div");
        card.classList.add("user-card");

        card.innerHTML = `
            <img src="${user.profilePictureUrl || '/img/user.png'}" alt="Foto de perfil">
            <span class="username">${user.username}</span>
            ${user.role === 'ADMIN' ? `<span class="role admin">⭐</span>` : ''}
        `;

        // Agrego el evento click para redirigir al perfil
        card.addEventListener("click", () => {
            window.location.href = `/profile/${user.username}`;
        });

        userGrid.appendChild(card);
    });

    // Ocultar o mostrar botones según página
    prevBtn.style.visibility = currentPage === 1 ? "hidden" : "visible";
    nextBtn.style.visibility = end >= users.length ? "hidden" : "visible";
}

// =================== EVENTOS DE PÁGINA =================== //
prevBtn.addEventListener("click", () => {
    if (currentPage > 1) {
        currentPage--;
        renderPage();
    }
});

nextBtn.addEventListener("click", () => {
    if ((currentPage * usersPerPage) < users.length) {
        currentPage++;
        renderPage();
    }
});

// =================== INICIO =================== //
document.addEventListener("DOMContentLoaded", fetchUsers);
