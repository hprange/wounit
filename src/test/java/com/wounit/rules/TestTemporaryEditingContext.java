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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXProperties;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestTemporaryEditingContext extends AbstractEditingContextTest {
    @Test
    @SuppressWarnings("unused")
    public void changeAdaptorForModelsNotLoadedByTemporaryEditingContext() throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEditingContext();

	assertThat(model.adaptorName(), is("Memory"));
    }

    @Test
    @SuppressWarnings("unused")
    public void changeAdaptorIfModelAlreadyLoadedWithDifferentAdaptor() throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEditingContext(TEST_MODEL_NAME);

	assertThat(model.adaptorName(), is("Memory"));
    }

    @Override
    protected TemporaryEditingContext createEditingContext(String... modelNames) {
	return new TemporaryEditingContext(modelNames);
    }

    @Test
    @Ignore("Don't know how to clean the mess in the adaptor context after the test")
    public void doNotClearTheDatabaseContextIfNoModelsLoaded() throws Throwable {
	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	NSArray<EOModel> models = modelGroup.models();

	EOModel model = null;

	for (int i = 0; i < models.size(); i++) {
	    model = models.get(i);

	    modelGroup.removeModel(model);
	}

	TemporaryEditingContext editingContext = new TemporaryEditingContext();

	editingContext.before();
	editingContext.after();
    }

    @Test
    @Ignore("Don't know how to clean the mess in the adaptor context after the test")
    public void exceptionIfAdaptorContextIsNotMemoryAdaptor() throws Throwable {
	TemporaryEditingContext editingContext = new TemporaryEditingContext(TEST_MODEL_NAME);

	NSArray<EOModel> models = EOModelGroup.defaultGroup().models();

	for (EOModel model : models) {
	    model.setAdaptorName("JDBC");
	}

	editingContext.before();

	thrown.expect(IllegalStateException.class);
	thrown.expectMessage(is("Expected er.memoryadaptor.ERMemoryAdaptorContext, but got com.webobjects.jdbcadaptor.JDBCContext. Please, use the TemporaryEditingContext constructor to load all the required models for testing."));

	editingContext.after();
    }

    @Test
    public void restoreOriginalAdaptorConfigurationAfterTestExecution() throws Throwable {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	TemporaryEditingContext editingContext = new TemporaryEditingContext();

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME).adaptorName(), is("JDBC"));
    }

    @Test
    @SuppressWarnings("unused")
    public void useMemoryAdaptorForAllModels() throws Exception {
	new TemporaryEditingContext(TEST_MODEL_NAME);

	String result = EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME).adaptorName();

	assertThat(result, is("Memory"));
    }

    @Test
    @SuppressWarnings("unused")
    public void useMemoryPrototypesForAllModels() throws Exception {
	new TemporaryEditingContext();

	String result = ERXProperties.stringForKey("dbEOPrototypesEntityGLOBAL");

	assertThat(result, is("EOMemoryPrototypes"));
    }
}
