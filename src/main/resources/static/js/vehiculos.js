document.addEventListener('DOMContentLoaded', () => {
    const tablaVehiculosBody = document.getElementById('tabla-vehiculos');
    const inputBusqueda = document.getElementById('inputBusqueda');
    let todosLosVehiculos = []; // Guardamos una copia local para filtrar rápidamente

    // Cargar todos los vehículos al iniciar
    async function cargarVehiculos() {
        try {
            const response = await fetch('/api/vehiculos');
            if (!response.ok) throw new Error('Error al cargar los vehículos');

            todosLosVehiculos = await response.json();
            renderizarTabla(todosLosVehiculos);
        } catch (error) {
            console.error(error);
            tablaVehiculosBody.innerHTML = `<tr><td colspan="6">Error al cargar los datos.</td></tr>`;
        }
    }

    // Dibujar la tabla con los datos de los vehículos
    function renderizarTabla(vehiculos) {
        tablaVehiculosBody.innerHTML = '';
        if (vehiculos.length === 0) {
            tablaVehiculosBody.innerHTML = `<tr><td colspan="6">No se encontraron vehículos.</td></tr>`;
            return;
        }

        vehiculos.forEach(vehiculo => {
            const row = document.createElement('tr');
            // La acción ahora es ir a la ficha del cliente propietario
            row.innerHTML = `
                <td>${vehiculo.marca}</td>
                <td>${vehiculo.modelo}</td>
                <td>${vehiculo.anio}</td>
                <td>${vehiculo.matricula}</td>
                <td>${vehiculo.clienteNombre}</td>
                <td>
                    <a href="/cliente-detalle.html?id=${vehiculo.id}" class="btn btn-primary" title="Ver Ficha del Cliente">
                        <i class="fas fa-user"></i> Ver Propietario
                    </a>
                </td>
            `;
            tablaVehiculosBody.appendChild(row);
        });
    }

    // Evento para filtrar la tabla mientras el usuario escribe
    inputBusqueda.addEventListener('keyup', () => {
        const textoBusqueda = inputBusqueda.value.toLowerCase();

        const vehiculosFiltrados = todosLosVehiculos.filter(vehiculo => {
            return vehiculo.marca.toLowerCase().includes(textoBusqueda) ||
                vehiculo.modelo.toLowerCase().includes(textoBusqueda) ||
                vehiculo.matricula.toLowerCase().includes(textoBusqueda);
        });

        renderizarTabla(vehiculosFiltrados);
    });

    // Carga inicial
    cargarVehiculos();
});