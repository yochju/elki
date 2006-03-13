package de.lmu.ifi.dbs.wrapper;

import de.lmu.ifi.dbs.algorithm.KDDTask;
import de.lmu.ifi.dbs.algorithm.KNNJoin;
import de.lmu.ifi.dbs.algorithm.clustering.DeLiClu;
import de.lmu.ifi.dbs.database.DeLiCluTreeDatabase;
import de.lmu.ifi.dbs.database.connection.AbstractDatabaseConnection;
import de.lmu.ifi.dbs.normalization.AttributeWiseRealVectorNormalization;
import de.lmu.ifi.dbs.utilities.optionhandling.NoParameterValueException;
import de.lmu.ifi.dbs.utilities.optionhandling.OptionHandler;
import de.lmu.ifi.dbs.utilities.optionhandling.UnusedParameterException;
import de.lmu.ifi.dbs.utilities.optionhandling.WrongParameterValueException;

import java.util.List;

/**
 * Wrapper class for the DeliClu algorithm.
 *
 * @author Elke Achtert (<a href="mailto:achtert@dbs.ifi.lmu.de">achtert@dbs.ifi.lmu.de</a>)
 */
public class DeliCluWrapper extends FileBasedDatabaseConnectionWrapper {
  /**
   * Main method to run this wrapper.
   *
   * @param args the arguments to run this wrapper
   */
  public static void main(String[] args) {
    DeliCluWrapper wrapper = new DeliCluWrapper();
    try {
      wrapper.run(args);
    }
    catch (WrongParameterValueException e) {
      System.err.println(wrapper.optionHandler.usage(e.getMessage()));
    }
    catch (NoParameterValueException e) {
      System.err.println(wrapper.optionHandler.usage(e.getMessage()));
    }
    catch (UnusedParameterException e) {
      System.err.println(wrapper.optionHandler.usage(e.getMessage()));
    }
  }

  /**
   * Sets the parameter minpts, pagesize and cachesize in the parameter map additionally to the
   * parameters provided by super-classes.
   */
  public DeliCluWrapper() {
    super();
    parameterToDescription.put(DeLiClu.MINPTS_P + OptionHandler.EXPECTS_VALUE, DeLiClu.MINPTS_D);
    parameterToDescription.put(DeLiCluTreeDatabase.PAGE_SIZE_P + OptionHandler.EXPECTS_VALUE, DeLiCluTreeDatabase.PAGE_SIZE_D);
    parameterToDescription.put(DeLiCluTreeDatabase.CACHE_SIZE_P + OptionHandler.EXPECTS_VALUE, DeLiCluTreeDatabase.CACHE_SIZE_D);
    optionHandler = new OptionHandler(parameterToDescription, getClass().getName());
  }

  /**
   * @see KDDTaskWrapper#getParameters()<java.lang.String>)
   */
  public List<String> getParameters() {
    List<String> parameters = super.getParameters();

    // deliclu algorithm
    parameters.add(OptionHandler.OPTION_PREFIX + KDDTask.ALGORITHM_P);
    parameters.add(DeLiClu.class.getName());

    // knn join
    parameters.add(OptionHandler.OPTION_PREFIX + KNNJoin.K_P);
    parameters.add(optionHandler.getOptionValue(DeLiClu.MINPTS_P));

    // minpts
    parameters.add(OptionHandler.OPTION_PREFIX + DeLiClu.MINPTS_P);
    parameters.add(optionHandler.getOptionValue(DeLiClu.MINPTS_P));

    // normalization
    parameters.add(OptionHandler.OPTION_PREFIX + KDDTask.NORMALIZATION_P);
    parameters.add(AttributeWiseRealVectorNormalization.class.getName());
    parameters.add(OptionHandler.OPTION_PREFIX + KDDTask.NORMALIZATION_UNDO_F);

    // database
    parameters.add(OptionHandler.OPTION_PREFIX + AbstractDatabaseConnection.DATABASE_CLASS_P);
    parameters.add(DeLiCluTreeDatabase.class.getName());

    // bulk load
    parameters.add(OptionHandler.OPTION_PREFIX + DeLiCluTreeDatabase.BULK_LOAD_F);

    // page size
    if (optionHandler.isSet(DeLiCluTreeDatabase.PAGE_SIZE_P)) {
      parameters.add(OptionHandler.OPTION_PREFIX + DeLiCluTreeDatabase.PAGE_SIZE_P);
      parameters.add(DeLiCluTreeDatabase.PAGE_SIZE_P);
    }

    // cache size
    if (optionHandler.isSet(DeLiCluTreeDatabase.CACHE_SIZE_P)) {
      parameters.add(OptionHandler.OPTION_PREFIX + DeLiCluTreeDatabase.CACHE_SIZE_P);
      parameters.add(DeLiCluTreeDatabase.CACHE_SIZE_P);
    }

    return parameters;
  }
}
