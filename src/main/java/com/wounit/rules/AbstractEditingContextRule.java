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

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOObjectStore;

import er.extensions.eof.ERXEC;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public abstract class AbstractEditingContextRule extends ERXEC implements MethodRule {

    private final Collection<String> modelsToClear = new ArrayList<String>();

    public AbstractEditingContextRule(EOObjectStore objectStore, String... modelNames) {
	super(objectStore);

	for (String modelName : modelNames) {
	    loadModel(modelName);
	}
    }

    public AbstractEditingContextRule(String... modelNames) {
	this(defaultParentObjectStore(), modelNames);
    }

    protected void after() {
	revert();
	unlock();
	dispose();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

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
     * Set up the editing context in order to execute the test case.
     */
    protected void before() {
	lock();
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
