// DOM Elements
const recoveryForm = document.getElementById('recovery-form');

const step1 = document.getElementById('step-1');
const step2 = document.getElementById('step-2');
const step3 = document.getElementById('step-3');

const header1 = document.getElementById('header-step-1');
const header2 = document.getElementById('header-step-2');
const header3 = document.getElementById('header-step-3');

const btnToStep2 = document.getElementById('btn-to-step-2');
const btnToStep3 = document.getElementById('btn-to-step-3');
const btnBackTo1 = document.getElementById('btn-back-to-1');

const displayEmail = document.getElementById('display-email');
const emailInput = document.getElementById('email');
const otpInputs = document.querySelectorAll('.otp-input');

const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('password-confirm');

const checkLength = document.getElementById('check-length');
const checkUpper = document.getElementById('check-upper');
const checkMatch = document.getElementById('check-match');

const btnText = document.getElementById('btnText');
const btnSpinner = document.getElementById('btnSpinner');

/* =====================================================
   TOAST
===================================================== */
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = "fixed top-5 right-5 px-4 py-3 rounded-lg shadow-lg text-white font-semibold";
    toast.classList.add(type === 'success' ? 'bg-green-500' : 'bg-red-500');
    toast.classList.remove('hidden');
    setTimeout(() => toast.classList.add('hidden'), 2500);
}

/* =====================================================
   TIMER
===================================================== */
let countdown;
const timerDisplay = document.getElementById('timer');
const btnResend = document.getElementById('btn-resend');

function startTimer(durationInSeconds) {
    clearInterval(countdown);
    let timer = durationInSeconds;

    btnResend.disabled = true;
    btnResend.classList.add('opacity-50', 'cursor-not-allowed');

    countdown = setInterval(() => {
        let minutes = Math.floor(timer / 60);
        let seconds = timer % 60;

        const minStr = minutes < 10 ? '0' + minutes : minutes;
        const secStr = seconds < 10 ? '0' + seconds : seconds;

        timerDisplay.textContent = `${minStr}:${secStr}`;

        if (--timer < 0) {
            clearInterval(countdown);
            timerDisplay.textContent = "Expirado";
            btnResend.disabled = false;
            btnResend.classList.remove('opacity-50', 'cursor-not-allowed');
            showToast('El código ha expirado, solicita uno nuevo', 'error');
        }
    }, 1000);
}

/* =====================================================
   STEP NAVIGATION
===================================================== */
function showStep(stepNumber) {
    [step1, step2, step3].forEach((s, i) => s.classList.toggle('hidden', i + 1 !== stepNumber));
    [header1, header2, header3].forEach((h, i) => h.classList.toggle('hidden', i + 1 !== stepNumber));

    if (stepNumber === 2) {
        displayEmail.textContent = emailInput.value;
        setTimeout(() => otpInputs[0].focus(), 100);
    }
}

function clearOTPInputs() {
    otpInputs.forEach(input => input.value = '');
}

function resetButton() {
    btnToStep2.disabled = false;
    btnText.textContent = "Enviar código";
    btnSpinner.classList.add('hidden');
}

/* =====================================================
   STEP 1 → ENVIAR CÓDIGO
===================================================== */
btnToStep2.addEventListener('click', async () => {

    if (!emailInput.value || !emailInput.checkValidity()) return;

    btnToStep2.disabled = true;
    btnText.textContent = "Enviando";
    btnSpinner.classList.remove('hidden');

    try {
        const res = await fetch('/auth/forgot-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `email=${encodeURIComponent(emailInput.value)}`
        });

        const ok = await res.json();

        if (!ok) {
            showToast('No existe una cuenta con ese correo', 'error');
            resetButton();
            return;
        }

        showToast('Código enviado con éxito');
        showStep(2);
        startTimer(600);

    } catch (error) {
        showToast('Error de conexión', 'error');
        resetButton();
    }
});

/* =====================================================
   STEP 2 → VERIFICAR OTP
===================================================== */
btnToStep3.addEventListener('click', async () => {

    const code = Array.from(otpInputs).map(i => i.value).join('');

    if (code.length !== 6) {
        showToast('Ingresa los 6 dígitos', 'error');
        return;
    }

    try {
        const res = await fetch('/auth/verify-code', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `email=${encodeURIComponent(emailInput.value)}&code=${code}`
        });

        const ok = await res.json();

        if (!ok) {
            showToast('Código incorrecto o expirado', 'error');
            return;
        }

        showToast('Código validado exitosamente');
        clearInterval(countdown);
        showStep(3);

    } catch (error) {
        showToast('Error de conexión', 'error');
    }
});

