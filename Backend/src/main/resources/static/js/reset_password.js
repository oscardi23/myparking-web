// Elements
const resetPasswordForm = document.getElementById('reset-password-form');
const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('password-confirm');
const checkLength = document.getElementById('check-length');
const checkUpper = document.getElementById('check-upper');
const checkMatch = document.getElementById('check-match');

// Toggles
const togglePassword = document.getElementById('toggle-password');
const toggleConfirm = document.getElementById('toggle-confirm');

if (togglePassword) {
    togglePassword.addEventListener('click', () => {
        passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
        togglePassword.querySelector('span').textContent = passwordInput.type === 'password' ? 'visibility_off' : 'visibility';
    });
}

if (toggleConfirm) {
    toggleConfirm.addEventListener('click', () => {
        passwordConfirmInput.type = passwordConfirmInput.type === 'password' ? 'text' : 'password';
        toggleConfirm.querySelector('span').textContent = passwordConfirmInput.type === 'password' ? 'visibility_off' : 'visibility';
    });
}

// Validation Logic
function updateCheckUI(element, isValid) {
    const icon = element.querySelector('.material-symbols-outlined');
    if (isValid) {
        element.classList.remove('text-slate-500', 'text-red-500');
        element.classList.add('text-green-500');
        icon.textContent = 'check';
    } else {
        element.classList.remove('text-green-500', 'text-slate-500');
        element.classList.add('text-red-500');
        icon.textContent = 'close';
    }
}

function validatePasswords() {
    const val = passwordInput.value;
    const confirmVal = passwordConfirmInput.value;
    
    // Length (8+)
    updateCheckUI(checkLength, val.length >= 8);
    // Upper
    updateCheckUI(checkUpper, /[A-Z]/.test(val));
    // Match
    if (confirmVal) {
        updateCheckUI(checkMatch, val === confirmVal && val !== '');
    } else {
        checkMatch.classList.remove('text-green-500', 'text-red-500');
        checkMatch.classList.add('text-slate-500');
        checkMatch.querySelector('.material-symbols-outlined').textContent = 'close';
    }
}

if (passwordInput) passwordInput.addEventListener('input', validatePasswords);
if (passwordConfirmInput) passwordConfirmInput.addEventListener('input', validatePasswords);

// Form Submission
if (resetPasswordForm) {
    resetPasswordForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const val = passwordInput.value;
        const confirmVal = passwordConfirmInput.value;
        
        const isLengthValid = val.length >= 8;
        const isUpperValid = /[A-Z]/.test(val);
        const isMatching = val === confirmVal && val !== '';
        // Se necesita la logica del backend para que funcione correctamente
        if (isLengthValid && isUpperValid && isMatching) {
            console.log('aqui va la logica del backend');
            // Redirect to Login
            window.location.href = '/auth/login';
        } else {
            console.log('Por favor cumple con todos los requisitos de seguridad antes de continuar.');
        }
    });
}
