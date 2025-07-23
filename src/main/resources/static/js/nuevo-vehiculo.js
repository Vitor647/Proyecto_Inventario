document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const clienteId = params.get('clienteId');
    const form = document.getElementById('formNuevoVehiculo');

    if (!clienteId) {
        document.body.innerHTML = '<h1>Error: Se necesita un cliente para añadir un vehículo.</h1>';
        return;
    }

    // Guardamos el ID del cliente en el campo oculto y en el botón de cancelar
    document.getElementById('clienteId').value = clienteId;
    document.getElementById('btnCancelar').href = `/cliente-detalle.html?id=${clienteId}`;

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const vehiculoData = {
            marca: document.getElementById('marca').value,
            modelo: document.getElementById('modelo').value,
            anio: document.getElementById('anio').value,
            matricula: document.getElementById('matricula').value,
            clienteId: parseInt(clienteId)
        };

        try {
            const response = await fetch('/api/vehiculos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(vehiculoData)
            });

            if (!response.ok) throw new Error('Error al guardar el vehículo.');

            alert('Vehículo añadido con éxito.');

            window.location.href = `/cliente-detalle.html?id=${clienteId}`;
        } catch (error) {
            alert(error.message);
            console.error(error);
        }
    });
});