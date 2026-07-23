/* =====================================================
   ADMIN VEHICLES
===================================================== */

let isEditing = false;

/* ── MODAL ────────────────────────────────────────── */

function openCreateModal() {
    isEditing = false;
    clearForm();
    document.getElementById('modal-title').textContent = 'Nuevo vehículo';
    document.getElementById('vehicle-modal').classList.remove('hidden');
}

async function openEditModal(vehicleId) {
    isEditing = true;
    document.getElementById('modal-title').textContent = 'Editar vehículo';

    try {
        const res = await fetch(`/admin/vehicles/${vehicleId}`);
        const result = await res.json();

        if (!result.ok) {
            showToast('Error cargando el vehículo', 'error');
            return;
        }

        const v = result.data;
        document.getElementById('vehicle-id').value         = v.id;
        document.getElementById('vehicle-plate').value      = v.plate;
        document.getElementById('vehicle-type').value       = v.type;
        document.getElementById('vehicle-brand').value      = v.brand ?? '';
        document.getElementById('vehicle-color').value      = v.color ?? '';
        document.getElementById('vehicle-client-id').value  = v.clientId;

        // mostrar cliente encontrado
        if (v.clientName) {
            showClientFound(v.clientName + ' ' + v.clientLastName, v.clientPhone);
        }

        document.getElementById('vehicle-modal').classList.remove('hidden');

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}

function closeModal() {
    document.getElementById('vehicle-modal').classList.add('hidden');
    clearForm();
}

function clearForm() {
    document.getElementById('vehicle-id').value        = '';
    document.getElementById('search-nationalid').value = '';
    document.getElementById('vehicle-plate').value     = '';
    document.getElementById('vehicle-type').value      = '';
    document.getElementById('vehicle-brand').value     = '';
    document.getElementById('vehicle-color').value     = '';
    document.getElementById('vehicle-client-id').value = '';
    document.getElementById('client-found').classList.add('hidden');
}

/* ── BUSCAR CLIENTE POR CÉDULA ────────────────────── */

async function searchClient() {

    const nationalId = document.getElementById('search-nationalid').value.trim();

    if (!nationalId) {
        showToast('Ingresa una cédula', 'error');
        return;
    }

    try {
        // Buscamos el cliente via endpoint de clients
        const res = await fetch(`/admin/clients/search?nationalId=${encodeURIComponent(nationalId)}`);
        const result = await res.json();

        if (!result.ok) {
            showToast('No se encontró un cliente con esa cédula', 'error');
            document.getElementById('client-found').classList.add('hidden');
            document.getElementById('vehicle-client-id').value = '';
            return;
        }

        const c = result.data;
        document.getElementById('vehicle-client-id').value = c.id;
        showClientFound(c.name + ' ' + c.lastName, c.phone);

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}

function showClientFound(name, phone) {
    document.getElementById('client-found').classList.remove('hidden');
    document.getElementById('client-found-name').textContent = '✓ ' + name;
    document.getElementById('client-found-phone').textContent = phone;
}

/* ── GUARDAR (crear o editar) ─────────────────────── */

async function saveVehicle() {

    const clientId = document.getElementById('vehicle-client-id').value.trim();
    const plate    = document.getElementById('vehicle-plate').value.trim();
    const type     = document.getElementById('vehicle-type').value;
    const brand    = document.getElementById('vehicle-brand').value.trim();
    const color    = document.getElementById('vehicle-color').value.trim();

    if (!clientId) {
        showToast('Busca y selecciona un cliente primero', 'error');
        return;
    }

    if (!plate) {
        showToast('La placa es obligatoria', 'error');
        return;
    }

    if (!type) {
        showToast('Selecciona el tipo de vehículo', 'error');
        return;
    }

    const payload = { clientId, plate, type, brand, color };

    const btn = document.getElementById('btn-save-vehicle');
    btn.disabled = true;
    btn.textContent = 'Guardando...';

    try {
        let res;

        if (isEditing) {
            payload.id = document.getElementById('vehicle-id').value;
            res = await fetch('/admin/vehicles/update', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        } else {
            res = await fetch('/admin/vehicles/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        }

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error guardando el vehículo', 'error');
            return;
        }

        showToast(isEditing ? 'Vehículo actualizado' : 'Vehículo registrado exitosamente');
        closeModal();
        setTimeout(() => location.reload(), 1000);

    } catch (e) {
        showToast('Error de conexión', 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Guardar';
    }
}

/* ── TOGGLE ACTIVO / INACTIVO ─────────────────────── */

async function toggleVehicle(vehicleId, isActive) {

    const action = isActive ? 'desactivar' : 'activar';
    if (!confirm(`¿Deseas ${action} este vehículo?`)) return;

    try {
        const res = await fetch(`/admin/vehicles/toggle/${vehicleId}`, {
            method: 'PATCH'
        });

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error actualizando estado', 'error');
            return;
        }

        showToast('Estado del vehículo actualizado');
        setTimeout(() => location.reload(), 1000);

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}

/* ── CERRAR MODAL CON ESC ─────────────────────────── */
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
});

/* ── CERRAR MODAL CLICK FUERA ─────────────────────── */
document.getElementById('vehicle-modal').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) closeModal();
});