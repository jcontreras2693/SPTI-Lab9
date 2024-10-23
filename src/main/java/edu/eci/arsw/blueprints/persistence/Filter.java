package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import java.util.Set;

public interface Filter {
    Blueprint filter(Blueprint blueprint);
    Set<Blueprint> multiFilter(Set<Blueprint> blueprints);
}