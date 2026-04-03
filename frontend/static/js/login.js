const togglePassword = document.getElementById('toggle-password');
if (togglePassword) {
    togglePassword.addEventListener('click', () => {
        const password = document.getElementById('password');
        const icon = togglePassword.querySelector('span');
        if (password.type === 'password') {
            password.type = 'text';
            if (icon) icon.textContent = 'visibility';
        } else {
            password.type = 'password';
            if (icon) icon.textContent = 'visibility_off';
        }
    });
}

// Login Form Submission
const loginForm = document.querySelector('form');
if (loginForm && !window.location.href.includes('register')) {
    loginForm.addEventListener('submit', (e) => {
        // e.preventDefault(); // Uncomment when connecting to actual backend
        console.log('Intento de inicio de sesión');
    });
}
