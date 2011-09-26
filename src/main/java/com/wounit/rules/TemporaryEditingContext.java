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

import java.util.HashMap;
import java.util.Map;

import com.webobjects.eoaccess.EOAdaptorContext;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.wounit.exceptions.WOUnitException;

import er.extensions.foundation.ERXProperties;
import er.memoryadaptor.ERMemoryAdaptor;
import er.memoryadaptor.ERMemoryAdaptorContext;

/**
 * The <code>TemporaryEditingContext</code> class provides means to create
 * temporary enterprise object for unit testing. It is an editing context
 * configured with the memory adaptor settings that is guaranteed to be reset
 * when the test method finishes (whether it passes or fails).
 * <p>
 * This class is useful for integration testing because it mimics the internal
 * behavior of the <code>ERXEC</code>. Every <code>EOEnterpriseObject</code>
 * inserted in the <code>TemporaryEditingContext</code> behaves as a real EO.
 * This kind of feature is useful to validate the behavior of a group of EOs.
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
public class TemporaryEditingContext extends AbstractEditingContextRule {
    private static final long serialVersionUID = 1L;

    /**
     * Map of original adaptor name for each loaded <code>EOModel</code> to be
     * restored at the end of the test execution.
     */
    private final Map<String, String> adaptorsToRestore = new HashMap<String, String>();

    /**
     * Creates a <code>TemporaryEditingContext</code> and loads all models with
     * name specified by parameter.
     * 
     * @param modelNames
     *            the name of all models required by unit tests.
     */
    public TemporaryEditingContext(String... modelNames) {
	super(modelNames);

	fixJavaMemoryDictionary();

	// Use Memory prototypes for tests. We don't want to set this
	// information in the EOModel dictionary
	ERXProperties.setStringForKey("EOMemoryPrototypes", "dbEOPrototypesEntityGLOBAL");

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
     * Reset all changes made into this temporary editing context, restoring the
     * original adaptor name of each loaded <code>EOModel</code>.
     */
    @Override
    protected void after() {
	ERMemoryAdaptorContext adaptorContext = currentAdaptorContext();

	if (adaptorContext != null) {
	    adaptorContext.resetAllEntities();
	}

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	for (String modelName : adaptorsToRestore.keySet()) {
	    EOModel model = modelGroup.modelNamed(modelName);

	    if (model == null) {
		System.out.println("[WARN] Cannot restore the adaptor configuration for model named " + modelName);

		continue;
	    }

	    model.setAdaptorName(adaptorsToRestore.get(modelName));
	}

	super.after();
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

	if (bundle == null) {
	    throw new WOUnitException("The JavaMemoryAdaptor bundle is not loaded. Are you sure the JavaMemoryAdaptor framework is in the test classpath?");
	}

	bundle._infoDictionary().takeValueForKey(ERMemoryAdaptor.class.getName(), "EOAdaptorClassName");
    }
}
