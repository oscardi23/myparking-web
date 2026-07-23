/* =====================================================
   ADMIN CLIENTS
===================================================== */

let isEditing = false;

/* ── MODAL ────────────────────────────────────────── */

function openCreateModal() {
    isEditing = false;
    clearForm();
    document.getElementById('modal-title').textContent = 'Nuevo cliente';
    document.getElementById('client-modal').classList.remove('hidden');
}

async function openEditModal(clientId) {
    isEditing = true;
    document.getElementById('modal-title').textContent = 'Editar cliente';

    try {
        const res = await fetch(`/admin/clients/${clientId}`);
        const result = await res.json();

        if (!result.ok) {
            showToast('Error cargando el cliente', 'error');
            return;
        }

        const c = result.data;
        document.getElementById('client-id').value        = c.id;
        document.getElementById('client-name').value      = c.name;
        document.getElementById('client-lastname').value  = c.lastName;
        document.getElementById('client-nationalid').value = c.nationalId;
        document.getElementById('client-phone').value     = c.phone;
        document.getElementById('client-email').value     = c.email ?? '';

        document.getElementById('client-modal').classList.remove('hidden');

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}

function closeModal() {
    document.getElementById('client-modal').classList.add('hidden');
    clearForm();
}

function clearForm() {
    document.getElementById('client-id').value         = '';
    document.getElementById('client-name').value       = '';
    document.getElementById('client-lastname').value   = '';
    document.getElementById('client-nationalid').value = '';
    document.getElementById('client-phone').value      = '';
    document.getElementById('client-email').value      = '';
}

/* ── GUARDAR (crear o editar) ─────────────────────── */

async function saveClient() {

    const name       = document.getElementById('client-name').value.trim();
    const lastName   = document.getElementById('client-lastname').value.trim();
    const nationalId = document.getElementById('client-nationalid').value.trim();
    const phone      = document.getElementById('client-phone').value.trim();
    const email      = document.getElementById('client-email').value.trim();

    if (!name || !lastName || !nationalId || !phone) {
        showToast('Completa los campos obligatorios', 'error');
        return;
    }

    const payload = { name, lastName, nationalId, phone, email };

    const btn = document.getElementById('btn-save-client');
    btn.disabled = true;
    btn.textContent = 'Guardando...';

    try {
        let res;

        if (isEditing) {
            payload.id = document.getElementById('client-id').value;
            res = await fetch('/admin/clients/update', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        } else {
            res = await fetch('/admin/clients/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        }

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error guardando el cliente', 'error');
            return;
        }

        showToast(isEditing ? 'Cliente actualizado' : 'Cliente creado exitosamente');
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

async function toggleClient(clientId, isActive) {

    const action = isActive ? 'desactivar' : 'activar';
    if (!confirm(`¿Deseas ${action} este cliente?`)) return;

    try {
        const res = await fetch(`/admin/clients/toggle/${clientId}`, {
            method: 'PATCH'
        });

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error actualizando estado', 'error');
            return;
        }

        showToast('Estado del cliente actualizado');
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
document.getElementById('client-modal').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) closeModal();
});