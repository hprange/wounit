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

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOObjectStore;
import com.wounit.annotations.UnderTest;
import com.wounit.foundation.WOUnitBundleFactory;

import er.extensions.eof.ERXEC;

/**
 * <code>AbstractEditingContextRule</code> is a subclass of <code>ERXEC</code>
 * that implements the {@link MethodRule} interface. This class provides the
 * required infrastructure to properly initialize/dispose the <code>ERXEC</code>
 * before/after the test execution.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public abstract class AbstractEditingContextRule extends ERXEC implements MethodRule {
    /**
     * This facade creates enterprise objects and inserts them into the
     * specified editing context.
     */
    static class UnderTestFacade extends EditingContextFacade {
	public UnderTestFacade(EOEditingContext editingContext) {
	    super(editingContext);
	}

	@Override
	public EOEnterpriseObject create(Class<? extends EOEnterpriseObject> type) {
	    EOEntity entity = EOUtilities.entityForClass(editingContext, type);

	    return EOUtilities.createAndInsertInstance(editingContext, entity.name());
	}

	@Override
	public void insert(EOEnterpriseObject object) {
	    editingContext.insertObject(object);
	}
    }

    private static final long serialVersionUID = 1L;

    /**
     * Collection of models to unload after the test execution.
     */
    private final Collection<String> modelToUnload = new ArrayList<String>();

    /**
     * A processor for fields with special annotations.
     */
    protected AnnotationProcessor processor;

    /**
     * Constructor only for test purposes.
     */
    AbstractEditingContextRule(EOObjectStore objectStore, String... modelNames) {
	super(objectStore);

	System.setProperty("NSProjectBundleEnabled", "true");

	System.setProperty("NSBundleFactories", "(" + WOUnitBundleFactory.class.getName() + ")");

	for (String modelName : modelNames) {
	    loadModel(modelName);
	}
    }

    /**
     * Create a new instance of this editing context loading the models
     * specified by parameter.
     * 
     * @param modelNames
     *            the name of the models to be loaded before the test execution.
     */
    public AbstractEditingContextRule(String... modelNames) {
	this(defaultParentObjectStore(), modelNames);
    }

    /**
     * Reset all changes made into this editing context and unload all models
     * loaded by this class at the beginning of the test execution.
     */
    protected void after() {
	unlock();
	dispose();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	for (String modelName : modelToUnload) {
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
    public final Statement apply(final Statement base, FrameworkMethod method, final Object target) {
	processor = new AnnotationProcessor(target);

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
     * Set up this editing context in order to execute the test case.
     * <p>
     * Create and insert objects for fields annotated with @UnderTest before the
     * test execution.
     */
    protected void before() {
	lock();

	processor.process(UnderTest.class, new UnderTestFacade(this));
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

	modelToUnload.add(modelName);
    }

}
