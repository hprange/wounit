package com.wounit.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.net.URL;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.wounit.model.DifferentClassNameForEntity;
import com.wounit.model.FooEntity;
import com.wounit.model.StubEnttiy;

import er.extensions.foundation.ERXProperties;
import er.memoryadaptor.ERMemoryAdaptorContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTemporaryEnterpriseObjectProvider {
    private static final String TEST_MODEL_NAME = "Test";

    @Mock
    private EOEditingContext mockEditingContext;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void cannotCreateEditingContextAfterTestExecution() throws Throwable {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	provider.before();
	provider.after();

	thrown.expect(IllegalStateException.class);
	thrown.expectMessage(is("You cannot obtain an editing context instance after the TemporaryEnterpriseObjectProvider disposal"));

	provider.temporaryEditingContext();
    }

    @Test
    public void cannotCreateInstanceForClassThatCannotBeRecognized()
	    throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance based on the provided class. Please, provide an entity name instead."));

	provider.createInstance(StubEnttiy.class);
    }

    @Test
    public void cannotCreateInstanceForInvalidEntityName() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Could not find EOClassDescription for entity name 'InvalidEntityName' !"));

	provider.createInstance("InvalidEntityName");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void cannotCreateInstanceForNullClass() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null class."));

	provider.createInstance((Class) null);
    }

    @Test
    public void cannotCreateInstanceForNullEntityName() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null entity name."));

	provider.createInstance((String) null);
    }

    @Test
    public void cannotDeleteNullInstance() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot delete a null instance. Please, provide a valid enterprise object."));

	provider.deleteInstance(null);
    }

    @Test
    public void changeAdaptorForModelsNotLoadedByTemporaryEnterpriseObjectProvider()
	    throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEnterpriseObjectProvider();

	assertThat(model.adaptorName(), is("Memory"));
    }

    @Test
    public void changeAdaptorIfModelAlreadyLoadedWithDifferentAdaptor()
	    throws Exception {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME);

	assertThat(model.adaptorName(), is("Memory"));
    }

    @Test
    public void clearEditingContextChangesAfterTestExecution() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	EOEditingContext editingContext = provider.temporaryEditingContext();

	FooEntity foo = FooEntity.createFooEntity(editingContext);

	foo.setBar("test");

	editingContext.saveChanges();

	provider.after();

	provider = new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME);

	NSArray<FooEntity> result = FooEntity.fetchAllFooEntities(provider
		.temporaryEditingContext());

	assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void createInstanceForClassWithAnotherEntityNameContainingEntityNameProperty()
	    throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	DifferentClassNameForEntity result = provider
		.createInstance(DifferentClassNameForEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(),
		is(provider.temporaryEditingContext()));
    }

    @Test
    public void createInstanceForEntityNamed() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	FooEntity result = provider.createInstance(FooEntity.ENTITY_NAME);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(),
		is(provider.temporaryEditingContext()));
    }

    @Test
    public void createInstanceForExistingClass() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	FooEntity result = provider.createInstance(FooEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(),
		is(provider.temporaryEditingContext()));
    }

    @Test
    public void deleteInstance() throws Exception {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	FooEntity instance = provider.createInstance(FooEntity.class);

	provider.deleteInstance(instance);

	assertThat(provider.temporaryEditingContext().deletedObjects(),
		hasItem((EOEnterpriseObject) instance));
    }

    @Test
    public void disposeEditingContextAfterTestExecution() throws Throwable {
	TemporaryEnterpriseObjectProvider provider = Mockito
		.spy(new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME));

	Mockito.doReturn(mockEditingContext).when(provider)
		.createEditingContext();
	Mockito.doReturn(Mockito.mock(ERMemoryAdaptorContext.class))
		.when(provider).currentAdaptorContext();

	provider.before();

	Mockito.verify(mockEditingContext, Mockito.times(0)).dispose();

	provider.after();

	Mockito.verify(mockEditingContext, Mockito.times(1)).dispose();
    }

    @Test
    public void doNotClearTheDatabaseContextIfNoModelsLoaded() throws Throwable {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider();

	provider.before();

	EOModelGroup modelGroup = EOModelGroup.defaultGroup();

	NSArray<EOModel> models = modelGroup.models();

	for (EOModel model : models) {
	    modelGroup.removeModel(model);
	}

	provider.after();
    }

    @Test
    public void doNotRemoveModelsLoadedOutsideOfTheProvider() throws Throwable {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModelGroup.defaultGroup().addModelWithPathURL(url);

	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider();

	provider.before();
	provider.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME),
		notNullValue());
    }

    @Test
    public void exceptionIfAdaptorContextIsNotMemoryAdaptor() throws Throwable {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	NSArray<EOModel> models = EOModelGroup.defaultGroup().models();

	for (EOModel model : models) {
	    model.setAdaptorName("JDBC");
	}

	provider.before();

	thrown.expect(IllegalStateException.class);
	thrown.expectMessage(is("Expected er.memoryadaptor.ERMemoryAdaptorContext, but got com.webobjects.jdbcadaptor.JDBCContext. Please, use the TemporaryEnterpriseObjectProvider constructor to load all the required models for testing."));

	provider.after();
    }

    @Test
    public void exceptionIfCannotFindModel() throws Exception {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot load model named 'UnknownModel'"));

	new TemporaryEnterpriseObjectProvider("UnknownModel");
    }

    @Test
    public void initializeEditingContextOnce() throws Exception {
	TemporaryEnterpriseObjectProvider provider = Mockito
		.spy(new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME));

	Mockito.doReturn(mockEditingContext).when(provider)
		.createEditingContext();

	provider.temporaryEditingContext();
	provider.temporaryEditingContext();

	Mockito.verify(provider, Mockito.times(1)).createEditingContext();
	Mockito.verify(mockEditingContext, Mockito.times(1)).lock();
    }

    @Test
    public void loadOneModel() throws Exception {
	new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME);

	EOModel result = EOModelGroup.defaultGroup()
		.modelNamed(TEST_MODEL_NAME);

	assertThat(result, notNullValue());
    }

    @Test
    public void loadOneModelInsideResourcesFolder() throws Exception {
	new TemporaryEnterpriseObjectProvider("AnotherTest");

	EOModel result = EOModelGroup.defaultGroup().modelNamed("AnotherTest");

	assertThat(result, notNullValue());
    }

    @Test
    public void removeModelsLoadedByTheProviderAfterTestExecution()
	    throws Throwable {
	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider(
		TEST_MODEL_NAME);

	provider.before();
	provider.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME),
		nullValue());
    }

    @Test
    public void restoreOriginalAdaptorConfigurationAfterTestExecution()
	    throws Throwable {
	URL url = getClass().getResource("/" + TEST_MODEL_NAME + ".eomodeld");

	EOModel model = EOModelGroup.defaultGroup().addModelWithPathURL(url);

	model.setAdaptorName("JDBC");

	TemporaryEnterpriseObjectProvider provider = new TemporaryEnterpriseObjectProvider();

	provider.before();
	provider.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME)
		.adaptorName(), is("JDBC"));
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
    public void useMemoryAdaptorForAllModels() throws Exception {
	new TemporaryEnterpriseObjectProvider(TEST_MODEL_NAME);

	String result = EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME)
		.adaptorName();

	assertThat(result, is("Memory"));
    }

    @Test
    public void useMemoryPrototypesForAllModels() throws Exception {
	new TemporaryEnterpriseObjectProvider();

	String result = ERXProperties
		.stringForKey("dbEOPrototypesEntityGLOBAL");

	assertThat(result, is("EOMemoryPrototypes"));
    }
}
