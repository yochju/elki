package de.lmu.ifi.dbs.data;

import de.lmu.ifi.dbs.logging.AbstractLoggable;
import de.lmu.ifi.dbs.logging.LoggingConfiguration;

/**
 * Abstract super class for all database objects. Provides the required access
 * methods for the unique object id.
 * 
 * @author Elke Achtert 
 */
public abstract class AbstractDatabaseObject extends AbstractLoggable implements DatabaseObject
{
    /**
     * The unique id of this object.
     */
    private Integer id;

  /**
   * Initializes the logger and sets the debug status to false.
   *
   */
  protected AbstractDatabaseObject() {
    super(LoggingConfiguration.DEBUG);
  }

  /**
     * @see DatabaseObject#getID()
     */
    public final Integer getID()
    {
        return id;
    }

    /**
     * @see DatabaseObject#setID(Integer)
     */
    public void setID(Integer id)
    {
        this.id = id;
    }

}
