const searchInput = document.getElementById('search-input');
const faqItems = document.querySelectorAll('.faq-item');
const noResults = document.getElementById('no-results');

if (searchInput) {
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase().trim();
        let hasResults = false;
        
        faqItems.forEach(item => {
            const text = item.textContent.toLowerCase();
            if (query === "" || text.includes(query)) {
                item.style.display = 'block';
                hasResults = true;
            } else {
                item.style.display = 'none';
            }
        });

        if (noResults) {
            if (hasResults || query === "") {
                noResults.classList.add('hidden');
            } else {
                noResults.classList.remove('hidden');
            }
        }
    });
}
