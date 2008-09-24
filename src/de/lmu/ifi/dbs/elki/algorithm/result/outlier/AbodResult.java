/**
 * 
 */
package de.lmu.ifi.dbs.elki.algorithm.result.outlier;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import de.lmu.ifi.dbs.elki.algorithm.result.AbstractResult;
import de.lmu.ifi.dbs.elki.data.ClassLabel;
import de.lmu.ifi.dbs.elki.data.RealVector;
import de.lmu.ifi.dbs.elki.database.AssociationID;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.normalization.NonNumericFeaturesException;
import de.lmu.ifi.dbs.elki.normalization.Normalization;
import de.lmu.ifi.dbs.elki.utilities.ComparablePair;
import de.lmu.ifi.dbs.elki.utilities.UnableToComplyException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.AttributeSettings;

public class AbodResult<V extends RealVector<V, ?>> extends AbstractResult<V> {
  /**
   * Marker for a file name containing abod values.
   */
  public static final String ABOD_MARKER = "abod";

  ComparablePair<Double, Integer>[] result;

  public AbodResult(Database<V> data, ComparablePair<Double, Integer>[] result) {
    super(data);
    this.result = result;
  }

  @Override
  public void output(File out, Normalization<V> normalization, List<AttributeSettings> settings) throws UnableToComplyException {
    PrintStream outStream;
    try {
      File abodFile = new File(out.getAbsolutePath() + File.separator + ABOD_MARKER + FILE_EXTENSION);
      abodFile.getParentFile().mkdirs();
      PrintStream abodOut = new PrintStream(new FileOutputStream(abodFile));
      output(abodOut, normalization, settings);
      abodOut.flush();

    }
    catch(Exception e) {
      outStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
      output(outStream, normalization, settings);
    }
  }

  @Override
  public void output(PrintStream outStream, Normalization<V> normalization, List<AttributeSettings> settings) throws UnableToComplyException {
    writeHeader(outStream, settings, null);

    try {
      for(ComparablePair<Double, Integer> p : result) {
        Integer id = p.getSecond();

        outStream.print(id);
        outStream.print(" ");

        V object = db.get(id);
        if(normalization != null) {
          V restored = normalization.restore(object);
          outStream.print(restored.toString());
        }
        else {
          outStream.print(object.toString());
        }
        outStream.print(" ");

        String label = db.getAssociation(AssociationID.LABEL, id);
        if(label != null) {
          outStream.print(label);
          outStream.print(" ");
        }

        ClassLabel classLabel = db.getAssociation(AssociationID.CLASS, id);
        if(classLabel != null) {
          outStream.print(classLabel);
          outStream.print(" ");
        }

        outStream.print("ABOD=");
        outStream.print(p.getFirst());
        outStream.println();
      }

    }
    catch(NonNumericFeaturesException e) {
      throw new UnableToComplyException(e);
    }
  }
}