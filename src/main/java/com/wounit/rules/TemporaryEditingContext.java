/**
 * Copyright (C) 2009 hprange <hprange@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.wounit.rules;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.webobjects.eoaccess.EOAdaptorContext;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXProperties;
import er.memoryadaptor.ERMemoryAdaptor;
import er.memoryadaptor.ERMemoryAdaptorContext;

/**
 * The <code>TemporaryEditingContext</code> class provides means to create
 * temporary enterprise object for unit testing. It produces an editing context
 * configured with the memory adaptor settings that is guaranteed to be reset
 * when the test method finishes (whether it passes or fails):
 * 
 * <pre>
 * public class TestMyModel {
 *     &#064;Rule
 *     public TemporaryEditingContext editingContext = new TemporaryEditingContext(&quot;MyModel&quot;);
 * 
 *     &#064;Test
 *     public void testingMyModelLogic() throws Exception {
 * 	MyEntity instance = MyEntity.createMyEntity(editingContext);
 * 	// Do something with instance...
 *     }
 * }
 * </pre>
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class TemporaryEditingContext extends ERXEC implements MethodRule {
    private final Map<String, String> adaptorsToRestore = new HashMap<String, String>();

    private final Collection<String> modelsToClear = new ArrayList<String>();

    /**
     * Creates a <code>TemporaryEditingContext</code> and loads all models with
     * name specified by parameter.
     * 
     * @param modelNames
     *            the name of all models required by unit tests.
     */
    public TemporaryEditingContext(String... modelNames) {
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
     */
    protected void after() {
	ERMemoryAdaptorContext adaptorContext = currentAdaptorContext();

	if (adaptorContext != null) {
	    adaptorContext.resetAllEntities();
	}

	revert();
	unlock();
	dispose();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	for (String modelName : adaptorsToRestore.keySet()) {
	    EOModel model = modelGroup.modelNamed(modelName);

	    model.setAdaptorName(adaptorsToRestore.get(modelName));
	}

	for (String modelName : modelsToClear) {
	    EOModel model = modelGroup.modelNamed(modelName);

	    modelGroup.removeModel(model);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement,
     * org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
	return new Statement() {

	    @Override
	    public void evaluate() throws Throwable {
		before();

		try {
		    base.evaluate();
		} finally {
		    after();
		}
	    }
	};
    }

    /**
     * Set up the temporary editing context in order to execute the test case.
     */
    protected void before() {
	lock();
    }

    ERMemoryAdaptorContext currentAdaptorContext() {
	NSArray<String> modelNames = EOModelGroup.defaultGroup().modelNames();

	if (modelNames.isEmpty()) {
	    return null;
	}

	EODatabaseContext databaseContext = EOUtilities.databaseContextForModelNamed(this, modelNames.objectAtIndex(0));

	EOAdaptorContext adaptorContext = databaseContext.adaptorContext();

	if (!(adaptorContext instanceof ERMemoryAdaptorContext)) {
	    throw new IllegalStateException(String.format("Expected %s, but got %s. Please, use the %s constructor to load all the required models for testing.", ERMemoryAdaptorContext.class.getName(), adaptorContext.getClass().getName(), this.getClass().getSimpleName()));
	}

	return (ERMemoryAdaptorContext) adaptorContext;
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
}
