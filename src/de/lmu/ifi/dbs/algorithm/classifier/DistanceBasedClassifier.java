package de.lmu.ifi.dbs.algorithm.classifier;


import java.util.List;

import de.lmu.ifi.dbs.data.ClassLabel;
import de.lmu.ifi.dbs.data.DatabaseObject;
import de.lmu.ifi.dbs.distance.Distance;
import de.lmu.ifi.dbs.distance.distancefunction.DistanceFunction;
import de.lmu.ifi.dbs.distance.distancefunction.EuklideanDistanceFunction;
import de.lmu.ifi.dbs.properties.Properties;
import de.lmu.ifi.dbs.utilities.UnableToComplyException;
import de.lmu.ifi.dbs.utilities.Util;
import de.lmu.ifi.dbs.utilities.optionhandling.AttributeSettings;
import de.lmu.ifi.dbs.utilities.optionhandling.ClassParameter;
import de.lmu.ifi.dbs.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.utilities.optionhandling.WrongParameterValueException;


/**
 * An abstract classifier already based on DistanceBasedAlgorithm
 * making use of settings for time and verbose and DistanceFunction.
 *
 * @author Arthur Zimek
 */
public abstract class DistanceBasedClassifier<O extends DatabaseObject, D extends Distance<D>, L extends ClassLabel<L>> extends AbstractClassifier<O ,L> {
  /**
   * The default distance function.
   */
  public static final String DEFAULT_DISTANCE_FUNCTION = EuklideanDistanceFunction.class.getName();

  /**
   * Parameter for distance function.
   */
  public static final String DISTANCE_FUNCTION_P = "distancefunction";

  /**
   * Description for parameter distance function.
   */
  public static final String DISTANCE_FUNCTION_D = "the distance function to determine the distance between database objects" +
                                                   Properties.KDD_FRAMEWORK_PROPERTIES.restrictionString(DistanceFunction.class)+
                                                   ". Default: " + DEFAULT_DISTANCE_FUNCTION;

  /**
   * The distance function.
   */
  private DistanceFunction<O, D> distanceFunction;


  /**
   * Adds parameter for distance function to parameter map.
   */
  protected DistanceBasedClassifier() {
    super();
    ClassParameter<DistanceFunction> distance = new ClassParameter<DistanceFunction>(DISTANCE_FUNCTION_P,DISTANCE_FUNCTION_D,DistanceFunction.class);
    distance.setDefaultValue(DEFAULT_DISTANCE_FUNCTION);
    optionHandler.put(DISTANCE_FUNCTION_P, distance);
  }


  /**
   * Provides a classification for a given instance.
   * The classification is the index of the class-label
   * in {@link #labels labels}.
   * <p/>
   * This method returns the index of the maximum probability
   * as provided by {@link #classDistribution(DatabaseObject) classDistribution(M)}.
   * If an extending classifier requires a different classification,
   * it should overwrite this method.
   *
   * @param instance an instance to classify
   * @return a classification for the given instance
   * @throws IllegalStateException if the Classifier has not been initialized
   *                               or properly trained
   */
  public int classify(O instance) throws IllegalStateException {
    return Util.getIndexOfMaximum(classDistribution(instance));
  }

  /**
   * Returns the parameter setting of the attributes.
   *
   * @return the parameter setting of the attributes
   */
  public List<AttributeSettings> getAttributeSettings() {
    List<AttributeSettings> attributeSettings = super.getAttributeSettings();
    attributeSettings.addAll(distanceFunction.getAttributeSettings());
    return attributeSettings;
  }


  /**
   * Returns the distanceFunction.
   *
   * @return the distanceFunction
   */
  protected DistanceFunction<O, D> getDistanceFunction() {
    return distanceFunction;
  }


  /**
   * @see AbstractClassifier#setParameters(String[])
   */
  @Override
  public String[] setParameters(String[] args) throws ParameterException {
    String[] remainingParameters = super.setParameters(args);

    //parameter distance function
    String className = (String)optionHandler.getOptionValue(DISTANCE_FUNCTION_P);
    try {
      //noinspection unchecked
      distanceFunction = Util.instantiate(DistanceFunction.class, className);
    }
    catch (UnableToComplyException e) {
      throw new WrongParameterValueException(DISTANCE_FUNCTION_P, className, DISTANCE_FUNCTION_D, e);
    }
    remainingParameters = distanceFunction.setParameters(remainingParameters);
    setParameters(args, remainingParameters);
    return remainingParameters;
  }


}
