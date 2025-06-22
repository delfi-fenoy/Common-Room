const profileTabs = document.querySelectorAll(".tab");
const reviewsBox = document.getElementById("reviews");
const listsBox = document.getElementById("lists");

profileTabs.forEach((tab) => {
    tab.addEventListener("click", () => {
        profileTabs.forEach((t) => {
            t.classList.remove("selected", "reviews-tab", "lists-tab");
            if (t.dataset.tab === "reviews") {
                t.classList.add("reviews-tab");
            } else {
                t.classList.add("lists-tab");
            }
        });

        tab.classList.add("selected");

        if (tab.dataset.tab === "reviews") {
            tab.classList.add("reviews-tab");
            reviewsBox.style.display = "block";
            listsBox.style.display = "none";
        } else {
            tab.classList.add("lists-tab");
            reviewsBox.style.display = "none";
            listsBox.style.display = "block";
        }
    });
});
