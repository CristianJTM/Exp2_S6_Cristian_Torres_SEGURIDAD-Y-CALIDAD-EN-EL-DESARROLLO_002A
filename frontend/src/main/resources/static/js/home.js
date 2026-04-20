document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');


    document.getElementById('btnLogin').style.display = 'none';
    document.getElementById('btnAdoptions').style.display = 'none';
    document.getElementById('btnPets').style.display = 'none';
    document.getElementById('btnLogout').style.display = 'none';

    if (token) {
        // Usuario Logeado
        document.getElementById('btnAdoptions').style.display = 'inline-block';
        document.getElementById('btnPets').style.display = 'inline-block';
        document.getElementById('btnLogout').style.display = 'inline-block';
    } else {
        // Usuario No Logeado
        document.getElementById('btnLogin').style.display = 'inline-block';
    }

    document.getElementById('btnLogout').addEventListener('click', function() {
        localStorage.removeItem('jwtToken');
        window.location.reload();
    });
});
