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

const btnText = document.getElementById('btnText');
const btnSpinner = document.getElementById('btnSpinner');

// functions

function clearOTPInputS(){

otpInputs.forEach(input => {
input.value = '';

});


}



// send code timer

let countdown;
const timerDisplay = document.getElementById('timer');
const btnResend = document.getElementById('btn-resend');

function starTimer(durationInSeconds) {

    clearInterval(countdown);
    let timer = durationInSeconds;

    btnResend.disabled = true;
    btnResend.classList.add('opacity-50', 'cursor-not-allowed');

    countdown = setInterval(() => {

    let minutes = Math.floor(timer / 60);
    let seconds = timer % 60;

   const minStr = minutes < 10 ? '0' + minutes : minutes;
   const secStr = seconds < 10 ? "0" +  seconds : seconds;

    timerDisplay.textContent = `${minStr}:${secStr}`;

    if(--timer < 0) {

    clearInterval(countdown);
    timerDisplay.textContent = "Expirado";

    btnResend.disabled = false;
    btnResend.classList.remove('opacity-50', 'cursor-not-allowed');

    showToast('El codigo ha expirado, solicita uno nuevo', "error")

    }


    }, 1000);

}




/*  alert modal  */

function showToast(message, type = 'success'){

    const toast = document.getElementById('toast');

    toast.textContent = message;

    toast.className = "fixed top-5 right-5 px-4 py-3 rounded-lg shadow-lg text-white font-semi-bold";

    toast.classList.add(
        type === 'success' ? 'bg-green-500' : 'bg-red-500'
    );

    toast.classList.remove('hidden');

    setTimeout(() => {

        toast.classList.add('hidden');
    }, 2500);


}


/* =====================================================
   STEP NAVIGATION
===================================================== */

function showStep(stepNumber) {
    [step1, step2, step3].forEach((s, i) => s.classList.toggle('hidden', i + 1 !== stepNumber));

    if (stepNumber === 2) {
        displayEmail.textContent = emailInput.value;
        setTimeout(() => otpInputs[0].focus(), 100);
    }
}
/* =====================================================
   STEP 1 → SEND EMAIL CODE (AJAX)
===================================================== */

btnToStep2.addEventListener('click', async () => {

    if (!emailInput.value || !emailInput.checkValidity()) return;

   

    btnToStep2.disabled = true;
    btnText.textContent = "Enviando";
    btnSpinner.classList.remove('hidden');

    try {

    const res = await fetch('/auth/send-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `email=${encodeURIComponent(emailInput.value)}`
    });

    const ok = await res.json();

    if (!ok) {
        showToast('Este correo ya se encuentra registrado', 'error')

        resetButton();
        return;
    }

    showToast("Codigo enviado con exito")

    showStep(2);
    starTimer(600);

    }catch(error) {

        showToast("Error de conexion", "error");
        resetButton();
    }



});




/*  reset button */

    function resetButton(){
        btnToStep2.disabled = false;
        btnText.textContent = "Siguiente";
        btnSpinner.classList.add('hidden');
    }


/* =====================================================
   STEP 2 → VERIFY OTP (AJAX)
===================================================== */

btnToStep3.addEventListener('click', async () => {

    const code = Array.from(otpInputs).map(i => i.value).join('');

    if (code.length !== 6) {
        showToast('Ingresa los 6 dígitos');
        return;
    }

    const res = await fetch('/auth/verify-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `email=${encodeURIComponent(emailInput.value)}&code=${code}`
    });

    const ok = await res.json();

    if (!ok) {
        showToast('Código incorrecto o expirado', 'error');
        return;
    }

    if(ok){
    showToast('Codigo validado exitosamente')
    clearInterval(countdown);
    showStep(3);
    }
});


/* =====================================================
   STEP 3 → COMPLETE REGISTER (AJAX)
===================================================== */
registrationForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('name').value;
    const lastName = document.getElementById('lastName').value;

    if (!name || !lastName ) {
        showToast('Completa correctamente los campos', 'error');
        return;
    }

    if(passwordInput.value !== passwordConfirmInput.value) {
    showToast('Las contraseñas no coinciden', 'error');
            return;
    }

    const data = {
        email: emailInput.value,
        name,
        lastName,
        password: passwordInput.value,
        confirmPassword: passwordConfirmInput.value


    };

    const res = await fetch('/auth/complete-register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const result = await res.json();

    if (!result.ok) {
        showToast('Correo no verificado', 'error');
        return;
    }

    showToast('Registro exitoso');
    window.location.href = '/auth/login';
});

/* =====================================================
   BACK BUTTONS
===================================================== */

btnBackTo1.addEventListener('click', () => {

    const confirmCancel = confirm('Desea cancelar la validacion del codigo');

    if(!confirmCancel) return;


    resetButton();
    clearOTPInputS();
    showStep(1)
})


btnBackTo1From3.addEventListener('click', () => {

const confirmCancel2 = confirm('Desea cancelar el proceso registro?');

    if(!confirmCancel2) return;
    resetButton();
    clearOTPInputS();
    showStep(1)
})





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

/*  function show input password*/
function setupPasswordToggle(toggleId, input) {

    const toggleBtn = document.getElementById(toggleId);
    const inputField = document.getElementById(input);
    const icon = toggleBtn.querySelector('.material-symbols-outlined');

    toggleBtn.addEventListener('click', () => {

    const isPassword = inputField.type === 'password';
    inputField.type = isPassword ? 'text' : 'password';

    icon.textContent = isPassword ?  'visibility' : 'visibility_off';

    if (isPassword) {
                toggleBtn.classList.add('text-primary');
            } else {
                toggleBtn.classList.remove('text-primary');
            }


    });



}
 setupPasswordToggle('toggle-password', 'password');
 setupPasswordToggle('toggle-password-confirm', 'password-confirm');


 // send code timer

 btnResend.addEventListener('click', async () => {

    btnResend.disabled = true;
    btnResend.textContent = "Enviando";
    btnSpinner.classList.remove('hidden');

    try{
        const res = await fetch('/auth/send-code', {

            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded'},
            body: `email=${encodeURIComponent(emailInput.value)}`
        
        });



        const ok = await res.json();

        if(ok){
            showToast('Nuevo codigo enviado');
            btnResend.textContent = 'Reenviar codigo';
            starTimer(600);

        }

    }catch(error){
        showToast('Error al enviar', 'error')
        btnResend.disable = false;
    }
    
})