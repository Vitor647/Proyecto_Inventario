document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('clienteForm');
    const tablaClientesBody = document.getElementById('tablaClientes');
    const clienteIdField = document.getElementById('clienteId');
    const btnLimpiar = document.getElementById('btnLimpiar');

    // Cargar clientes al iniciar
    loadClients();

    // Evento para el formulario (crear y actualizar)
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const clienteData = {
            nombre: document.getElementById('nombre').value.trim(),
            telefono: document.getElementById('telefono').value.trim(),
            email: document.getElementById('email').value.trim(),
            direccion: document.getElementById('direccion').value.trim()
        };

        if (!clienteData.nombre || !clienteData.telefono || !clienteData.email) {
            alert('Nombre, Teléfono y Email son obligatorios.');
            return;
        }

        const clienteId = clienteIdField.value;
        const url = clienteId ? `/api/clientes/${clienteId}` : '/api/clientes';
        const method = clienteId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(clienteData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Error en la petición');
            }

            alert(`Cliente ${clienteId ? 'actualizado' : 'creado'} con éxito.`);
            form.reset();
            clienteIdField.value = '';
            loadClients();
        } catch (error) {
            console.error('Error al guardar cliente:', error);
            alert(`Error al guardar: ${error.message}`);
        }
    });

    // Evento para el botón de limpiar
    btnLimpiar.addEventListener('click', () => {
        form.reset();
        clienteIdField.value = '';
    });

    // Cargar la lista de clientes desde el API
    async function loadClients() {
        try {
            const response = await fetch('/api/clientes');
            if (!response.ok) throw new Error('No se pudieron cargar los clientes.');
            const clientes = await response.json();
            renderClientTable(clientes);
        } catch (error) {
            console.error('Error:', error);
            tablaClientesBody.innerHTML = `<tr><td colspan="4">Error al cargar datos.</td></tr>`;
        }
    }


    // Dibujar la tabla de clientes
    function renderClientTable(clientes) {
        tablaClientesBody.innerHTML = '';
        if (clientes.length === 0) {
            tablaClientesBody.innerHTML = `<tr><td colspan="4">No hay clientes registrados.</td></tr>`;
            return;
        }

        clientes.forEach(cliente => {
            const row = document.createElement('tr');
            // El botón de editar ahora es "Ver Ficha" y nos llevará a la página de detalle
            row.innerHTML = `
                <td>${escapeHTML(cliente.nombre)}</td>
                <td>${escapeHTML(cliente.telefono)}</td>
                <td>${escapeHTML(cliente.email)}</td>
                <td>
                    <a href="/cliente-detalle.html?id=${cliente.id}" class="btn btn-primary" title="Ver Ficha del Cliente">
                        <i class="fas fa-eye"></i> Ver Ficha
                    </a>
                    <button class="btn-delete btn-danger" data-id="${cliente.id}" title="Eliminar Cliente">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            `;
            tablaClientesBody.appendChild(row);
        });
    }

    // Delegación de eventos para la tabla
    tablaClientesBody.addEventListener('click', (event) => {

        const target = event.target.closest('button');
        if (!target) return;

        if (target.classList.contains('btn-delete')) {
            const id = target.dataset.id;
            deleteClient(id);
        }
    });

    // Eliminar un cliente
    async function deleteClient(id) {
        if (!confirm(`¿Está seguro de que desea eliminar el cliente con ID ${id}?`)) return;

        try {
            const response = await fetch(`/api/clientes/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                throw new Error('El cliente no se pudo eliminar (puede tener vehículos u órdenes asociadas).');
            }
            alert('Cliente eliminado con éxito.');
            loadClients();
        } catch (error) {
            console.error('Error al eliminar:', error);
            alert(`Error: ${error.message}`);
        }
    }

    function escapeHTML(str) {
        if (str === null || str === undefined) return '';
        const text = String(str);
        return text.replace(/[&<>"']/g, tag => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[tag] || tag));
    }
});