document.addEventListener('DOMContentLoaded', function () {
    // Estado global de la aplicación
    const appState = {
        movimientos: [],
        repuestos: [],
        clientes: []
    };


    const elementos = {
        tablaMovimientos: document.getElementById('tabla-movimientos'),
        filtroRepuesto: document.querySelector('#filtro-repuestos'),
        btnNuevoMovimiento: document.querySelector('#btn-nuevo-movimiento'),
        modal: document.querySelector('#modal-movimiento'),
        modalForm: document.querySelector('#form-movimiento'),
        modalTitulo: document.querySelector('#modal-titulo'),
        btnGuardar: document.querySelector('#btn-guardar'),
        btnCancelar: document.querySelector('#btn-cancelar'),
        modalCerrar: document.querySelectorAll('[data-modal-cerrar]')
    };

    // Inicializar la aplicación
    async function init() {
        await cargarDatosIniciales();
        inicializarEventos();
    }

    // Cargar datos iniciales desde la API
    async function cargarDatosIniciales() {

        try {
            const [movimientosRes, repuestosRes, clientesRes] = await Promise.all([
                fetch('/api/movimientos-inventario'),
                fetch('/api/repuestos'),
                fetch('/api/clientes')
            ]);

            if (!movimientosRes.ok || !repuestosRes.ok || !clientesRes.ok) {
                throw new Error('Error al cargar datos iniciales del servidor.');
            }

            appState.movimientos = await movimientosRes.json();
            appState.repuestos = await repuestosRes.json();
            appState.clientes = await clientesRes.json();

            popularFiltroRepuestos();
            mostrarMovimientos(appState.movimientos);

        } catch (error) {
            console.error("Error fatal al cargar datos:", error);
            elementos.tablaMovimientos.innerHTML = `<tr><td colspan="9">${error.message}</td></tr>`;
        }
    }

    function popularFiltroRepuestos() {
        const select = elementos.filtroRepuesto;
        select.innerHTML = '<option value="">Todos los repuestos</option>';
        appState.repuestos.forEach(repuesto => {
            select.innerHTML += `<option value="${repuesto.id}">${repuesto.nombre}</option>`;
        });
    }

    // Mostrar movimientos en la tabla
    function mostrarMovimientos(movimientos) {
        elementos.tablaMovimientos.innerHTML = '';
        if (movimientos.length === 0) {
            elementos.tablaMovimientos.innerHTML = `<tr><td colspan="9" class="text-center py-4">No hay movimientos registrados.</td></tr>`;
            return;
        }

        movimientos.forEach(mov => {
            const fila = document.createElement('tr');
            const fecha = new Date(mov.fecha).toLocaleString('es-ES');
            const tipoClase = mov.tipo === 'ENTRADA' ? 'text-green-600' : 'text-red-600';

            // Usamos los campos del DTO
            fila.innerHTML = `
                <td>${mov.id}</td>
                <td>${mov.repuestoNombre}</td>
                <td><span class="font-bold ${tipoClase}">${mov.tipo}</span></td>
                <td class="font-bold ${tipoClase}">${mov.tipo === 'ENTRADA' ? '+' : '-'}${mov.cantidad}</td>
                <td>${mov.clienteNombre}</td>
                <td>${fecha}</td>
                <td>N/A</td> <td>${mov.usuarioNombre}</td>
                <td><button class="btn-sm btn-danger">Eliminar</button></td>
            `;
            elementos.tablaMovimientos.appendChild(fila);
        });
    }

    // --- Lógica del Modal (simplificada) ---

    function abrirModalNuevoMovimiento() {
        elementos.modalTitulo.textContent = 'Registrar Nuevo Movimiento';
        elementos.modalForm.innerHTML = `
            <div class="form-group">
                <label for="modal-tipo">Tipo de Movimiento</label>
                <select id="modal-tipo" class="form-control" required>
                    <option value="ENTRADA">Entrada de Stock (Compra)</option>
                    <option value="SALIDA">Salida de Stock (Venta/Uso)</option>
                </select>
            </div>
            <div class="form-group">
                <label for="modal-repuesto">Repuesto</label>
                <select id="modal-repuesto" class="form-control" required>
                    ${appState.repuestos.map(r => `<option value="${r.id}">${r.nombre}</option>`).join('')}
                </select>
            </div>
             <div class="form-group">
                <label for="modal-cantidad">Cantidad</label>
                <input type="number" id="modal-cantidad" class="form-control" min="1" required>
            </div>
            <div class="form-group">
                <label for="modal-cliente">Cliente (solo para SALIDAS)</label>
                <select id="modal-cliente" class="form-control">
                     <option value="">Ninguno</option>
                    ${appState.clientes.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('')}
                </select>
            </div>
        `;
        elementos.modal.classList.remove('hidden');
    }

    function cerrarModal() {
        elementos.modal.classList.add('hidden');
    }

    async function guardarNuevoMovimiento(event) {
        event.preventDefault();
        const movimientoData = {
            tipo: document.getElementById('modal-tipo').value,
            repuestoId: parseInt(document.getElementById('modal-repuesto').value),
            cantidad: parseInt(document.getElementById('modal-cantidad').value),
            clienteId: document.getElementById('modal-cliente').value ? parseInt(document.getElementById('modal-cliente').value) : null,
            usuarioId: 1
        };

        if (!movimientoData.tipo || !movimientoData.repuestoId || !movimientoData.cantidad) {
            alert('Tipo, Repuesto y Cantidad son obligatorios.');
            return;
        }

        try {
            const response = await fetch('/api/movimientos-inventario', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(movimientoData)
            });


            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Error al guardar el movimiento.');
            }

            const nuevoMovimiento = await response.json();
            alert(`Movimiento #${nuevoMovimiento.id} guardado con éxito.`);
            cerrarModal();
            init();
        } catch (error) {
            console.error('Error al guardar:', error);

            alert(`Error: ${error.message}`);
        }
    }

    // --- Inicializar Eventos ---
    function inicializarEventos() {
        elementos.btnNuevoMovimiento?.addEventListener('click', abrirModalNuevoMovimiento);
        elementos.btnGuardar?.addEventListener('click', guardarNuevoMovimiento);
        elementos.btnCancelar?.addEventListener('click', cerrarModal);
        elementos.modalCerrar?.forEach(btn => btn.addEventListener('click', cerrarModal));
    }

    init(); // Iniciar la aplicación
});