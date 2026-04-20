const API_URL = "http://localhost:8081/pets";

document.addEventListener('DOMContentLoaded', function() {
const token = localStorage.getItem('jwtToken');

if (!token) {
    window.location.href = '/login';
}});


document.getElementById('petForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    const body = {
        name: document.getElementById('name').value.trim(),
        type: document.getElementById('type').value.trim(),
        age: parseInt(document.getElementById('age').value, 10),
        status: document.getElementById('status').value,
    };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(body)
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem('jwtToken');
            window.location.href = '/login';
            return;
        }

        if (!response.ok) {
            const err = await response.text();
            document.getElementById('errorPet').textContent = 'Error: ' + err;
            return;
        }

        window.location.href = '/pets';
    } catch (error) {
        document.getElementById('errorPet').textContent = 'Error al registrar mascota.';
    }
});