document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const clienteId = params.get('id');

    if (!clienteId) {
        document.body.innerHTML = '<h1>Error: No se ha especificado un ID de cliente.</h1>';
        return;
    }

    // Poner el enlace correcto en el botón "Añadir Vehículo"
    document.getElementById('btnNuevoVehiculo').href = `/nuevo-vehiculo.html?clienteId=${clienteId}`;

    // Cargar los datos del cliente y sus vehículos
    fetch(`/api/clientes/${clienteId}`)
        .then(response => response.ok ? response.json() : Promise.reject('Cliente no encontrado'))
        .then(clienteDetalle => {
            document.getElementById('nombreCliente').textContent = `Ficha de: ${clienteDetalle.nombre}`;
            document.getElementById('telefonoCliente').textContent = clienteDetalle.telefono;
            document.getElementById('emailCliente').textContent = clienteDetalle.email;
            document.getElementById('direccionCliente').textContent = clienteDetalle.direccion;

            const tablaBody = document.getElementById('tablaVehiculosCliente');
            tablaBody.innerHTML = '';
            if (clienteDetalle.vehiculos.length > 0) {
                clienteDetalle.vehiculos.forEach(v => {
                    tablaBody.innerHTML += `<tr><td>${v.marca}</td><td>${v.modelo}</td><td>${v.matricula}</td></tr>`;
                });
            } else {
                tablaBody.innerHTML = `<tr><td colspan="3">Este cliente no tiene vehículos registrados.</td></tr>`;
            }
        })
        .catch(error => {
            document.getElementById('nombreCliente').textContent = 'Error al cargar cliente';
            console.error(error);
        });
});