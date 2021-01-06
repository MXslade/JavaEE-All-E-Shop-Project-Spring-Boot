let panels = [
    "shop_items_table",
    "brands_table",
    "countries_table",
    "categories_table",
    "shop_item_add_form",
    "brand_add_form",
    "country_add_form",
    "category_add_form",
    "users_table",
    "user_add_form",
    "payments_table",
]

function showAllItems() {
    hideEverything();

    let allItemsTable = document.getElementById("shop_items_table");
    allItemsTable.setAttribute("class", "table table-striped");
}

function showAllBrands() {
    hideEverything();

    let panel = document.getElementById("brands_table");
    panel.setAttribute("class", "table table-striped");
}

function showAllCountries() {
    hideEverything();

    let panel = document.getElementById("countries_table");
    panel.setAttribute("class", "table table-striped");
}

function showAllCategories() {
    hideEverything();

    let panel = document.getElementById("categories_table");
    panel.setAttribute("class", "table table-striped");
}

function showAllUsers() {
    hideEverything();

    let panel = document.getElementById("users_table");
    panel.setAttribute("class", "table table-striped");
}

function showAllPayments() {
    hideEverything();

    let panel = document.getElementById("payments_table");
    panel.setAttribute("class", "table table-striped");
}

function showAddItemForm() {
    hideEverything();

    let addItemForm = document.getElementById("shop_item_add_form");
    addItemForm.setAttribute("class", "row justify-content-center");
}

function showAddBrandForm() {
    hideEverything();

    let addBrandFrom = document.getElementById("brand_add_form");
    addBrandFrom.setAttribute("class", "row justify-content-center");
}

function showAddCountryForm() {
    hideEverything();

    let addCountryForm = document.getElementById("country_add_form");
    addCountryForm.setAttribute("class", "row justify-content-center");
}

function showAddCategoryForm() {
    hideEverything();

    let addCategoryForm = document.getElementById("category_add_form");
    addCategoryForm.setAttribute("class", "row justify-content-center");
}

function showAddUserForm() {
    hideEverything();

    let addCategoryForm = document.getElementById("user_add_form");
    addCategoryForm.setAttribute("class", "row justify-content-center");
}

function hideEverything() {
    panels.forEach(item => {
        try {
            let panel = document.getElementById(item);
            panel.setAttribute("class", "d-none");
        } catch (e) {
            console.log(e);
        }
    });
}
