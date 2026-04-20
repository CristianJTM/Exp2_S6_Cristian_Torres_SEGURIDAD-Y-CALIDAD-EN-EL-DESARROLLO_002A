const API_URL = "http://localhost:8081/pets";

// 🔐 Obtener token
function getToken() {
    return localStorage.getItem("jwtToken");
}

// 🚫 Redirigir si no hay token
function checkAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = "/login";
    }
}

// 📡 Obtener mascotas
async function fetchPets() {
    console.log("TOKEN:", getToken());
    try {
        const response = await fetch(API_URL, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + getToken(),
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error("Error al obtener mascotas");
        }

        const pets = await response.json();
        renderPets(pets);

    } catch (error) {
        console.error(error);
        document.getElementById("petsMessage").textContent = "Error al cargar mascotas";
    }
}

// 🎨 Renderizar tabla
function renderPets(pets) {
    const tbody = document.getElementById("petsTableBody");
    tbody.innerHTML = "";

    if (pets.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6">No hay mascotas</td></tr>`;
        return;
    }

    pets.forEach(pet => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${pet.id}</td>
            <td>${pet.name}</td>
            <td>${pet.type}</td>
            <td>${pet.age}</td>
            <td>${pet.status}</td>
        `;

        const tdAction = document.createElement("td");

        const btn = document.createElement("button");
        btn.textContent = "Eliminar";

        btn.addEventListener("click", () => deletePet(pet.id));

        tdAction.appendChild(btn);
        row.appendChild(tdAction);

        tbody.appendChild(row);
    });
}

// 🗑️ Eliminar mascota
async function deletePet(id) {
    console.log("DELETE ID:", id);

    if (!confirm("¿Seguro que quieres eliminar esta mascota?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + getToken()
            }
        });

        console.log("STATUS:", response.status);

        if (!response.ok) {
            const text = await response.text();
            console.error("DELETE ERROR:", text);
            throw new Error("Error al eliminar mascota");
        }

        document.getElementById("petsMessage").textContent = "Mascota eliminada";
        fetchPets();

    } catch (error) {
        console.error(error);
        document.getElementById("petsMessage").textContent = "Error al eliminar mascota";
    }
}

// 🔍 Filtro simple en frontend
function filterPets(pets) {
    const type = document.getElementById("type").value.toLowerCase();
    const status = document.getElementById("status").value;
    const age = document.getElementById("age").value;

    return pets.filter(pet => {
        return (
            (!type || pet.type.toLowerCase().includes(type)) &&
            (!status || pet.status === status) &&
            (!age || pet.age == age)
        );
    });
}

// 🔄 Manejar búsqueda
async function handleSearch(event) {
    event.preventDefault();

    try {
        const response = await fetch(API_URL, {
            headers: {
                "Authorization": "Bearer " + getToken()
            }
        });

        const pets = await response.json();
        const filtered = filterPets(pets);
        renderPets(filtered);

    } catch (error) {
        console.error(error);
    }
}

// 🔄 Reset filtros
function resetFilters() {
    document.getElementById("type").value = "";
    document.getElementById("status").value = "";
    document.getElementById("age").value = "";
    fetchPets();
}

// 📋 Ver todas
function loadAll() {
    fetchPets();
}

// 🚀 INIT
document.addEventListener("DOMContentLoaded", () => {
    checkAuth();

    fetchPets();

    document.getElementById("searchForm").addEventListener("submit", handleSearch);
    document.getElementById("btnReset").addEventListener("click", resetFilters);
    document.getElementById("btnLoadAll").addEventListener("click", loadAll);
});

window.deletePet = deletePet;