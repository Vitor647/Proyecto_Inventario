document.addEventListener('DOMContentLoaded', function () {
    // Selectores en PLURAL para coincidir con el HTML
    const elementos = {
        tablaRepuestos: document.querySelector('#tabla-repuestos'),
        btnAgregar: document.querySelector('#btn-agregar'),
        inputNombre: document.querySelector('#repuestos-nombre'),
        inputDescripcion: document.querySelector('#repuestos-descripcion'),
        inputCategoria: document.querySelector('#repuestos-categoria'),
        inputProveedor: document.querySelector('#repuestos-proveedor'),
        inputUbicacion: document.querySelector('#repuestos-ubicacion'),
        inputStock: document.querySelector('#repuestos-stock'),
        inputStockMinimo: document.querySelector('#repuestos-stock-minimo'),
        inputPrecio: document.querySelector('#repuestos-precio'),
        inputId: document.querySelector('#repuestos-id')
    };

    // Función para cargar categorías
    function cargarCategorias() {
        fetch('/api/categorias')
            .then(response => {
                if (!response.ok) throw new Error('Error en la red al cargar categorías');
                return response.json();
            })
            .then(data => {
                const select = elementos.inputCategoria;
                select.innerHTML = '<option value="">Seleccione una categoría</option>';
                data.forEach(categoria => {
                    const option = document.createElement('option');
                    option.value = categoria.id;
                    option.textContent = categoria.nombre;
                    select.appendChild(option);
                });
            })
            .catch(error => console.error('Error en fetch de categorías:', error));
    }

    // Función para cargar proveedores
    function cargarProveedores() {
        fetch('/api/proveedores')
            .then(response => {
                if (!response.ok) throw new Error('Error en la red al cargar proveedores');
                return response.json();
            })
            .then(data => {
                const select = elementos.inputProveedor;
                select.innerHTML = '<option value="">Seleccione un proveedor</option>';
                data.forEach(proveedor => {
                    const option = document.createElement('option');
                    option.value = proveedor.id;
                    option.textContent = proveedor.nombre;
                    select.appendChild(option);
                });
            })
            .catch(error => console.error('Error en fetch de proveedores:', error));
    }

    // Cargar datos iniciales
    cargarCategorias();
    cargarProveedores();
    cargarRepuestos();

    elementos.btnAgregar.addEventListener('click', function () {
        const id = elementos.inputId.value || null;
        const repuesto = {
            nombre: elementos.inputNombre.value,
            descripcion: elementos.inputDescripcion.value,
            categoriaId: parseInt(elementos.inputCategoria.value),
            proveedorId: parseInt(elementos.inputProveedor.value),
            ubicacion: elementos.inputUbicacion.value,
            stock: parseInt(elementos.inputStock.value) || 0,
            stockMinimo: parseInt(elementos.inputStockMinimo.value) || 0,
            precio: parseFloat(elementos.inputPrecio.value) || 0.0
        };

        if (!repuesto.nombre || !repuesto.categoriaId || !repuesto.proveedorId) {
            alert('Nombre, Categoría y Proveedor son campos obligatorios.');
            return;
        }

        id ? editarRepuesto(id, repuesto) : agregarRepuesto(repuesto);
    });

    function cargarRepuestos() {
        fetch('/api/repuestos')
            .then(response => response.ok ? response.json() : Promise.reject('Error al cargar repuestos'))
            .then(data => {
                const tbody = elementos.tablaRepuestos;
                tbody.innerHTML = '';
                data.forEach(repuesto => {
                    const fila = document.createElement('tr');
                    fila.innerHTML = `
                        <td>${repuesto.id}</td>
                        <td>${repuesto.nombre}</td>
                        <td>${repuesto.descripcion}</td>
                        <td>${repuesto.ubicacion}</td>
                        <td>${repuesto.categoriaNombre}</td>
                        <td>${repuesto.proveedorNombre}</td>
                        <td>${repuesto.stock}</td>
                        <td>${repuesto.precio.toFixed(2)} €</td>
                        <td>
                            <div class="actions">
                                <button class="btn-warning editar-repuesto" data-id="${repuesto.id}">Editar</button>
                                <button class="btn-danger eliminar-repuesto" data-id="${repuesto.id}">Eliminar</button>
                            </div>
                        </td>
                    `;
                    tbody.appendChild(fila);
                });
                agregarEventosBotones();
            })
            .catch(error => console.error(error));
    }

    function cargarRepuestoParaEditar(id) {
        fetch(`/api/repuestos/${id}`)
            .then(response => response.ok ? response.json() : Promise.reject('Error al obtener el repuesto'))
            .then(repuesto => {
                elementos.inputId.value = repuesto.id;
                elementos.inputNombre.value = repuesto.nombre;
                elementos.inputDescripcion.value = repuesto.descripcion;
                elementos.inputCategoria.value = repuesto.categoriaId;
                elementos.inputProveedor.value = repuesto.proveedorId;
                elementos.inputUbicacion.value = repuesto.ubicacion;
                elementos.inputStock.value = repuesto.stock;
                elementos.inputStockMinimo.value = repuesto.stockMinimo;
                elementos.inputPrecio.value = repuesto.precio;
                elementos.btnAgregar.textContent = 'Actualizar Repuesto';
                window.scrollTo({ top: 0, behavior: 'smooth' });
            })
            .catch(error => alert(error));
    }

    function agregarRepuesto(repuesto) {
        fetch('/api/repuestos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(repuesto)
        })
            .then(response => {
                if (!response.ok) return response.text().then(text => Promise.reject(text));
                return response.json();
            })
            .then(() => {
                cargarRepuestos();
                resetFormulario();
                alert('Repuesto agregado correctamente');
            })
            .catch(error => {
                alert('Error al agregar el repuesto: ' + error);
                console.error(error);
            });
    }

    function editarRepuesto(id, repuesto) {
        fetch(`/api/repuestos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(repuesto)
        })
            .then(response => {
                if (!response.ok) return response.text().then(text => Promise.reject(text));
                return response.json();
            })
            .then(() => {
                cargarRepuestos();
                resetFormulario();
                alert('Repuesto actualizado correctamente');
            })
            .catch(error => {
                alert('Error al actualizar el repuesto: ' + error);
                console.error(error);
            });
    }

    function eliminarRepuesto(id) {
        if (!confirm('¿Está seguro de eliminar este repuesto?')) return;
        fetch(`/api/repuestos/${id}`, { method: 'DELETE' })
            .then(response => {
                if (!response.ok) return response.text().then(text => Promise.reject(text));
                cargarRepuestos();
                alert('Repuesto eliminado correctamente');
            })
            .catch(error => {
                alert('Error al eliminar el repuesto: ' + error);
                console.error(error);
            });
    }

    function resetFormulario() {
        elementos.inputId.value = '';
        elementos.inputNombre.value = '';
        elementos.inputDescripcion.value = '';
        elementos.inputCategoria.value = '';
        elementos.inputProveedor.value = '';
        elementos.inputUbicacion.value = '';
        elementos.inputStock.value = '';
        elementos.inputStockMinimo.value = '';
        elementos.inputPrecio.value = '';
        elementos.btnAgregar.textContent = 'Guardar Repuesto';
    }

    function agregarEventosBotones() {
        document.querySelectorAll('.editar-repuesto').forEach(btn => {
            btn.addEventListener('click', function () {
                cargarRepuestoParaEditar(this.getAttribute('data-id'));
            });
        });
        document.querySelectorAll('.eliminar-repuesto').forEach(btn => {
            btn.addEventListener('click', function () {
                eliminarRepuesto(this.getAttribute('data-id'));
            });
        });
    }
});