    function previewImage(event) {
        var fileInput = event.target;
        var previewImg = document.getElementById('previewImage');

        if (fileInput.files && fileInput.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                previewImg.src = e.target.result;
            }

            reader.readAsDataURL(fileInput.files[0]);
        }
    }

    // Écouteur d'événement pour appeler la fonction previewImage lorsque le fichier est sélectionné
    document.getElementById('image').addEventListener('change', previewImage);