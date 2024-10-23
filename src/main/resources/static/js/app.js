const app = (() => {
    var author = "";
    var bpname = "";
    var blueprints = [];
    var api = apiClient;
    var points = [];

    const setAuthor = (newAuthor) => {
        author = newAuthor;
        bpname = "";
        $('#selectedAuthor').text(author + "'s Blueprints");
    };

    const getBlueprintsByAuthor = async (author) => {
        setAuthor(author);
        try {
            await api.getBlueprintsByAuthor(author, openBlueprint);
            clearCanvas();
        } catch (error) {
            console.error('Error fetching blueprints by author:', error);
        }
    };

    const openBlueprint = (authorsBlueprints) => {
        $('#blueprintsTable tbody').empty();
        blueprints = authorsBlueprints.map(bp => ({
            name: bp.name,
            numberOfPoints: bp.points.length
        }));

        blueprints.forEach(bp => {
            const markup = `
                <tr>
                    <td>${bp.name}</td>
                    <td>${bp.numberOfPoints}</td>
                    <td>
                        <button type="button" onclick="app.getBlueprintsByNameAndAuthor('${author}', '${bp.name}')">
                            Open
                        </button>
                    </td>
                </tr>
            `;
            $("#blueprintsTable tbody").append(markup);
        });

        const totalPoints = blueprints.reduce((acc, bp) => acc + bp.numberOfPoints, 0);
        $('#userPoints').text(totalPoints);
    };

    const getBlueprintsByNameAndAuthor = async (author, name) => {
        try {
            await api.getBlueprintsByNameAndAuthor(author, name, drawBlueprints);
        } catch (error) {
            console.error('Error fetching blueprint by name and author:', error);
        }
    };

    const drawBlueprints = (blueprint) =>{
        bpname = blueprint.name;
        points = blueprint.points;
        if(points.length != 0){
            const c = document.getElementById('myCanvas');
            const ctx = c.getContext('2d');

            clearCanvas();
            ctx.moveTo(blueprint.points[0].x, blueprint.points[0].y);
            for (var i = 1 ; i < blueprint.points.length ; i++){
                ctx.lineTo(blueprint.points[i].x, blueprint.points[i].y);
                ctx.moveTo(blueprint.points[i].x, blueprint.points[i].y);
            }
            ctx.stroke();
        }
        $('#selectedBlueprint').text(bpname);
    };

    const clearCanvas = () => {
        const c = document.getElementById('myCanvas');
        const ctx = c.getContext('2d');
        ctx.clearRect(0, 0, c.width, c.height);
        ctx.beginPath();
    };

    const initCanvas = () => {
        const c = document.getElementById("myCanvas");
        const ctx = c.getContext("2d");
        if(window.PointerEvent) {
            scratchBlueprint("pointerdown", c, ctx);
        }
        else {
            scratchBlueprint("mousedown", c, ctx);
        }
    };

    const scratchBlueprint = (pointerType, c, ctx) => {
        c.addEventListener(pointerType, function(event){
            if (bpname !== "") {
                const rect = c.getBoundingClientRect();
                const posX = event.clientX - rect.left;
                const posY = event.clientY - rect.top;
                if (posX >= 0 && posX <= c.width && posY >= 0 && posY <= c.height) {
                    points.push({"x": posX, "y": posY});
                    ctx.lineTo(posX, posY);
                    ctx.moveTo(posX, posY);
                    ctx.stroke();
                }
            }
        });
    };

    const saveBlueprint = () => {
        var promise = api.updateBlueprint(author, bpname, points);
        promise.then(() => api.getBlueprintsByAuthor(author, openBlueprint))
               .then(() => api.getBlueprintsByNameAndAuthor(author, bpname, drawBlueprints));
   };

    const createBlueprint = () => {
        clearCanvas();
        bpname = $('#bpname').val();
        points = [];
        var promise = api.createBlueprint(author, bpname, points);
        promise.then(() => api.getBlueprintsByAuthor(author, openBlueprint))
               .then(() => api.getBlueprintsByNameAndAuthor(author, bpname, drawBlueprints));
    };

    const deleteBlueprint = () => {
        clearCanvas();
        var promise = api.deleteBlueprint(author, bpname);
        promise.then(() => api.getBlueprintsByAuthor(author, openBlueprint))
    };

    return {
        setAuthor,
        getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor,
        initCanvas,
        saveBlueprint,
        createBlueprint,
        deleteBlueprint
    };
})();
