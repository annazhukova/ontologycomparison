package ru.spbu.math.ontologycomparision.zhukova.logic.similarity;

import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyGraph;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyConcept;
import ru.spbu.math.ontologycomparision.zhukova.logic.ontologygraph.IOntologyRelation;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.SynsetHelper;
import ru.spbu.math.ontologycomparision.zhukova.logic.similarity.synset.EmptySynset;
import ru.spbu.math.ontologycomparision.zhukova.util.SetHelper;
import ru.spbu.math.ontologycomparision.zhukova.util.IHashTable;
import ru.spbu.math.ontologycomparision.zhukova.util.HashTable;

import java.util.*;

import edu.smu.tspell.wordnet.Synset;

/**
 * @author Anna Zhukova
 */
public class OntologyComparator<C extends IOntologyConcept<C, R>, R extends IOntologyRelation<C>> {
    private SynsetHelper<C, R> firstSynsetHelper;
    private SynsetHelper<C, R> secondSynsetHelper;

    public OntologyComparator(IOntologyGraph<C, R> firstGraph, IOntologyGraph<C, R> secondGraph) {
        this.firstSynsetHelper = new SynsetHelper<C, R>(firstGraph);
        this.secondSynsetHelper = new SynsetHelper<C, R>(secondGraph);
    }

    public double getSimilarity() {
        Set<Synset> firstSynsets = this.firstSynsetHelper.getSynsets();
        Set<Synset> secondSynsets = this.secondSynsetHelper.getSynsets();
        int firstNotSecondSynsetCount = SetHelper.INSTANCE.setIntersection(firstSynsets, secondSynsets).size();
        int firstAndSecondSynsetCount = SetHelper.INSTANCE.setUnion(firstSynsets, secondSynsets).size();
        int firstNoSynsetConceptCount = this.firstSynsetHelper.getConcepsWithNoSynset().size();
        int secondNoSynsetConceptCount = this.secondSynsetHelper.getConcepsWithNoSynset().size();
        return ((double) firstNotSecondSynsetCount + mergeNoSynsetConcepts().size())
                / (firstAndSecondSynsetCount + secondNoSynsetConceptCount + firstNoSynsetConceptCount);
    }

    public IHashTable<Synset, C> merge() {
        IHashTable<Synset, C> firstSynsetToConcept = this.firstSynsetHelper.getSynsetToConceptTable();
        IHashTable<Synset, C> secondSynsetToConcept = this.secondSynsetHelper.getSynsetToConceptTable();
        IHashTable<Synset, C> result = new HashTable<Synset, C>(firstSynsetToConcept);
        result.insertAll(secondSynsetToConcept);
        result.insertAll(mergeNoSynsetConcepts());
        return result;
    }

    public IHashTable<String, C> noSynsetConceptUnion() {
        IHashTable<String, C> result = new HashTable<String, C>(this.firstSynsetHelper.getConcepsWithNoSynset());
        result.insertAll(this.secondSynsetHelper.getConcepsWithNoSynset());
        return result;
    }

    public IHashTable<Synset, C> mergeNoSynsetConcepts() {
        Map<C, Synset> firstConceptToSynsetMap = this.firstSynsetHelper.getConceptToSynsetMap();
        Map<C, Synset> secondConceptToSynsetMap = this.secondSynsetHelper.getConceptToSynsetMap();
        IHashTable<String, C> allNoSynsetConcets = noSynsetConceptUnion();
        IHashTable<Synset, C> result = new HashTable<Synset, C>();
        for (Map.Entry<String, List<C>> entry : allNoSynsetConcets.entrySet()) {
            List<C> values = entry.getValue();
            if (values.size() == 1) {
                result.insert(new EmptySynset(), values.get(0));
            } else {
                C first = values.get(0);
                C second = values.get(1);
                Set<Synset> firstParentSynsets = new HashSet<Synset>();
                for (C parent : first.getAllParents()) {
                    Synset synset = firstConceptToSynsetMap.get(parent);
                    if (synset != null) {
                        firstParentSynsets.add(synset);
                    }
                }
                Set<Synset> secondParentSynsets = new HashSet<Synset>();
                for (C parent : second.getAllParents()) {
                    Synset synset = secondConceptToSynsetMap.get(parent);
                    if (synset != null) {
                        secondParentSynsets.add(synset);
                    }
                }
                if (SetHelper.INSTANCE.setIntersection(firstParentSynsets, secondParentSynsets).size() > 0) {
                    Synset empty = new EmptySynset();
                    result.insert(empty, first);
                    result.insert(empty, second);
                } else {
                    result.insert(new EmptySynset(), first);
                    result.insert(new EmptySynset(), second);
                }
            }
        }
        return result;
    }

}
