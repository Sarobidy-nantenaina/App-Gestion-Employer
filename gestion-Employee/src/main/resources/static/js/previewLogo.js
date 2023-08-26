    function previewLogo(event) {
        var fileInput = event.target;
        var logoPreview = document.getElementById('logoPreview');

        if (fileInput.files && fileInput.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                logoPreview.src = e.target.result;
                logoPreview.style.display = 'block'; // Afficher l'image prévisualisée
            }

            reader.readAsDataURL(fileInput.files[0]);
        } else {
            logoPreview.style.display = 'none'; // Cacher l'image prévisualisée s'il n'y a pas de fichier sélectionné
        }
    }

    // Écouteur d'événement pour appeler la fonction previewLogo lorsque le fichier est sélectionné
    document.getElementById('logo').addEventListener('change', previewLogo);
