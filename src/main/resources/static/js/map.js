$(document).ready(function(){
    // getting data
    axios.get(`/api/regional`)
        .then(function (response) {
            const regionalData = response.data;

            regionalData.forEach(element => {
                const removedDiacritics = element.region.normalize("NFD")
                    .replace(/[\u0300-\u036f]/g, "").replace("ł", "l");

                // preparing label
                let label = "<b>" + element.region.charAt(0).toUpperCase() + element.region.substring(1) + "</b>";
                label += "<br/>";
                label += "Potwierdzone: " + element.confirmed;
                label += "<br/>";
                label += "Zgony: " + element.deaths;

                document.querySelector(`a[href*="#${removedDiacritics}"]`).innerHTML = label;
            });
        })
        .catch(function (error) {
            console.log(error);
        });

    // CSSMap;
    $("#map-poland").CSSMap({
        "size": 750,
        "mapStyle": "blue",
        "tooltips": "sticky",
        "responsive": "auto",
        "authorInfo": true
    });
    // END OF THE CSSMap;

});