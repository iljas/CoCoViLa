package ee.ioc.cs.vsle.editor;

import ee.ioc.cs.vsle.vclass.*;

/**
 * @author pavelg
 * 
 * Interface used by ProgramRunner
 */
interface ISchemeContainer {

    /**
     * Returns a loaded package
     * 
     * @return
     */
    public VPackage getPackage();
    
    /**
     * Registers runner id
     * 
     * @param id
     */
    public void registerRunner( long id );
    
    /**
     * Unregisters runner id
     * 
     * @param id
     */
    public void unregisterRunner( long id );
    
    /**
     * Returns working folder of a package
     * 
     * @return
     */
    String getWorkDir();
    
    /**
     * Returns a list of objects
     * 
     * @return
     */
    ObjectList getObjects();

    /**
     * Returns a scheme
     * 
     * @return
     */
    Scheme getScheme();
    
    /**
     * Calls repaint
     */
    public void repaint();
}
