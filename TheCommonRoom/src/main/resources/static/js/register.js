document.getElementById("form-register").addEventListener("submit", function (e) {
  e.preventDefault();

  const data = {
    username: document.getElementById("new-username").value,
    email: document.getElementById("new-email").value,
    password: document.getElementById("new-password").value,
    profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
  };

  fetch("/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
      .then(res => {
        if (!res.ok) throw new Error("Error en el registro");
        return res.text();
      })
      .then(msg => {
        alert("Registrado con éxito");
        flipCard();  // Acá hace el flip para mostrar el login
      })
      .catch(err => alert(err.message));
});
