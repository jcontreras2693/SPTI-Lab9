package edu.eci.arsw.blueprints.persistence.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.Filter;


@Service
@Qualifier("RedundancyFilter")
public class Redundancy implements Filter {

    List<Point> original;
    List<Point> duplicated;
    List<Point> x;

    @Override
    public Blueprint filter(Blueprint blueprint) {
        original = blueprint.getPoints();
        duplicated = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            for (int j = i + 1; j < original.size(); j++) {
                if (areEquals(original.get(i), original.get(j))) {
                    duplicated.add(original.get(i));
                    break;
                }
            }
        }
        blueprint.setPoints(removeDuplicated(duplicated, original));
        return blueprint;
    }

    @Override
    public Set<Blueprint> multiFilter(Set<Blueprint> blueprints) {
        for (Blueprint i : blueprints) {
            filter(i);
        }
        return blueprints;
    }

    public boolean areEquals(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && p1.getY() == p2.getY());
    }

    public List<Point> removeDuplicated(List<Point> duplicatedPoints, List<Point> ptsAll) {
        x = new ArrayList<>(ptsAll);
        for (Point i: duplicatedPoints) {
            x.remove(i);
        }
        return x;
    }

}