/* =====================================================
   STEP 3 → CAMBIAR CONTRASEÑA
===================================================== */
recoveryForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (!passwordInput.value || passwordInput.value.length < 8) {
        showToast('La contraseña debe tener mínimo 8 caracteres', 'error');
        return;
    }

    if (passwordInput.value !== passwordConfirmInput.value) {
        showToast('Las contraseñas no coinciden', 'error');
        return;
    }

    try {
        const res = await fetch('/auth/reset-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `email=${encodeURIComponent(emailInput.value)}&code=${Array.from(otpInputs).map(i => i.value).join('')}&newPassword=${encodeURIComponent(passwordInput.value)}`
        });

        const result = await res.json();

        if (!result.ok) {
            showToast('Error al cambiar la contraseña, intenta de nuevo', 'error');
            return;
        }

        showToast('¡Contraseña cambiada exitosamente!');
        setTimeout(() => window.location.href = '/auth/login', 1500);

    } catch (error) {
        showToast('Error de conexión', 'error');
    }
});

/* =====================================================
   BACK BUTTON
===================================================== */
btnBackTo1.addEventListener('click', () => {
    const confirmCancel = confirm('¿Desea cancelar la recuperación de contraseña?');
    if (!confirmCancel) return;
    resetButton();
    clearOTPInputs();
    showStep(1);
});

/* =====================================================
   REENVIAR CÓDIGO
===================================================== */
btnResend.addEventListener('click', async () => {
    btnResend.disabled = true;
    btnResend.textContent = "Enviando...";

    try {
        const res = await fetch('/auth/forgot-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `email=${encodeURIComponent(emailInput.value)}`
        });

        const ok = await res.json();

        if (ok) {
            showToast('Nuevo código enviado');
            btnResend.textContent = 'Reenviar código';
            startTimer(600);
        }

    } catch (error) {
        showToast('Error al reenviar', 'error');
        btnResend.disabled = false;
    }
});

/* =====================================================
   OTP AUTO FOCUS
===================================================== */
otpInputs.forEach((input, index) => {
    input.addEventListener('input', () => {
        if (input.value && index < otpInputs.length - 1) {
            otpInputs[index + 1].focus();
        }
    });

    input.addEventListener('keydown', (e) => {
        if (e.key === 'Backspace' && !input.value && index > 0) {
            otpInputs[index - 1].focus();
        }
    });
});

/* =====================================================
   PASSWORD VALIDATION UI
===================================================== */
function updateCheckUI(element, isValid) {
    const icon = element.querySelector('.material-symbols-outlined');
    element.classList.toggle('text-green-500', isValid);
    element.classList.toggle('text-red-500', !isValid);
    icon.textContent = isValid ? 'check' : 'close';
}

function validatePasswords() {
    const val = passwordInput.value;
    updateCheckUI(checkLength, val.length >= 8);
    updateCheckUI(checkUpper, /[A-Z]/.test(val));
    updateCheckUI(checkMatch, val === passwordConfirmInput.value && val !== '');
}

passwordInput.addEventListener('input', validatePasswords);
passwordConfirmInput.addEventListener('input', validatePasswords);

/* =====================================================
   TOGGLE PASSWORD VISIBILITY
===================================================== */
function setupPasswordToggle(toggleId, inputId) {
    const toggleBtn = document.getElementById(toggleId);
    const inputField = document.getElementById(inputId);
    const icon = toggleBtn.querySelector('.material-symbols-outlined');

    toggleBtn.addEventListener('click', () => {
        const isPassword = inputField.type === 'password';
        inputField.type = isPassword ? 'text' : 'password';
        icon.textContent = isPassword ? 'visibility' : 'visibility_off';
        if (isPassword) {
            toggleBtn.classList.add('text-primary');
        } else {
            toggleBtn.classList.remove('text-primary');
        }
    });
}

setupPasswordToggle('toggle-password', 'password');
setupPasswordToggle('toggle-password-confirm', 'password-confirm');
