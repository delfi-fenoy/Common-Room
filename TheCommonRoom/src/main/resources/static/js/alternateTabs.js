const tabs = document.querySelectorAll(".tab");
const synopsisBox = document.getElementById("synopsis");
const detailsBox = document.getElementById("details");

tabs.forEach((tab) => {
  tab.addEventListener("click", () => {
    tabs.forEach((t) => {
      t.classList.remove("selected", "synopsis-tab", "details-tab");
      // Si es el tab "description" lo marco como no seleccionado synopsis-tab
      if (t.dataset.tab === "description") {
        t.classList.add("synopsis-tab");
      } else {
        t.classList.add("details-tab");
      }
    });

    tab.classList.add("selected");
    if (tab.dataset.tab === "description") {
      tab.classList.add("synopsis-tab");
      synopsisBox.style.display = "block";
      detailsBox.style.display = "none";
    } else {
      tab.classList.add("details-tab");
      synopsisBox.style.display = "none";
      detailsBox.style.display = "block";
    }
  });
});
