document.addEventListener('DOMContentLoaded', function () {
    const elementos = {
        tablaUsuarios: document.querySelector('#tabla-usuarios'),
        btnAgregar: document.querySelector('#btn-agregar'),
        inputNombre: document.querySelector('#usuario-nombre'),
        inputemail: document.querySelector('#usuario-email'),
        inputpassword: document.querySelector('#usuario-password'),
        inputRol: document.querySelector('#usuario-rol'),
        inputId: document.querySelector('#usuario-id')
    };

    // Cargar todos los usuarios al iniciar
    cargarUsuarios();

    // Evento para agregar o editar usuario
    elementos.btnAgregar.addEventListener('click', function () {
        const id = elementos.inputId.value;
        const nombre = elementos.inputNombre.value;
        const email = elementos.inputemail.value;
        const password = elementos.inputpassword.value;
        const rol = elementos.inputRol.value;

        if (!nombre || !email || !password || !rol) {
            alert('Por favor, complete todos los campos.');
            return;
        }

        if (id) {
            // Editar usuario
            editarUsuario(id, { nombre, email, password, rol });
        } else {
            // Agregar nuevo usuario
            agregarUsuario({ nombre, email, password, rol });
        }
    });

    // Cargar todos los usuarios
    function cargarUsuarios() {
        fetch('/api/usuarios')
            .then(response => response.json())
            .then(data => {
                elementos.tablaUsuarios.innerHTML = '';
                data.forEach(usuario => {
                    const fila = document.createElement('tr');
                    fila.innerHTML = `
                        <td class="px-6 py-4 border-b">${usuario.id}</td>
                        <td class="px-6 py-4 border-b">${usuario.nombre}</td>
                        <td class="px-6 py-4 border-b">${usuario.email}</td>
                        <td class="px-6 py-4 border-b">${usuario.rol}</td>
                        <td class="px-6 py-4 border-b">
                            <button class="text-blue-600 hover:text-blue-900 mr-3 editar-usuario" data-id="${usuario.id}"><i class="fas fa-edit"></i></button>
                            <button class="text-red-600 hover:text-red-900 eliminar-usuario" data-id="${usuario.id}"><i class="fas fa-trash-alt"></i></button>
                        </td>
                    `;
                    elementos.tablaUsuarios.appendChild(fila);
                });

                // Asignar eventos a los botones
                asignarEventosEditar();
                asignarEventosEliminar();
            });
    }

    // Agregar nuevo usuario
    function agregarUsuario(usuario) {
        fetch('/api/usuarios', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        })
            .then(response => {
                if (response.ok) {
                    cargarUsuarios();
                    resetForm();
                } else {
                    alert('Error al agregar el usuario.');
                }
            });
    }

    // Editar usuario
    function editarUsuario(id, usuario) {
        fetch(`/api/usuarios/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        })
            .then(response => {
                if (response.ok) {
                    cargarUsuarios();
                    resetForm();
                } else {
                    alert('Error al editar el usuario.');
                }
            });
    }

    // Eliminar usuario
    function eliminarUsuario(id) {
        if (!id) {
            console.error('ID inválido para eliminar el usuario');
            return;
        }

        if (confirm(`¿Está seguro de que desea eliminar el usuario con ID ${id}?`)) {
            fetch(`/api/usuarios/${id}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        cargarUsuarios();
                    } else {
                        console.error('Error al eliminar el usuario:', response.statusText);
                    }
                })
                .catch(error => {
                    console.error('Error en la solicitud:', error);
                });
        }
    }

    // Resetear el formulario
    function resetForm() {
        elementos.inputNombre.value = '';
        elementos.inputemail.value = '';
        elementos.inputpassword.value = '';
        elementos.inputRol.value = '';
        elementos.inputId.value = '';
    }

    // Asignar eventos a los botones de editar
    function asignarEventosEditar() {
        document.querySelectorAll('.editar-usuario').forEach(btn => {
            btn.addEventListener('click', function () {
                const id = this.getAttribute('data-id');
                editarUsuarioModal(id);
            });
        });
    }

    // Asignar eventos a los botones de eliminar
    function asignarEventosEliminar() {
        document.querySelectorAll('.eliminar-usuario').forEach(btn => {
            btn.addEventListener('click', function () {
                const id = this.getAttribute('data-id');
                eliminarUsuario(id);
            });
        });
    }

    // Cargar datos del usuario en el formulario para editar
    function editarUsuarioModal(id) {
        fetch(`/api/usuarios/${id}`)
            .then(response => response.json())
            .then(usuario => {
                elementos.inputNombre.value = usuario.nombre;
                elementos.inputemail.value = usuario.email;
                elementos.inputpassword.value = '';
                elementos.inputRol.value = usuario.rol;
                elementos.inputId.value = usuario.id;
            });
    }
});
