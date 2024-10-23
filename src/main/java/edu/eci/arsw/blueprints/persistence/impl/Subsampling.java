package edu.eci.arsw.blueprints.persistence.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SubsamplingFilter")
public class Subsampling implements Filter{

    List<Point> original;
    List<Point> newPoints;

    @Override
    public Blueprint filter(Blueprint blueprint) {
        original = blueprint.getPoints();
        newPoints = new ArrayList<>();
        for(int i = 0; i < original.size(); i += 2){
            newPoints.add(original.get(i));
        }
        blueprint.setPoints(newPoints);
        return blueprint;
    }

    @Override
    public Set<Blueprint> multiFilter(Set<Blueprint> blueprints) {
        for (Blueprint blueprint : blueprints) {
            filter(blueprint);
        }
        return blueprints;
    }
}
