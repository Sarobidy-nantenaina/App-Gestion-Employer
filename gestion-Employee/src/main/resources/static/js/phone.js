    document.getElementById('phoneCountryCode').addEventListener('change', updatePhoneNumber);
    document.getElementById('telephone').addEventListener('input', updatePhoneNumber);

    function updatePhoneNumber() {
        var phoneCountryCode = document.getElementById('phoneCountryCode').value;
        var phoneNumber = document.getElementById('telephone').value;

        // Divise les numéros par des virgules
        var numbersArray = phoneNumber.split(',').map(function (number) {
            var trimmedNumber = number.trim();
            // Vérifie si le numéro commence déjà avec un code pays
            if (trimmedNumber.startsWith("+")) {
                return trimmedNumber; // Conserve le numéro tel quel s'il a déjà un code pays
            } else {
                return "+" + phoneCountryCode + " " + trimmedNumber; // Ajoute le code pays sinon
            }
        });

        // Rejoint les numéros pour former le numéro de téléphone final
        var formattedPhoneNumber = numbersArray.join(', ');

        // Remplit le champ de téléphone avec les numéros complets (avec les codes de pays)
        document.getElementById('telephone').value = formattedPhoneNumber;
    }