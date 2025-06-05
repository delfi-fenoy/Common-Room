document.getElementById("form-register").addEventListener("submit", function (e) {
  e.preventDefault();

  const data = {
    username: document.getElementById("new-username").value,
    email: document.getElementById("new-email").value,
    password: document.getElementById("new-password").value,
    profilePictureUrl: document.getElementById("new-profilePictureUrl").value || null
  };

  fetch("/users", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
    .then(res => {
      if (!res.ok) throw new Error("Error en el registro");
      return res.text(); // o .json() si devolvés un objeto
    })
    .then(msg => {
      alert("Registrado con éxito");
      window.location.href = "/login.html"; // o redirigir al home
    })
    .catch(err => alert(err.message));
});
