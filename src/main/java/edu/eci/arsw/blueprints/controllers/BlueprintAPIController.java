package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    private final BlueprintsServices blueprintsServices;

    @Autowired
    public BlueprintAPIController(BlueprintsServices blueprintsServices) {
        this.blueprintsServices = blueprintsServices;
    }

    /**
     * Retorna todos los planos creados.
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetBlueprints(){
        try {
            return new ResponseEntity<>(blueprintsServices.getAllBlueprints(),HttpStatus.ACCEPTED);
        } catch ( BlueprintNotFoundException ex) {
            return new ResponseEntity<>("BLUEPRINTS NOT FOUND",HttpStatus.NOT_FOUND);
        }        
    }

    /**
     * Retorna todos los planos del autor. 
     * @param autor
     * @return
     */
    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> obtenerPlanosPorAutor(@PathVariable("author") String autor) {
        try {
            Set<?> bp = blueprintsServices.getBlueprintsByAuthor(autor);
            if (bp.isEmpty()) {
                return new ResponseEntity<>("AUTHOR NOT FOUND", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bp, HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retorna los planos por autor y nombre.
     * @param autor
     * @param blueprint
     * @return
     */
    @RequestMapping(value = "/{author}/{blueprint}", method = RequestMethod.GET)
    public ResponseEntity<?> obtenerPlanoPorAutorYNombre(@PathVariable("author") String autor, @PathVariable("blueprint") String blueprint) {
        try {
            Blueprint bp = blueprintsServices.getBlueprint(autor, blueprint);
            if (bp.equals(null)) {
                return new ResponseEntity<>("BLUEPRINT NOT FOUND", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bp, HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea un nuevo plano.
     * @param bp
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostRecursoAñadirBlueprint(@RequestBody Blueprint bp) {
        try {
            blueprintsServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED, HttpStatus.OK);
        } catch (BlueprintPersistenceException e) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza los puntos del plano indicado.
     * @param autor
     * @param blueprint
     * @param points
     * @return
     */
    @RequestMapping(value = "/{author}/{blueprint}", method = RequestMethod.PUT)
    public ResponseEntity<?> manejadorPostRecursoXX(@PathVariable("author") String autor, @PathVariable("blueprint") String blueprint, @RequestBody List<Point> points) {
        try {
            blueprintsServices.updateBlueprint(autor, blueprint, points);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza los puntos del plano indicado.
     * @param author
     * @param bpname
     */
    @RequestMapping(value = "/{author}/{blueprint}", method = RequestMethod.DELETE)
    public void manejadorDeleteRecursoXX(@PathVariable("author") String author, @PathVariable("blueprint") String bpname) {
        blueprintsServices.deleteBlueprint(author, bpname);
    }
}
