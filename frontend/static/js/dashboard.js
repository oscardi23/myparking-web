document.addEventListener("DOMContentLoaded", () => {
    const menuToggle = document.getElementById("menu-toggle");
    const closeSidebar = document.getElementById("close-sidebar");
    const sidebar = document.getElementById("admin-sidebar");
    const backdrop = document.getElementById("sidebar-backdrop");

    function toggleSidebar(show) {
        if (show) {
            // Show backdrop
            backdrop.classList.remove("hidden");
            // Small delay to allow transition
            setTimeout(() => {
                backdrop.classList.add("opacity-100");
                sidebar.classList.remove("-translate-x-full");
                sidebar.classList.add("translate-x-0");
            }, 10);
        } else {
            // Hide sidebar
            sidebar.classList.replace("translate-x-0", "-translate-x-full");
            backdrop.classList.replace("opacity-100", "opacity-0");
            
            // Wait for transition before hiding backdrop
            setTimeout(() => {
                backdrop.classList.add("hidden");
            }, 300);
        }
    }

    if (menuToggle) {
        menuToggle.addEventListener("click", () => toggleSidebar(true));
    }

    if (closeSidebar) {
        closeSidebar.addEventListener("click", () => toggleSidebar(false));
    }

    if (backdrop) {
        backdrop.addEventListener("click", () => toggleSidebar(false));
    }

    // Auto-close on resize if switching to desktop
    window.addEventListener("resize", () => {
        if (window.innerWidth >= 1024) { // lg breakpoint
            if (!backdrop.classList.contains("hidden")) {
                toggleSidebar(false);
            }
        }
    });
});
