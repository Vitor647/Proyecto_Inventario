document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('proveedorForm');
    const tabla = document.getElementById('tablaProveedores');

    // Cargar proveedores al iniciar
    cargarProveedores();

    // Manejar envío de formulario
    form.addEventListener('submit', e => {
        e.preventDefault();
        guardarProveedor();
    });

    async function cargarProveedores() {
        const response = await fetch('/api/proveedores');
        const proveedores = await response.json();

        tabla.innerHTML = '';
        proveedores.forEach(proveedor => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${proveedor.id}</td>
                <td>${proveedor.nombre}</td>
                <td>${proveedor.telefono}</td>
                <td>${proveedor.email}</td>
                <td class="actions">
                    <button onclick="editarProveedor(${proveedor.id})" class="btn btn-sm btn-warning btn-action">Editar</button>
                    <button onclick="eliminarProveedor(${proveedor.id})" class="btn btn-sm btn-danger">Eliminar</button>
                </td>
            `;
            tabla.appendChild(tr);
        });
    }

    async function guardarProveedor() {
        const proveedor = {
            nombre: document.getElementById('nombre').value,
            telefono: document.getElementById('telefono').value,
            email: document.getElementById('email').value,
            direccion: document.getElementById('direccion').value
        };

        const id = document.getElementById('proveedorId').value;
        const url = id ? `/api/proveedores/${id}` : '/api/proveedores';
        const method = id ? 'PUT' : 'POST';

        await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(proveedor)
        });

        form.reset();
        document.getElementById('proveedorId').value = '';
        cargarProveedores();
    }

    window.editarProveedor = async (id) => {
        const response = await fetch(`/api/proveedores/${id}`);
        const proveedor = await response.json();

        document.getElementById('proveedorId').value = proveedor.id;
        document.getElementById('nombre').value = proveedor.nombre;
        document.getElementById('telefono').value = proveedor.telefono;
        document.getElementById('email').value = proveedor.email;
        document.getElementById('direccion').value = proveedor.direccion || '';
    };

    window.eliminarProveedor = async (id) => {
        if (!id) {
            console.error('ID inválido para eliminar el proveedor');
            return;
        }

        if (confirm(`¿Está seguro de que desea eliminar el proveedor con ID ${id}?`)) {
            try {
                const response = await fetch(`/api/proveedores/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    cargarProveedores();
                } else {
                    console.error('Error al eliminar el proveedor:', response.statusText);
                }
            } catch (error) {
                console.error('Error en la solicitud:', error);
            }
        }
    };
});