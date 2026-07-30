/* =====================================================
   ADMIN PARKING
===================================================== */

const isNew = document.getElementById('btn-save-text').textContent.trim() === 'Crear parqueadero';

/* ── TABS OF RATES ──────────────────────────────── */

function showRateTab(type) {
    document.querySelectorAll('.rate-panel').forEach(p => p.classList.add('hidden'));
    document.querySelectorAll('.tab-btn').forEach(b => {
        b.classList.remove('border-primary', 'text-primary');
        b.classList.add('border-transparent', 'text-slate-500');
    });

    document.getElementById(`rate-${type}`).classList.remove('hidden');
    const tab = document.getElementById(`tab-${type}`);
    tab.classList.add('border-primary', 'text-primary');
    tab.classList.remove('border-transparent', 'text-slate-500');
}

/* ── BUILD PAYLOAD ────────────────────────────── */

function buildPayload() {
    const types = ['CAR', 'MOTORCYCLE', 'BICYCLE', 'TRUCK'];
    const rates = {};

    types.forEach(type => {
        rates[type] = {
            minimumRate: parseFloat(document.getElementById(`rate-${type}-min`).value)  || 0,
            hourlyRate:  parseFloat(document.getElementById(`rate-${type}-hour`).value) || 0,
            dailyRate:   parseFloat(document.getElementById(`rate-${type}-day`).value)  || 0,
            monthlyRate: parseFloat(document.getElementById(`rate-${type}-month`).value)|| 0,
        };
    });

    return {
        id:                    document.getElementById('parking-id').value || null,
        name:                  document.getElementById('parking-name').value.trim(),
        address:               document.getElementById('parking-address').value.trim(),
        phone:                 document.getElementById('parking-phone').value.trim(),
        nit:                   document.getElementById('parking-nit').value.trim(),
        totalSpotsCar:         parseInt(document.getElementById('spots-car').value)   || 0,
        totalSpotsMotorcycle:  parseInt(document.getElementById('spots-moto').value)  || 0,
        totalSpotsBicycle:     parseInt(document.getElementById('spots-bike').value)  || 0,
        totalSpotsTruck:       parseInt(document.getElementById('spots-truck').value) || 0,
        rates
    };
}

/* ──   SAVE ──────────────────────────────────────── */

async function saveParking() {

    const payload = buildPayload();

    if (!payload.name || !payload.address || !payload.phone || !payload.nit) {
        showToast('Completa los campos obligatorios', 'error');
        return;
    }

    const btn = document.getElementById('btn-save-text');
    const originalText = btn.textContent;
    btn.textContent = 'Guardando...';

    try {
        const url    = isNew ? '/parking/create' : '/parking/update';
        const method = isNew ? 'POST' : 'PUT';

        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error guardando', 'error');
            return;
        }

        showToast(isNew ? 'Parqueadero creado exitosamente' : 'Cambios guardados');
        if (isNew) setTimeout(() => location.reload(), 1200);

    } catch (e) {
        showToast('Error de conexión', 'error');
    } finally {
        btn.textContent = originalText;
    }
}

/* ── TOGGLE ACTIVO / INACTIVO ─────────────────────── */

async function toggleParking() {

    if (!confirm('¿Deseas cambiar el estado del parqueadero?')) return;

    try {
        const res = await fetch('/parking/toggle', { method: 'PATCH' });
        const result = await res.json();

        if (!result.ok) {
            showToast(result.message ?? 'Error actualizando estado', 'error');
            return;
        }

        showToast('Estado del parqueadero actualizado');
        setTimeout(() => location.reload(), 1000);

    } catch (e) {
        showToast('Error de conexión', 'error');
    }
}