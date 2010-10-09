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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URL;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.wounit.model.FooEntity;

import er.extensions.foundation.ERXProperties;
import er.memoryadaptor.ERMemoryAdaptorContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTemporaryEditingContext extends AbstractEditingContextTest {
    private static final String TEST_MODEL_NAME = "Test";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @SuppressWarnings("unused")
    @Test
    public void changeAdaptorForModelsNotLoadedByTemporaryEditingContext() throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEditingContext();

	assertThat(model.adaptorName(), is("Memory"));
    }

    @SuppressWarnings("unused")
    @Test
    public void changeAdaptorIfModelAlreadyLoadedWithDifferentAdaptor() throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEditingContext(TEST_MODEL_NAME);

	assertThat(model.adaptorName(), is("Memory"));
    }

    @Test
    public void clearEditingContextChangesAfterTestExecution() throws Exception {
	TemporaryEditingContext editingContext = new TemporaryEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity foo = FooEntity.createFooEntity(editingContext);

	foo.setBar("test");

	editingContext.saveChanges();

	editingContext.after();

	editingContext = new TemporaryEditingContext(TEST_MODEL_NAME);

	NSArray<FooEntity> result = FooEntity.fetchAllFooEntities(editingContext);

	assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void disposeEditingContextAfterTestExecution() throws Throwable {
	TemporaryEditingContext editingContext = Mockito.spy(new TemporaryEditingContext(TEST_MODEL_NAME));

	Mockito.doReturn(Mockito.mock(ERMemoryAdaptorContext.class)).when(editingContext).currentAdaptorContext();

	editingContext.before();

	Mockito.verify(editingContext, Mockito.times(0)).dispose();

	editingContext.after();

	Mockito.verify(editingContext, Mockito.times(1)).dispose();
    }

    @Test
    public void doNotClearTheDatabaseContextIfNoModelsLoaded() throws Throwable {
	TemporaryEditingContext editingContext = new TemporaryEditingContext();

	editingContext.before();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	NSArray<EOModel> models = modelGroup.models();

	for (EOModel model : models) {
	    modelGroup.removeModel(model);
	}

	editingContext.after();
    }

    @Test
    public void doNotRemoveModelsNotLoadedByTheTemporaryEditingContext() throws Throwable {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModelGroup.defaultGroup().addModelWithPathURL(url);

	TemporaryEditingContext editingContext = new TemporaryEditingContext();

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME), notNullValue());
    }

    @Test
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
    @SuppressWarnings("unused")
    public void exceptionIfCannotFindModel() throws Exception {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot load model named 'UnknownModel'"));

	new TemporaryEditingContext("UnknownModel");
    }

    @Test
    @SuppressWarnings("unused")
    public void loadOneModel() throws Exception {
	new TemporaryEditingContext(TEST_MODEL_NAME);

	EOModel result = EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME);

	assertThat(result, notNullValue());
    }

    @Test
    @SuppressWarnings("unused")
    public void loadOneModelInsideResourcesFolder() throws Exception {
	new TemporaryEditingContext("AnotherTest");

	EOModel result = EOModelGroup.defaultGroup().modelNamed("AnotherTest");

	assertThat(result, notNullValue());
    }

    @Test
    public void lockEditingContextBeforeRunningTheTestCase() throws Exception {
	TemporaryEditingContext editingContext = spy(new TemporaryEditingContext());

	verify(editingContext, times(0)).lock();

	editingContext.before();

	verify(editingContext, times(1)).lock();
    }

    @Test
    public void removeModelsLoadedByTheTemporaryEditingContextAfterTestExecution() throws Throwable {
	TemporaryEditingContext editingContext = new TemporaryEditingContext(TEST_MODEL_NAME);

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME), nullValue());
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
    public void revertEditingContextChangesAfterRunningTheTestCases() throws Exception {
	TemporaryEditingContext editingContext = spy(new TemporaryEditingContext());

	editingContext.before();

	verify(editingContext, times(0)).revert();

	editingContext.after();

	verify(editingContext, times(1)).revert();
    }

    @After
    public void tearDown() {
	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	EOModel model = modelGroup.modelNamed(TEST_MODEL_NAME);

	if (model != null) {
	    modelGroup.removeModel(model);
	}
    }

    @Test
    public void unlockEditingContextAfterRunningTheTestCase() throws Exception {
	TemporaryEditingContext editingContext = spy(new TemporaryEditingContext());

	editingContext.before();

	verify(editingContext, times(0)).unlock();

	editingContext.after();

	// The internal disposal logic calls the unlock too
	verify(editingContext, times(2)).unlock();
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
