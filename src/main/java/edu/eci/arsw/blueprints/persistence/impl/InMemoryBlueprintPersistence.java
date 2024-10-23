/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 *
 * @author hcadavid
 */
@Service
@Component
@Qualifier("inMemoryBluePrintPersistence")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentHashMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts1 = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp1 = new Blueprint("Juan", "bpHouse", pts1);
        blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);

        Point[] pts2 = new Point[] { new Point(10, 10), new Point(120, 120) };
        Blueprint bp2 = new Blueprint("Juan", "bpOffice", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

        Point[] pts3 = new Point[] { new Point(1, 1), new Point(100, 100) };
        Blueprint bp3 = new Blueprint("David", "bpHouse", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Object result = blueprints.putIfAbsent(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        System.out.println(result);
        if(!(result == null)){
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Tuple<String, String> key : blueprints.keySet()) {
            if (author.equals(key.o1)) {
                authorBlueprints.add(getBlueprint(key.o1, key.o2));
            }
        }
        return authorBlueprints;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Tuple<String, String> key : blueprints.keySet()) {
            authorBlueprints.add(getBlueprint(key.o1, key.o2));
        }
        return authorBlueprints;
    }

    @Override
    public void updateBlueprint(String author, String name, List<Point> points) throws BlueprintNotFoundException {
        Blueprint bpToUpdate = getBlueprint(author, name);
        synchronized(bpToUpdate){
            bpToUpdate.setPoints(points);  
        }     
    }

    @Override
    public void deleteBlueprint(String author, String bpname){
        blueprints.remove(new Tuple<>(author, bpname));
    }
} 