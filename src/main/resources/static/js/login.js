document.addEventListener('DOMContentLoaded', function () {
    const formLogin = document.getElementById('form-login');
    const errorMsg = document.getElementById('error-msg');

    formLogin.addEventListener('submit', function (e) {
        e.preventDefault();

        const email = formLogin.email.value.trim();
        const password = formLogin.password.value;

        // Aquí puedes validar campos si quieres (ya están requeridos en HTML)
        if (!email || !password) {
            mostrarError('Por favor complete todos los campos.');
            return;
        }

        // Simulación de login - reemplaza con tu API real
        fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Credenciales incorrectas');
                }
            })
            .then(data => {
                // En backend real, podrías recibir token u datos de usuario
                // Guardar flag en localStorage para simular sesión
                localStorage.setItem('usuarioLogueado', JSON.stringify(data));
                // Redirigir a página principal
                window.location.href = 'usuarios.html';
            })
            .catch(error => {
                mostrarError('Credenciales incorrectas.');
            });
    });

    function mostrarError(mensaje) {
        errorMsg.textContent = mensaje;
        errorMsg.classList.remove('hidden');
    }
});

