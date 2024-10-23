/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.BlockElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.persistence.Filter;
/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {

    @Autowired
    @Qualifier(value = "inMemoryBluePrintPersistence")
    BlueprintsPersistence bpp;

    @Autowired
    FilterServices filter;

    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException{
        bpp.saveBlueprint(bp);
    }

    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException{
        Set<Blueprint> blueprints;
        blueprints = bpp.getAllBlueprints();
        return blueprints;

    }

    /**
     *
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        Blueprint blueprint;
        blueprint = bpp.getBlueprint(author, name);
        filter.filter(blueprint);
        return blueprint;
    }

    /**
     * Updates the blueprint's points.
     * @param author
     * @param name
     * @param points
     * @throws BlueprintNotFoundException
     */
    public void updateBlueprint(String author,String name, List<Point> points)throws BlueprintNotFoundException{
        bpp.updateBlueprint(author, name, points);
    }
    /**
     *
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        Set<Blueprint> blueprints = bpp.getBlueprintsByAuthor(author);
        Set<Blueprint> blueprintsFiltered = new HashSet<>();
        for(Blueprint bp: blueprints){
            filter.filter(bp);
            blueprintsFiltered.add(bp);
        }
        return blueprintsFiltered;
    }

    public void deleteBlueprint(String author,String name){
        bpp.deleteBlueprint(author, name);
    }

}
