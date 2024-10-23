/*
await se utiliza para esperar a que la promesa devuelta por fetch
se resuelva, es decir, esperar a que los datos lleguen del servidor.
*/

const apiClient = (() => {
    const url = "http://localhost:8080/blueprints/";

    const getBlueprintsByAuthor = async (authName, callback) => {
        try {
            const response = await fetch(`${url}${authName}`);
            const data = await response.json();
            callback(data);
        } catch (error) {
            location.reload();
        }
    };

    const getBlueprintsByNameAndAuthor = async (authName, bpName, callback) => {
        try {
            const response = await fetch(`${url}${authName}/${bpName}`);
            const data = await response.json();
            callback(data);
        } catch (error) {
            console.error('Error searching for blueprints by author and name:', error);
        }
    };

    const updateBlueprint = async (authName, bpName, points) => {
        var promise = $.ajax({
            url: 'http://localhost:8080/blueprints/' + authName + "/" + bpName,
            type: 'PUT',
            data: JSON.stringify(points),
            contentType: "application/json"
        })
        return promise;
    };

    const createBlueprint = async (authName, bpName, points) => {
        var json = JSON.stringify({author: authName, points: points, name: bpName});
        var promise = $.ajax({
            url: 'http://localhost:8080/blueprints',
            type: 'POST',
            data: json,
            contentType: "application/json"
        })
        return promise;
    };

    const deleteBlueprint = async (authName, bpName) => {
            var promise = $.ajax({
                url: 'http://localhost:8080/blueprints/' + authName + "/" + bpName,
                type: 'DELETE'
            })
            return promise;
        };

    return {
        getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor,
        updateBlueprint,
        createBlueprint,
        deleteBlueprint
    };
})();
