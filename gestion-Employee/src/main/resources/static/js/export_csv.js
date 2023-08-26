    document.getElementById("exportCsvButton").addEventListener("click", function () {
        // Récupérer le contenu de la table au format CSV
        const table = document.getElementById("employeeTable");
        const rows = table.getElementsByTagName("tr");
        let csvContent = "data:text/csv;charset=utf-8,";

        for (let i = 0; i < rows.length; i++) {
            const cells = rows[i].getElementsByTagName("td");
            let rowContent = [];
            // Ignorer la dernière colonne contenant les actions
            for (let j = 0; j < cells.length - 1; j++) {
                rowContent.push(cells[j].innerText);
            }
            csvContent += rowContent.join(",") + "\n";
        }

        // Créer un lien de téléchargement pour le fichier CSV
        const encodedUri = encodeURI(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", "employees.csv");

        // Ajouter le lien à la page et le déclencher pour télécharger le fichier CSV
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    });
