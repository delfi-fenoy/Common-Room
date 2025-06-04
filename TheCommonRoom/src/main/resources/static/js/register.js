document.getElementById("form-registro").addEventListener("submit", function (e) {
  e.preventDefault();

  const username = e.target.username.value;
  const email = e.target.email.value;
  const password = e.target.password.value;

  fetch("http://localhost:8080/users", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username, email, password })
  })
  .then(response => {
    if (!response.ok) {
      return response.text().then(text => { throw new Error(text) });
    }
    return "Usuario registrado correctamente";
  })
  .then(msg => {
    document.getElementById("mensaje").innerText = msg;
    e.target.reset();
  })
  .catch(error => {
    document.getElementById("mensaje").innerText = "Error: " + error.message;
  });
});
