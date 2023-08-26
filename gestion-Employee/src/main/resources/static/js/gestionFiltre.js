    document.addEventListener('DOMContentLoaded', function () {
        const filterBySelect = document.getElementById('filterBy');
        const orderByLabel = document.getElementById('order-label');
        const orderBySelect = document.getElementById('orderBy');
        const keywordInput = document.getElementById('keyword');
        const codePaysLabel = document.getElementById('code-pays'); // Le label du champ "Code pays"
        const codePaysSelect = document.getElementById('phoneCountryCode'); // Le select du champ "Code pays"

        // Fonction pour masquer la section "order" (asc et desc) du formulaire
        function hideOrderSection() {
            orderByLabel.style.display = 'none';
            orderBySelect.style.display = 'none';
            orderBySelect.value = '';
        }

        // Fonction pour afficher la section "order" (asc et desc) du formulaire
        function showOrderSection() {
            orderByLabel.style.display = 'inline-block';
            orderBySelect.style.display = 'inline-block';
        }

        // Fonction pour masquer l'option "Code pays"
        function hideCodePaysOption() {
            codePaysLabel.style.display = 'none';
            codePaysSelect.style.display = 'none';
            codePaysSelect.value = ''; // Réinitialiser la sélection du code pays
        }

        // Fonction pour afficher l'option "Code pays"
        function showCodePaysOption() {
            codePaysLabel.style.display = 'inline-block';
            codePaysSelect.style.display = 'inline-block';
        }

        // Masquer la section "order" et l'option "Code pays" au chargement initial de la page
        hideOrderSection();
        hideCodePaysOption();

        // Écouter le changement dans le champ "Filter by"
        filterBySelect.addEventListener('change', function () {
            if (filterBySelect.value === 'sexe') {
                // Si "sexe" est sélectionné, masquer le champ "orderBy" et le label "Order"
                hideOrderSection();
                // Masquer l'option "Code pays" également
                hideCodePaysOption();
            } else if (filterBySelect.value === 'dateEmbauche' || filterBySelect.value === 'dateDepart') {
                // Si "dateEmbauche" ou "dateDepart" est sélectionné, afficher le champ "orderBy" et le label "Order"
                showOrderSection();
                // Masquer l'option "Code pays" également
                hideCodePaysOption();
            } else if (filterBySelect.value === 'telephones') {
                // Si "Téléphone" est sélectionné, afficher l'option "Code pays"
                showCodePaysOption();
                // Masquer la section "order" (asc et desc) du formulaire
                hideOrderSection();
            } else {
                // Pour les autres options, afficher le champ "orderBy" et le label "Order"
                showOrderSection();
                // Masquer l'option "Code pays" également
                hideCodePaysOption();
            }
        });

        // Écouter le changement dans le champ "Code pays"
        codePaysSelect.addEventListener('change', function () {
            // Récupérer la valeur sélectionnée dans le champ "Code pays"
            const selectedCodePays = codePaysSelect.value;

            // Récupérer la valeur actuelle du champ "keyword"
            const currentKeyword = keywordInput.value.trim();

            // Vérifier si le champ "keyword" est vide
            if (currentKeyword === '') {
                // Si le champ "keyword" est vide, mettre simplement la valeur du code pays sélectionné
                keywordInput.value = selectedCodePays;
            } else {
                // Si le champ "keyword" contient déjà une valeur, ajouter le code pays sélectionné à la fin avec une virgule
                keywordInput.value = currentKeyword + ', ' + selectedCodePays;
            }
        });

        // Écouter la saisie dans le champ "keyword"
        keywordInput.addEventListener('input', function () {
            if (filterBySelect.value !== 'sexe' && (filterBySelect.value === 'dateEmbauche' || filterBySelect.value === 'dateDepart') && keywordInput.value.trim() !== '') {
                // Si "dateEmbauche" ou "dateDepart" est sélectionné et que le champ "keyword" contient une valeur,
                // masquer la section "order" (asc et desc) du formulaire
                hideOrderSection();
            } else if (filterBySelect.value !== 'sexe' && (filterBySelect.value === 'dateEmbauche' || filterBySelect.value === 'dateDepart') && keywordInput.value.trim() === '') {
                // Si "dateEmbauche" ou "dateDepart" est sélectionné et que le champ "keyword" est vide,
                // afficher la section "order" (asc et desc) du formulaire
                showOrderSection();
            }
        });

        // Écouter le soumission du formulaire
        document.getElementById('filterForm').addEventListener('submit', function () {
            // Supprimer les espaces en début et fin du mot clé
            keywordInput.value = keywordInput.value.trim();

            // Vérifier si le champ "order" est vide
            if (orderBySelect.value === '') {
                // Supprimer le champ "order" du formulaire avant de le soumettre
                orderBySelect.remove();
            }
        });

        // Au chargement initial de la page, vérifier l'état du champ "keyword" pour décider de l'affichage initial de la section "order"
        if (
            (filterBySelect.value === 'dateEmbauche' || filterBySelect.value === 'dateDepart') &&
            keywordInput.value.trim() !== ''
        ) {
            hideOrderSection();
        }
    });
