document.addEventListener('DOMContentLoaded', () => {
    // --- Selectores ---
    const selectCliente = document.getElementById('selectCliente');
    const selectVehiculo = document.getElementById('selectVehiculo');
    const descripcionProblema = document.getElementById('descripcionProblema');
    const selectRepuesto = document.getElementById('selectRepuesto');
    const inputCantidad = document.getElementById('inputCantidad');
    const btnAnadirRepuesto = document.getElementById('btnAnadirRepuesto');
    const tablaRepuestosBody = document.getElementById('tablaRepuestosAnadidos')?.querySelector('tbody');
    const btnCrearOrden = document.getElementById('btnCrearOrden');

    let repuestosParaOrden = []; // Array para guardar los repuestos añadidos

    // --- Carga de Datos Inicial ---
    fetch('/api/clientes')
        .then(response => response.json())
        .then(clientes => {
            selectCliente.innerHTML = '<option value="">-- Seleccione un cliente --</option>';
            clientes.forEach(cliente => {
                selectCliente.innerHTML += `<option value="${cliente.id}">${cliente.nombre}</option>`;
            });
        });

    fetch('/api/repuestos')
        .then(response => response.json())
        .then(repuestos => {
            selectRepuesto.innerHTML = '<option value="">-- Seleccione un repuesto --</option>';
            repuestos.forEach(repuesto => {
                selectRepuesto.innerHTML += `<option value="${repuesto.id}">${repuesto.nombre} (Stock: ${repuesto.stock})</option>`;
            });
        });

    // --- Lógica de Eventos ---
    selectCliente.addEventListener('change', () => {
        const clienteId = selectCliente.value;
        selectVehiculo.innerHTML = '<option value="">Cargando vehículos...</option>';
        selectVehiculo.disabled = true;

        if (clienteId) {
            fetch(`/api/vehiculos/cliente/${clienteId}`)
                .then(response => response.json())
                .then(vehiculos => {
                    selectVehiculo.innerHTML = '<option value="">-- Seleccione un vehículo --</option>';
                    if (vehiculos.length > 0) {
                        vehiculos.forEach(vehiculo => {
                            selectVehiculo.innerHTML += `<option value="${vehiculo.id}">${vehiculo.marca} ${vehiculo.modelo} (${vehiculo.matricula})</option>`;
                        });
                    } else {
                        selectVehiculo.innerHTML = '<option value="">Este cliente no tiene vehículos</option>';
                    }
                    selectVehiculo.disabled = false;
                });
        } else {
            selectVehiculo.innerHTML = '<option value="">Primero seleccione un cliente</option>';
        }
    });

    btnAnadirRepuesto.addEventListener('click', () => {
        const repuestoId = selectRepuesto.value;
        const cantidad = parseInt(inputCantidad.value);

        if (!repuestoId || !cantidad || cantidad < 1) {
            alert('Por favor, seleccione un repuesto y una cantidad válida.');
            return;
        }
        const repuestoSeleccionado = {
            repuestoId: parseInt(repuestoId),
            cantidad: cantidad,
            nombre: selectRepuesto.options[selectRepuesto.selectedIndex].text
        };
        if (repuestosParaOrden.some(r => r.repuestoId === repuestoSeleccionado.repuestoId)) {
            alert('Este repuesto ya ha sido añadido a la orden.');
            return;
        }
        repuestosParaOrden.push(repuestoSeleccionado);
        renderizarTablaRepuestos();
    });

    btnCrearOrden.addEventListener('click', () => {
        console.log('Valor del desplegable de Cliente:', document.getElementById('selectCliente').value);
        console.log('Valor del desplegable de Vehículo:', document.getElementById('selectVehiculo').value);

        const clienteId = parseInt(selectCliente.value);
        const vehiculoId = parseInt(selectVehiculo.value);
        const desc = descripcionProblema.value.trim();

        // Esta nueva validación es más estricta. isNaN (is Not-a-Number) comprueba que se haya seleccionado
        // una opción válida con un número de ID, en lugar de la opción "-- Seleccione --" que tiene value="".
        if (isNaN(clienteId) || isNaN(vehiculoId) || !desc) {
            alert('Cliente, Vehículo y Descripción son campos obligatorios. Por favor, asegúrese de seleccionar un vehículo.');
            return;
        }

        const ordenData = {
            clienteId: clienteId,
            vehiculoId: vehiculoId,
            descripcionProblema: desc,
            repuestos: repuestosParaOrden.map(r => ({ repuestoId: r.repuestoId, cantidad: r.cantidad }))
        };

        fetch('/api/ordenes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ordenData)
        })
            .then(response => {
                if (!response.ok) {

                    return response.json().then(err => Promise.reject(err));
                }
                return response.json();
            })
            .then(data => {
                alert(`¡Orden de Trabajo #${data.id} creada con éxito!`);
                window.location.href = '/OrdenesdeTrabajo.html';
            })
            .catch(error => {
                console.error('Error al crear la orden:', error);
                // Mostramos el mensaje de error que viene del servidor, si existe
                alert(`Error al crear la orden: ${error.message || 'Error desconocido. Revise la consola del servidor.'}`);
            });
    });

    // --- Funciones de Ayuda ---
    function renderizarTablaRepuestos() {
        tablaRepuestosBody.innerHTML = '';
        repuestosParaOrden.forEach((repuesto, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${repuesto.nombre}</td>
                <td>${repuesto.cantidad}</td>
                <td><button class="btn btn-danger btn-eliminar-repuesto" data-index="${index}"><i class="fas fa-trash"></i></button></td>
            `;
            tablaRepuestosBody.appendChild(row);
        });

        document.querySelectorAll('.btn-eliminar-repuesto').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const indexToRemove = parseInt(e.currentTarget.dataset.index);
                repuestosParaOrden.splice(indexToRemove, 1);
                renderizarTablaRepuestos();
            });
        });
    }
});