package ru.spbu.math.ontologycomparison.zhukova.logic.similarity.mappers;

import ru.spbu.math.ontologycomparison.zhukova.logic.similarity.comparators.IComparator;
import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;
import ru.spbu.math.ontologycomparison.zhukova.util.Pair;

/**
 * @author Anna Zhukova
 */
public abstract class Mapper<C1, C2, P> implements IMapper<C1,C2,P> {

    protected boolean tryToBind(IComparator<C1, C2, P> comparator, C1 first, C2 second, ITriple<P, String, String>... bindFactors) {
        for (ITriple<P, String, String> bind : bindFactors) {
            if (tryToBind(comparator, first, second, bind.getFirst(), bind.getSecond(), bind.getThird())) {
                return true;
            }
        }
        return false;
    }

    protected boolean tryToBind(IComparator<C1, C2, P> comparator, C1 first, C2 second, P relation, String reasonForOrigin, String reasonForFound) {
        Pair<C1, C2> pair = comparator.areSimilar(first, second, relation);
        if (pair != null) {
            bind(first, second, reasonForOrigin);
            bind(pair.getFirst(), pair.getSecond(), reasonForFound);
            return true;
        }
        return false;
    }

}
