package de.lmu.ifi.dbs.elki.data.synthetic.bymodel;

import java.util.List;

import de.lmu.ifi.dbs.elki.math.linearalgebra.Vector;
import de.lmu.ifi.dbs.elki.utilities.UnableToComplyException;

/**
 * Interface for cluster generators
 * 
 * @author Erich Schubert
 */
public interface GeneratorInterface {
  /**
   * Get number of points to be generated by this generator.
   * 
   * @return cluster size
   */
  public int getSize();
  
  /**
   * Get dimensionality of generated data
   * 
   * @return dimensionality
   */
  public int getDim();
  
  /**
   * Generate a specified number of points
   * 
   * @param count Number of points to generate
   * @return List of generated points
   * @throws UnableToComplyException when generation fails
   */
  public abstract List<Vector> generate(int count) throws UnableToComplyException;

  /**
   * Get the density of the given vector
   * 
   * @param p vector
   * @return density
   */
  public abstract double getDensity(Vector p);

  /**
   * Get points.
   * 
   * NOTE: The list may be modified by the caller, it is not immutable.
   * The class should not return a copy, but should allow modification.
   * However when removing points, the called is expected to call setDiscarded.
   */
  public List<Vector> getPoints(); 

  /**
   * Get cluster name
   * @return cluster name
   */
  public String getName();
}