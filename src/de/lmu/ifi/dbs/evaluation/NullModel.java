package de.lmu.ifi.dbs.evaluation;

import de.lmu.ifi.dbs.algorithm.classifier.Classifier;
import de.lmu.ifi.dbs.data.ClassLabel;
import de.lmu.ifi.dbs.data.DatabaseObject;
import de.lmu.ifi.dbs.database.Database;
import de.lmu.ifi.dbs.utilities.Util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A model describing the database and the available class labels. As an empty
 * model this model may be suitable for lazy learners. XXX probably no need for
 * this class
 * 
 * @author Arthur Zimek 
 */
public class NullModel<O extends DatabaseObject, L extends ClassLabel<L>,C extends Classifier<O,L>>
        extends AbstractClassifierEvaluation<O, L,C>
{
    /**
     * The labels available for classification.
     */
    protected List<L> labels;

    /**
     * Provides a new NullModel for the given database and labels.
     * 
     * @param db
     *            the database where the NullModel is bsaed on
     * @param classifier
     *            the classifier related to the null model
     * @param labels
     *            the labels available for classification
     */
    public NullModel(Database<O> db, Database<O> testset, C classifier,
            L[] labels)
    {
        super(db, testset, classifier);
        this.labels = new ArrayList<L>(labels.length);
        for (L label : labels)
        {
            this.labels.add(label);
        }
    }

    /**
     * 
     * 
     * @see de.lmu.ifi.dbs.evaluation.Evaluation#outputEvaluationResult(java.io.PrintStream)
     */
    public void outputEvaluationResult(PrintStream output)
    {
        output.print("### classes:\n### ");
        Util.print(this.labels, ",", output);
        output.println();
    }

}
