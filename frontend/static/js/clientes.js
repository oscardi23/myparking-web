/**
 * [INTEGRACION] Busca elementos con clase 'js-cliente-inicial' y genera 
 * iniciales usando el atributo 'data-nombre'. El backend debe inyectar
 * el nombre real en este atributo para que el buscador y las iniciales funcionen.
 */
function getInitials(name) {
    if (!name) return "--";
    const names = name.trim().split(/\s+/);
    let initials = "";
    if (names.length > 0) initials += names[0].charAt(0).toUpperCase();
    if (names.length > 1) initials += names[names.length - 1].charAt(0).toUpperCase();
    return initials;
}

/**
 * Procesa todos los elementos con la clase 'js-cliente-inicial'
 */
function processAllInitials() {
    const elements = document.querySelectorAll(".js-cliente-inicial");
    elements.forEach(el => {
        const nombre = el.getAttribute("data-nombre");
        if (nombre) {
            el.textContent = getInitials(nombre);
        }
    });
}

/**
 * Configura el buscador en tiempo real con estado de 'sin resultados'
 */
function setupBuscador() {
    const inputBusqueda = document.getElementById("input_busqueda");
    const items = document.querySelectorAll(".js-cliente-item");
    const containerSinResultados = document.getElementById("sin_resultados");
    const tableContainer = document.querySelector(".hidden.md\\:block"); // El div que envuelve la tabla

    if (!inputBusqueda) return;

    inputBusqueda.addEventListener("input", (e) => {
        const query = e.target.value.toLowerCase().trim();
        let coincidencias = 0;

        items.forEach(item => {
            const dataEl = item.querySelector(".js-cliente-inicial");
            const nombre = dataEl ? dataEl.getAttribute("data-nombre").toLowerCase() : "";
            
            // Búsqueda simple por nombre
            const coincide = nombre.includes(query);

            if (coincide) {
                item.style.display = ""; 
                coincidencias++;
            } else {
                item.style.display = "none";
            }
        });

        // Toggle del estado 'sin resultados'
        if (coincidencias === 0) {
            containerSinResultados?.classList.remove("hidden");
            if (tableContainer) tableContainer.style.display = "none";
        } else {
            containerSinResultados?.classList.add("hidden");
            if (tableContainer) tableContainer.style.display = "";
        }
    });
}

// Ejecutar cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", () => {
    processAllInitials();
    setupBuscador();
});