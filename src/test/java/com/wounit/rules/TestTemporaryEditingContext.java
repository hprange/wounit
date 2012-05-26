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

import java.lang.reflect.Field;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.wounit.exceptions.WOUnitException;

import er.extensions.foundation.ERXProperties;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestTemporaryEditingContext extends AbstractEditingContextTest {
    private static final String JAVA_MEMORY_ADAPTOR_BUNDLE_NAME = "JavaMemoryAdaptor";

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
    public void exceptionIfJavaMemoryAdaptorBundleNotFound() throws Exception {
	Field field = NSBundle.class.getDeclaredField("BundlesNamesTable");

	field.setAccessible(true);

	@SuppressWarnings("unchecked")
	NSDictionary<String, NSBundle> bundles = (NSDictionary<String, NSBundle>) field.get(null);

	NSBundle bundleToRestore = bundles.remove(JAVA_MEMORY_ADAPTOR_BUNDLE_NAME);

	thrown.expect(WOUnitException.class);
	thrown.expectMessage("The JavaMemoryAdaptor bundle is not loaded. Are you sure the JavaMemoryAdaptor framework is in the test classpath?");

	try {
	    new TemporaryEditingContext();
	} catch (Exception exception) {
	    bundles.takeValueForKey(bundleToRestore, JAVA_MEMORY_ADAPTOR_BUNDLE_NAME);

	    throw exception;
	}
    }

    @Test
    public void ignoreMissingModelWhileRestoringAdaptorConfiguration() throws Exception {
	AbstractEditingContextRule editingContext = initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	modelGroup.removeModel(modelGroup.modelNamed(TEST_MODEL_NAME));

	editingContext.after();
    }

    @Test
    public void restoreOriginalAdaptorConfigurationAfterTestExecution() throws Throwable {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	AbstractEditingContextRule editingContext = initEditingContext();

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME).adaptorName(), is("JDBC"));
    }

    @Test
    public void useMemoryAdaptorForAllModels() throws Exception {
	new TemporaryEditingContext(TEST_MODEL_NAME);

	String result = EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME).adaptorName();

	assertThat(result, is("Memory"));
    }

    @Test
    public void useMemoryPrototypesForAllModels() throws Exception {
	new TemporaryEditingContext();

	String result = ERXProperties.stringForKey("dbEOPrototypesEntityGLOBAL");

	assertThat(result, is("EOMemoryPrototypes"));
    }
}
