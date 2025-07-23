// Función para validar formularios
function validarFormulario() {
    let valido = true;

    // Obtener todos los campos requeridos
    const camposRequeridos = document.querySelectorAll('[required]');

    // Verificar cada campo
    camposRequeridos.forEach(campo => {
        if (!campo.value) {
            campo.classList.add('is-invalid');
            valido = false;
        } else {
            campo.classList.remove('is-invalid');
        }
    });

    return valido;
}

// Inicializar cuando el DOM esté cargado
document.addEventListener('DOMContentLoaded', function () {
    // Agregar validación a todos los formularios
    const formularios = document.querySelectorAll('form');
    formularios.forEach(form => {
        form.addEventListener('submit', function (event) {
            if (!validarFormulario()) {
                event.preventDefault();
                alert('Por favor, complete todos los campos requeridos.');
            }
        });
    });

    // Confirmar eliminación
    const botonesEliminar = document.querySelectorAll('.btn-danger');
    botonesEliminar.forEach(boton => {
        boton.addEventListener('click', function (event) {
            if (!confirm('¿Está seguro de que desea eliminar este elemento?')) {
                event.preventDefault();
            }
        });
    });
});
