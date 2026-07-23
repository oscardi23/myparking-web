/* =====================================================
   ADMIN OPERATORS
===================================================== */

/* ── MODAL ────────────────────────────────────────── */

function openCreateModal() {
    clearForm();
    document.getElementById('operator-modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('operator-modal').classList.add('hidden');
    clearForm();
}

function clearForm() {
    document.getElementById('op-name').value     = '';
    document.getElementById('op-lastname').value = '';
    document.getElementById('op-email').value    = '';
    document.getElementById('op-phone').value    = '';
    document.getElementById('op-password').value = '';
}

/* ── CREAR OPERADOR ───────────────────────────────── */

async function saveOperator() {

    const name     = document.getElementById('op-name').value.trim();
    const lastName = document.getElementById('op-lastname').value.trim();
    const email    = document.getElementById('op-email').value.trim();
    const phone    = document.getElementById('op-phone').value.trim();
    const password = document.getElementById('op-password').value;

    if (!name || !lastName || !email || !password) {
        showToast('Completa los campos obligatorios', 'error');
        return;
    }

    if (password.length < 8) {
        showToast('La contraseña debe tener mínimo 8 caracteres', 'error');
        return;
    }

    const payload = {
        name,
        lastName,
        email,
        numberPhone: phone,
        password,
        confirmPassword: password  // el admin define la contraseña directamente
    };

    const btn = document.getElementById('btn-save-operator');
    btn.disabled = true;
    btn.textContent = 'Creando...';

    try {
        const res = await fetch('/admin/operators/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error creando el operador', 'error');
            return;
        }

        showToast('Operador creado exitosamente');
        closeModal();
        setTimeout(() => location.reload(), 1000);

    } catch (e) {
        showToast('Error de conexión', 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Crear operador';
    }
}

/* ── TOGGLE ACTIVO / INACTIVO ─────────────────────── */

async function toggleOperator(operatorId, isActive) {

    const action = isActive ? 'desactivar' : 'activar';
    if (!confirm(`¿Deseas ${action} este operador?`)) return;

    try {
        const res = await fetch(`/admin/operators/toggle/${operatorId}`, {
            method: 'PATCH'
        });

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error actualizando estado', 'error');
            return;
        }

        showToast('Estado del operador actualizado');
        setTimeout(() => location.reload(), 1000);

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}

/* ── CERRAR CON ESC / CLICK FUERA ─────────────────── */
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
});

document.getElementById('operator-modal').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) closeModal();
});