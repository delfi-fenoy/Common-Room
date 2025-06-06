// Alternar entre tabs de Description y Details
const tabs = document.querySelectorAll(".tab");
const description = document.getElementById("description");
const details = document.getElementById("details");

tabs.forEach((tab) => {
  tab.addEventListener("click", () => {
    tabs.forEach((t) => t.classList.remove("selected"));
    tab.classList.add("selected");

    if (tab.dataset.tab === "description") {
      description.style.display = "block";
      details.style.display = "none";
    } else {
      description.style.display = "none";
      details.style.display = "block";
    }
  });
});
