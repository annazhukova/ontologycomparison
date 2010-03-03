package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators;

import ru.spbu.math.ontologycomparison.zhukova.util.Pair;

import java.util.Set;

/**
 * @author Anna Zhukova
 */
public abstract class Comparator<C1, C2, P> {

    public Pair<C1, C2> areSimilar(C1 first, C2 second, P property) {
        Set<C1> firstSet =  getByFirstProperty(first, property);
        Set<C2> secondSet = getBySecondProperty(second, property);
        return areSimilar(firstSet, secondSet);
    }

    public abstract Set<C1> getByFirstProperty(C1 concept, P property);

    public abstract Set<C2> getBySecondProperty(C2 concept, P property);

    protected Pair<C1, C2> areSimilar(Set<C1> firstSet, Set<C2> secondSet) {
        for (C1 first : firstSet){
            for (C2 second : secondSet) {
                if (areSimilar(first, second)) {
                    return new Pair<C1, C2>(first, second);
                }
            }
        }
        return null;
    }

    public abstract boolean areSimilar(C1 first, C2 second);
}
