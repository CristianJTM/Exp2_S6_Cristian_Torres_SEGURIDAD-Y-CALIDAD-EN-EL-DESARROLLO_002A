const API_ADOPTIONS = "http://localhost:8081/adoptions";
const API_PETS = "http://localhost:8081/pets";
const API_USERS = "http://localhost:8081/users";

// 🔐 token
function getToken() {
    return localStorage.getItem("jwtToken");
}

// 🚀 INIT
document.addEventListener("DOMContentLoaded", () => {
    loadPets();
    loadUsers();
    loadAdoptions();

    document.getElementById("adoptionForm")
        .addEventListener("submit", createAdoption);
});


// 📦 cargar pets disponibles
async function loadPets() {
    const res = await fetch(API_PETS, {
        headers: { "Authorization": "Bearer " + getToken() }
    });

    const pets = await res.json();

    const select = document.getElementById("petSelect");
    select.innerHTML = "";

    pets
        .filter(p => p.status === "DISPONIBLE")
        .forEach(p => {
            const opt = document.createElement("option");
            opt.value = p.id;
            opt.textContent = `${p.name} (${p.type})`;
            select.appendChild(opt);
        });
}


// 👤 cargar users
async function loadUsers() {
    const res = await fetch(API_USERS, {
        headers: { "Authorization": "Bearer " + getToken() }
    });

    const users = await res.json();

    const select = document.getElementById("userSelect");
    select.innerHTML = "";

    users.forEach(u => {
        const opt = document.createElement("option");
        opt.value = u.id;
        opt.textContent = u.username;
        select.appendChild(opt);
    });
}


// ➕ crear adopción
async function createAdoption(e) {
    e.preventDefault();

    const adoption = {
        petId: document.getElementById("petSelect").value,
        userId: document.getElementById("userSelect").value,
        date: document.getElementById("date").value,
        time: document.getElementById("time").value
    };

    const res = await fetch(API_ADOPTIONS, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + getToken()
        },
        body: JSON.stringify(adoption)
    });

    if (!res.ok) {
        document.getElementById("adoptionMessage").textContent =
            "Error al crear adopción";
        return;
    }

    document.getElementById("adoptionMessage").textContent =
        "Adopción creada correctamente";

    loadAdoptions();
}


// 📋 listar adopciones
async function loadAdoptions() {
    const res = await fetch(API_ADOPTIONS, {
        headers: { "Authorization": "Bearer " + getToken() }
    });

    const data = await res.json();

    const tbody = document.getElementById("adoptionTableBody");
    tbody.innerHTML = "";

    data.forEach(a => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${a.id}</td>
            <td>${a.user.username}</td>
            <td>${a.pet.name}</td>
            <td>${a.date}</td>
            <td>${a.time}</td>
        `;

        const td = document.createElement("td");

        const btn = document.createElement("button");
        btn.textContent = "Eliminar";

        btn.addEventListener("click", () => deleteAdoption(a.id));

        td.appendChild(btn);
        row.appendChild(td);

        tbody.appendChild(row);
    });
}


// 🗑 eliminar adopción
async function deleteAdoption(id) {
    if (!confirm("¿Eliminar adopción?")) return;

    const res = await fetch(`${API_ADOPTIONS}/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    });

    if (!res.ok) {
        alert("Error al eliminar adopción");
        return;
    }

    loadAdoptions();
}