document.addEventListener('DOMContentLoaded', () => {
    // --- Selectores de Elementos ---
    const tablaOrdenesBody = document.getElementById('tablaOrdenes')?.querySelector('tbody');
    const detalleCard = document.getElementById('detalleCard');
    const sinSeleccion = document.getElementById('sinSeleccion');
    const btnAplicarFiltros = document.getElementById('btnAplicarFiltros');
    const btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
    const btnGuardarEstado = document.getElementById('btnGuardarEstado');
    const btnCerrarDetalle = document.getElementById('btnCerrarDetalle');
    const messageEl = document.getElementById('message');

    let ordenSeleccionadaId = null;

    // --- Asignación de Eventos ---
    btnAplicarFiltros?.addEventListener('click', aplicarFiltros);
    btnLimpiarFiltros?.addEventListener('click', limpiarFiltros);
    btnGuardarEstado?.addEventListener('click', guardarEstadoOrden);
    btnCerrarDetalle?.addEventListener('click', () => {
        detalleCard.style.display = 'none';
        sinSeleccion.style.display = 'block';
        ordenSeleccionadaId = null;
    });

    // --- Carga Inicial ---
    cargarOrdenes();

    // --- Funciones Principales ---

    async function cargarOrdenes(filtroEstado = '', filtroCliente = '') {
        try {
            const params = new URLSearchParams();
            if (filtroEstado) params.append('estado', filtroEstado);
            if (filtroCliente) params.append('cliente', filtroCliente);

            const response = await fetch(`/api/ordenes?${params.toString()}`);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const ordenes = await response.json();
            renderizarTabla(ordenes);
        } catch (error) {
            showMessage(`Error al cargar órdenes: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }

    function aplicarFiltros() {
        const filtroEstado = document.getElementById('filtroEstado').value;
        const filtroCliente = document.getElementById('filtroCliente').value;
        cargarOrdenes(filtroEstado, filtroCliente);
    }

    function limpiarFiltros() {
        document.getElementById('filtroEstado').value = '';
        document.getElementById('filtroCliente').value = '';
        cargarOrdenes();
    }

    function renderizarTabla(ordenes) {
        if (!tablaOrdenesBody) return;
        tablaOrdenesBody.innerHTML = '';
        if (ordenes.length === 0) {
            tablaOrdenesBody.innerHTML = `<tr><td colspan="7" style="text-align: center;">No se encontraron órdenes</td></tr>`;
            return;
        }

        ordenes.forEach(orden => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${orden.id}</td>
                <td>${orden.clienteNombre || 'N/A'}</td>
                <td>${orden.vehiculoInfo || 'N/A'}</td>
                <td>${(orden.descripcionProblema || '').substring(0, 40)}...</td>
                <td><span class="estado-badge">${orden.estado.replace('_', ' ')}</span></td>
                <td>${new Date(orden.fechaCreacion).toLocaleDateString()}</td>
                <td class="actions">
                    <button class="btn btn-primary btn-ver-detalle" data-id="${orden.id}" title="Ver Detalle"><i class="fas fa-eye"></i></button>
                </td>
            `;
            tablaOrdenesBody.appendChild(row);
        });

        document.querySelectorAll('.btn-ver-detalle').forEach(btn => {
            btn.addEventListener('click', () => verDetalleOrden(btn.dataset.id));
        });
    }

    async function verDetalleOrden(id) {
        try {
            const response = await fetch(`/api/ordenes/${id}`);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const orden = await response.json();
            ordenSeleccionadaId = orden.id;
            mostrarDetalleOrden(orden);
        } catch (error) {
            showMessage(`Error al cargar la orden: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }

    function mostrarDetalleOrden(orden) {
        detalleCard.style.display = 'block';
        sinSeleccion.style.display = 'none';
        document.getElementById('clienteOrden').value = orden.cliente.nombre || 'N/A';
        document.getElementById('vehiculoOrden').value = `${orden.vehiculo.marca} ${orden.vehiculo.modelo} (${orden.vehiculo.matricula})`;
        document.getElementById('descripcionOrden').value = orden.descripcionProblema;
        document.getElementById('estadoOrden').value = orden.estado;
        document.getElementById('fechaCreacion').value = new Date(orden.fechaCreacion).toLocaleString();
        document.getElementById('fechaCompletado').value = orden.fechaCompletado ? new Date(orden.fechaCompletado).toLocaleString() : 'Pendiente';

        const repuestosBody = document.getElementById('repuestosBody');
        repuestosBody.innerHTML = '';
        let total = 0;

        if (orden.repuestos && orden.repuestos.length > 0) {
            orden.repuestos.forEach(rep => {
                // CORRECCIÓN 1: Se usa 'precioUnitario' para el cálculo
                const subtotal = rep.cantidad * rep.precioUnitario;
                total += subtotal;
                const row = document.createElement('tr');

                // CORRECCIÓN 1: Se usa 'nombreRepuesto' y 'precioUnitario' para mostrar los datos
                row.innerHTML = `
                    <td>${rep.nombreRepuesto}</td>
                    <td>${rep.cantidad}</td>
                    <td>${rep.precioUnitario.toFixed(2)} €</td>
                    <td>${subtotal.toFixed(2)} €</td>
                `;
                repuestosBody.appendChild(row);
            });
        } else {
            repuestosBody.innerHTML = `<tr><td colspan="4" style="text-align: center;">No se utilizaron repuestos</td></tr>`;
        }
        document.getElementById('totalRepuestos').textContent = `${total.toFixed(2)} €`;
    }

    async function guardarEstadoOrden() {
        if (!ordenSeleccionadaId) return;
        const nuevoEstado = document.getElementById('estadoOrden').value;

        try {
            // CORRECCIÓN 2: Se envía un objeto JSON en lugar de texto plano
            const response = await fetch(`/api/ordenes/${ordenSeleccionadaId}/estado`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' }, // Se especifica el tipo de contenido
                body: JSON.stringify({ estado: nuevoEstado })    // Se envía el objeto JSON
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Error al actualizar estado');
            }

            const ordenActualizada = await response.json();
            showMessage(`Estado de la orden #${ordenSeleccionadaId} actualizado`, 'success');
            mostrarDetalleOrden(ordenActualizada);
            cargarOrdenes(document.getElementById('filtroEstado').value, document.getElementById('filtroCliente').value);
        } catch (error) {
            showMessage(`Error: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }

    function showMessage(message, type) {
        if (!messageEl) return;
        messageEl.textContent = message;
        messageEl.className = `message message-${type}`; // Se añade un espacio para aplicar ambas clases
        messageEl.style.display = 'block';
        setTimeout(() => { messageEl.style.display = 'none'; }, 4000);
    }
});