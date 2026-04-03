// DOM Elements
const registrationForm = document.getElementById('registration-form');
const step1 = document.getElementById('step-1');
const step2 = document.getElementById('step-2');
const step3 = document.getElementById('step-3');

const header1 = document.getElementById('header-step-1');
const header2 = document.getElementById('header-step-2');
const header3 = document.getElementById('header-step-3');

const btnToStep2 = document.getElementById('btn-to-step-2');
const btnToStep3 = document.getElementById('btn-to-step-3');
const btnBackTo1 = document.getElementById('btn-back-to-1');
const btnBackTo1From3 = document.getElementById('btn-back-to-1-from-3');

const displayEmail = document.getElementById('display-email');
const emailInput = document.getElementById('email');
const otpInputs = document.querySelectorAll('.otp-input');

// Password Elements (Step 3)
const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('password-confirm');
const checkLength = document.getElementById('check-length');
const checkUpper = document.getElementById('check-upper');
const checkMatch = document.getElementById('check-match');

// Navigation Helpers
function showStep(stepNumber) {
    [step1, step2, step3].forEach((s, i) => s.classList.toggle('hidden', i + 1 !== stepNumber));
    [header1, header2, header3].forEach((h, i) => h.classList.toggle('hidden', i + 1 !== stepNumber));
    
    if (stepNumber === 2) {
        displayEmail.textContent = emailInput.value;
        setTimeout(() => otpInputs[0].focus(), 100);
    }
}

// Step 1 -> Step 2
if (btnToStep2) {
    btnToStep2.addEventListener('click', () => {
        if (emailInput.value && emailInput.checkValidity()) {
            showStep(2);
        } else {
            emailInput.classList.add('border-red-500');
            emailInput.addEventListener('input', () => emailInput.classList.remove('border-red-500'), { once: true });
        }
    });
}

// Step 2 -> Step 3
if (btnToStep3) {
    btnToStep3.addEventListener('click', () => {
        const otpCode = Array.from(otpInputs).map(input => input.value).join('');
        if (otpCode.length === 6) {
            // In a real app, you'd verify the OTP via API here
            showStep(3);
        } else {
            otpInputs.forEach(input => {
                if (!input.value) {
                    input.classList.add('border-red-500');
                    input.addEventListener('input', () => input.classList.remove('border-red-500'), { once: true });
                }
            });
        }
    });
}

// Back buttons
if (btnBackTo1) btnBackTo1.addEventListener('click', () => showStep(1));
if (btnBackTo1From3) btnBackTo1From3.addEventListener('click', () => showStep(1));

// OTP Logic (6 Digits)
otpInputs.forEach((input, index) => {
    input.addEventListener('input', (e) => {
        if (e.inputType === 'deleteContentBackward') return;
        if (!/^\d$/.test(input.value)) {
            input.value = '';
            return;
        }
        if (input.value && index < otpInputs.length - 1) {
            otpInputs[index + 1].focus();
        }
    });

    input.addEventListener('keydown', (e) => {
        if (e.key === 'Backspace' && !input.value && index > 0) {
            otpInputs[index - 1].focus();
        }
    });

    input.addEventListener('paste', (e) => {
        e.preventDefault();
        const data = e.clipboardData.getData('text').slice(0, 6);
        if (/^\d+$/.test(data)) {
            const digits = data.split('');
            digits.forEach((digit, i) => {
                if (otpInputs[i]) otpInputs[i].value = digit;
            });
            otpInputs[Math.min(digits.length, otpInputs.length - 1)].focus();
        }
    });
});

// Password Validation (Step 3)
function updateCheckUI(element, isValid) {
    if (!element) return;
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
    
    updateCheckUI(checkLength, val.length >= 8);
    updateCheckUI(checkUpper, /[A-Z]/.test(val));
    
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

// Password Toggles
const togglePassword = document.getElementById('toggle-password');
if (togglePassword) {
    togglePassword.addEventListener('click', () => {
        passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
        togglePassword.querySelector('span').textContent = passwordInput.type === 'password' ? 'visibility_off' : 'visibility';
    });
}

const togglePasswordConfirm = document.getElementById('toggle-password-confirm');
if (togglePasswordConfirm) {
    togglePasswordConfirm.addEventListener('click', () => {
        passwordConfirmInput.type = passwordConfirmInput.type === 'password' ? 'text' : 'password';
        togglePasswordConfirm.querySelector('span').textContent = passwordConfirmInput.type === 'password' ? 'visibility_off' : 'visibility';
    });
}

// Final Submit
if (registrationForm) {
    registrationForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        // Final validations could go here
        const parkingName = document.getElementById('parking-name').value;
        const adminName = document.getElementById('admin-name').value;
        
        if (!parkingName || !adminName || !passwordInput.value || passwordInput.value !== passwordConfirmInput.value) {
            alert('Por favor completa todos los campos correctamente.');
            return;
        }

        console.log('Registro Exitoso:', {
            email: emailInput.value,
            parking: parkingName,
            admin: adminName
        });

        alert('¡Registro completado con éxito!');
        window.location.href = '/auth/login';
    });
}
