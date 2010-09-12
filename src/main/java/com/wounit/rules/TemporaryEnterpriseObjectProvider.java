package com.wounit.rules;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import com.webobjects.eoaccess.EOAdaptorContext;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXProperties;
import er.memoryadaptor.ERMemoryAdaptor;
import er.memoryadaptor.ERMemoryAdaptorContext;

/**
 * The TemporaryEnterpriseObjectProvider Rule provide means to create temporary
 * enterprise object for unit testing. It uses an editing context configured
 * with the memory adaptor that is guaranteed to be reset when the test method
 * finishes (whether it passes or fails):
 * 
 * <pre>
 * public class TestMyModel {
 *     &#064;Rule
 *     public TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider();
 * 
 *     &#064;Test
 *     public void testingMyModelLogic() throws Exception {
 * 	MyEntity instance = provider.createInstance(&quot;MyEntity&quot;);
 * 	// Do something with instance...
 * 	EOEditingContext editingContext = provider.temporaryEditingContext();
 * 	// Do something with editingContext...
 *     }
 * }
 * </pre>
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class TemporaryEnterpriseObjectProvider extends ExternalResource {
    private final Map<String, String> adaptorsToRestore = new HashMap<String, String>();

    private EOEditingContext editingContext;

    private boolean finished = false;

    private final Collection<String> modelsToClear = new ArrayList<String>();

    /**
     * Creates a TemporaryEnterpriseObjectProvider rule.
     * 
     * @param modelNames
     *            the name of all models required by unit tests.
     */
    public TemporaryEnterpriseObjectProvider(String... modelNames) {
	fixJavaMemoryDictionary();

	// Use Memory prototypes for tests. We don't want to set this
	// information in the EOModel dictionary
	ERXProperties.setStringForKey("EOMemoryPrototypes", "dbEOPrototypesEntityGLOBAL");

	for (String modelName : modelNames) {
	    loadModel(modelName);
	}

	NSArray<EOModel> models = EOModelGroup.defaultGroup().models();

	for (EOModel model : models) {
	    if (!"Memory".equals(model.adaptorName())) {
		adaptorsToRestore.put(model.name(), model.adaptorName());

		// Use Memory adaptor for tests. We don't want to set this
		// information in the EOModel dictionary
		model.setAdaptorName("Memory");
	    }
	}
    }

    /**
     * Reset all changes made into the temporary editing context.
     * 
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected void after() {
	ERMemoryAdaptorContext adaptorContext = currentAdaptorContext();

	if (adaptorContext != null) {
	    adaptorContext.resetAllEntities();
	}

	editingContext.revert();
	editingContext.unlock();
	editingContext.dispose();
	editingContext = null;

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	for (String modelName : adaptorsToRestore.keySet()) {
	    EOModel model = modelGroup.modelNamed(modelName);

	    model.setAdaptorName(adaptorsToRestore.get(modelName));
	}

	for (String modelName : modelsToClear) {
	    EOModel model = modelGroup.modelNamed(modelName);

	    modelGroup.removeModel(model);
	}

	finished = true;

	super.after();
    }

    /**
     * Initialize the temporary editing context provided for unit testing.
     * 
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected void before() throws Throwable {
	super.before();

	temporaryEditingContext();
    }

    EOEditingContext createEditingContext() {
	return ERXEC.newEditingContext();
    }

    /**
     * Create an instance of the specified class and insert into the temporary
     * editing context.
     * 
     * @param <T>
     *            the static type of the instance that should be instantiated
     * @param clazz
     *            the class of the entity that should be instantiated
     * @return an instance of the given class
     */
    public <T extends EOEnterpriseObject> T createInstance(Class<T> clazz) {
	if (clazz == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null class.");
	}

	try {
	    return ERXEOControlUtilities.createAndInsertObject(temporaryEditingContext(), clazz);
	} catch (Exception exception) {
	    // Ops. The entity name cannot be obtained based on the class name.
	}

	String entityName = null;

	try {
	    Field field = clazz.getField("ENTITY_NAME");

	    entityName = (String) field.get(null);
	} catch (Exception exception) {
	    throw new IllegalArgumentException("Cannot create an instance based on the provided class. Please, provide an entity name instead.", exception);
	}

	@SuppressWarnings("unchecked")
	T instance = (T) createInstance(entityName);

	return instance;
    }

    /**
     * Create an instance of the specified entity named and insert into the
     * temporary editing context.
     * 
     * @param <T>
     *            the static type of the enterprise object returned by this
     *            method
     * @param entityName
     *            the name of the entity that should be instantiated
     * @return an instance of the given entity named
     */
    @SuppressWarnings("unchecked")
    public <T extends EOEnterpriseObject> T createInstance(String entityName) {
	if (entityName == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null entity name.");
	}

	return (T) EOUtilities.createAndInsertInstance(temporaryEditingContext(), entityName);
    }

    ERMemoryAdaptorContext currentAdaptorContext() {
	NSArray<String> modelNames = EOModelGroup.defaultGroup().modelNames();

	if (modelNames.isEmpty()) {
	    return null;
	}

	EODatabaseContext databaseContext = EOUtilities.databaseContextForModelNamed(editingContext, modelNames.objectAtIndex(0));

	EOAdaptorContext adaptorContext = databaseContext.adaptorContext();

	if (!(adaptorContext instanceof ERMemoryAdaptorContext)) {
	    throw new IllegalStateException(String.format("Expected %s, but got %s. Please, use the %s constructor to load all the required models for testing.", ERMemoryAdaptorContext.class.getName(), adaptorContext.getClass().getName(), this.getClass().getSimpleName()));
	}

	return (ERMemoryAdaptorContext) adaptorContext;
    }

    /**
     * @param object
     */
    public void deleteInstance(EOEnterpriseObject object) {
	if (object == null) {
	    throw new IllegalArgumentException("Cannot delete a null instance. Please, provide a valid enterprise object.");
	}

	temporaryEditingContext().deleteObject(object);
    }

    /**
     * Maven build produces an incomplete Info.plist dictionary for
     * JavaMemoryAdaptor framework. Fixing it here temporarily.
     */
    private void fixJavaMemoryDictionary() {
	NSBundle bundle = NSBundle.bundleForName("JavaMemoryAdaptor");

	bundle._infoDictionary().takeValueForKey(ERMemoryAdaptor.class.getName(), "EOAdaptorClassName");
    }

    /**
     * Load the model with the specified name into the default model group.
     * 
     * @param modelName
     *            name of the model to be loaded
     * @throws IllegalArgumentException
     *             if no model can be found with the specified name
     * @see EOModelGroup#defaultGroup();
     */
    protected void loadModel(String modelName) {
	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	EOModel model = modelGroup.modelNamed(modelName);

	if (model != null) {
	    return;
	}

	URL url = getClass().getResource("/Resources/" + modelName + ".eomodeld");

	if (url == null) {
	    url = getClass().getResource("/" + modelName + ".eomodeld");
	}

	if (url == null) {
	    throw new IllegalArgumentException(String.format("Cannot load model named '%s'", modelName));
	}

	modelGroup.addModelWithPathURL(url);

	modelsToClear.add(modelName);
    }

    /**
     * Get the temporary editing context to be used for unit testing.
     * 
     * @return an <code>EOEditingContext</code> that save changes in memory.
     */
    public EOEditingContext temporaryEditingContext() {
	if (finished) {
	    throw new IllegalStateException(String.format("You cannot obtain an editing context instance after the %s disposal", this.getClass().getSimpleName()));
	}

	if (editingContext == null) {
	    editingContext = createEditingContext();

	    editingContext.lock();
	}

	return editingContext;
    }
}
