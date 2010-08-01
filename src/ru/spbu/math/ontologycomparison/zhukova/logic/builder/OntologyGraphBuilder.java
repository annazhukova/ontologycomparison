package ru.spbu.math.ontologycomparison.zhukova.logic.builder;

//import net.sourceforge.fluxion.utils.OWLTransformationException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasonerException;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.IOntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.ClassAnnotationVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.OntologyManager;
import ru.spbu.math.ontologycomparison.zhukova.logic.builder.loader.impl.PropertyVisitor;
import ru.spbu.math.ontologycomparison.zhukova.logic.ontologygraph.IOntologyGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Anna Zhukova
 */
public class OntologyGraphBuilder {
    private IOntologyManager ontologyManager;

    public IOntologyGraph build(String ontologyPath)
            throws FileNotFoundException/*, OWLTransformationException*/, OWLReasonerException {
        ontologyManager = new OntologyManager(new FileInputStream(ontologyPath));
        return ontologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
    }

    public IOntologyGraph build(File ontologyFile)
            throws FileNotFoundException/*, OWLTransformationException*/, OWLReasonerException {
        ontologyManager = new OntologyManager(ontologyFile);
        return ontologyManager.load(new ClassAnnotationVisitor(), new PropertyVisitor());
    }

    public OWLOntology getOntology() {
        return ontologyManager == null ? null : ontologyManager.getOntology();
    }

}
