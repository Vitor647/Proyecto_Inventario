document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('categoriaForm');
    const tablaCategorias = document.getElementById('tablaCategorias').querySelector('tbody');
    const btnCancelar = document.getElementById('btnCancelar');
    const messageEl = document.getElementById('message');
    
    // Cargar categorías al iniciar
    cargarCategorias();
    
    // Manejar envío de formulario
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        guardarCategoria();
    });
    
    // Manejar cancelar edición
    btnCancelar.addEventListener('click', () => {
        form.reset();
        document.getElementById('categoriaId').value = '';
        btnCancelar.style.display = 'none';
        showMessage('Edición cancelada', 'success');
    });
    
    // Función para cargar todas las categorías
    async function cargarCategorias() {
        try {
            const response = await fetch('/api/categorias');
            if (!response.ok) throw new Error('Error al cargar categorías');
            
            const categorias = await response.json();
            renderizarTabla(categorias);
        } catch (error) {
            showMessage(`Error: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }
    
    // Función para renderizar la tabla de categorías
    function renderizarTabla(categorias) {
        tablaCategorias.innerHTML = '';
        
        categorias.forEach(categoria => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${categoria.id}</td>
                <td>${escapeHTML(categoria.nombre)}</td>
                <td>${escapeHTML(categoria.descripcion || '')}</td>
                <td class="actions">
                    <button class="btn-edit" data-id="${categoria.id}">Editar</button>
                    <button class="btn-delete" data-id="${categoria.id}">Eliminar</button>
                </td>
            `;
            tablaCategorias.appendChild(row);
        });
        
        // Agregar event listeners a los botones
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', () => editarCategoria(btn.dataset.id));
        });
        
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', () => eliminarCategoria(btn.dataset.id));
        });
    }
    
    // Función para guardar/actualizar una categoría
    async function guardarCategoria() {
        const categoria = {
            id: document.getElementById('categoriaId').value || null,
            nombre: document.getElementById('nombre').value,
            descripcion: document.getElementById('descripcion').value
        };
        
        // Validación básica
        if (!categoria.nombre.trim()) {
            showMessage('El nombre de la categoría es obligatorio', 'error');
            return;
        }
        
        const url = categoria.id ? `/api/categorias/${categoria.id}` : '/api/categorias';
        const method = categoria.id ? 'PUT' : 'POST';
        
        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoria)
            });
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al guardar la categoría');
            }
            
            const data = await response.json();
            showMessage(
                categoria.id 
                    ? `Categoría "${data.nombre}" actualizada correctamente` 
                    : `Categoría "${data.nombre}" creada correctamente`,
                'success'
            );
            
            // Limpiar formulario y recargar
            form.reset();
            document.getElementById('categoriaId').value = '';
            btnCancelar.style.display = 'none';
            cargarCategorias();
        } catch (error) {
            showMessage(`Error: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }
    
    // Función para editar una categoría
    async function editarCategoria(id) {
        try {
            const response = await fetch(`/api/categorias/${id}`);
            if (!response.ok) throw new Error('Error al cargar la categoría');
            
            const categoria = await response.json();
            
            // Llenar el formulario
            document.getElementById('categoriaId').value = categoria.id;
            document.getElementById('nombre').value = categoria.nombre;
            document.getElementById('descripcion').value = categoria.descripcion || '';
            
            // Mostrar botón de cancelar
            btnCancelar.style.display = 'inline-block';
            
            // Scroll al formulario
            form.scrollIntoView({ behavior: 'smooth' });
            showMessage('Editando categoría. Complete los cambios y haga clic en Guardar', 'success');
        } catch (error) {
            showMessage(`Error: ${error.message}`, 'error');
            console.error('Error:', error);
        }
    }
    
    // Función para eliminar una categoría
    async function eliminarCategoria(id) {
        if (!id) {
            console.error('ID inválido para eliminar la categoría');
            return;
        }

        if (confirm(`¿Está seguro de que desea eliminar la categoría con ID ${id}?`)) {
            try {
                const response = await fetch(`/api/categorias/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    cargarCategorias();
                } else {
                    console.error('Error al eliminar la categoría:', response.statusText);
                }
            } catch (error) {
                console.error('Error en la solicitud:', error);
            }
        }
    }
    
    // Función para mostrar mensajes
    function showMessage(message, type) {
        messageEl.textContent = message;
        messageEl.className = 'message';
        messageEl.classList.add(`message-${type}`);
        messageEl.style.display = 'block';
        
        // Ocultar mensaje después de 5 segundos
        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 5000);
    }
    
    // Función para prevenir ataques XSS básicos
    function escapeHTML(str) {
        if (!str) return '';
        return str.toString()
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }
});