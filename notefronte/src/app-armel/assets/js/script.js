    function capitalizeWords(input) {
        return input.toLowerCase().replace(/(?:^|\s)\S/g, function (a) {
            return a.toUpperCase();
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById('studentForm');

        const capitalizeFields = ['lastname', 'firstname', 'parentLastname', 'parentFirstname'];

        capitalizeFields.forEach(function (id) {
            const input = document.getElementById(id);
            input.addEventListener('input', function () {
                input.value = capitalizeWords(input.value);
            });
        });

        form.addEventListener('submit', function (e) {
            capitalizeFields.forEach(function (id) {
                const input = document.getElementById(id);
                input.value = capitalizeWords(input.value);
            });
        });
    });
