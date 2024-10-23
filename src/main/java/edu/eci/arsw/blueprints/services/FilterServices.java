package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class FilterServices {
    @Autowired
    @Qualifier("RedundancyFilter")
    Filter filter;

    public void filter(Blueprint blueprint)   {
        filter.filter(blueprint);
    }

    public void multiFilter(Set<Blueprint> blueprints) {
        filter.multiFilter(blueprints);
    }
}
