/*============================================

admin layout -sidebar toggle (mobile)

=================================*/

function toggleSidebar(){

const sidebar = document.getElementById('sidebar');
const overlay = document.getElementById('sidebar-overlay');

const isOpen = !sidebar.classList.contains('-translate-x-full');


if(isOpen){

    sidebar.classList.add('-translate-x-full');
    overlay.classList.add('hidden');

}else {

    sidebar.classList.remove('-translate-x-full');
    overlay.classList.remove('hidden');

    
}

}

/* toast global*/

function showToast(message, type = 'success'){


const toast = document.getElementById('toast');
toast.textContent = message

toast.className = "fixed top-5 right-5 px-4 py-3 rounded-lg text-white font-semibold z-50";
toast.classList.add(type === 'success' ? 'bg-green-500' : 'bg-red-500');
toast.classList.remove('hidden');

setTimeout(() => toast.classList.add('hidden'), 2500);

}