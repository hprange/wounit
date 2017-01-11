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

import static er.extensions.eof.ERXEOControlUtilities.primaryKeyObjectForObject;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.wounit.model.DifferentClassNameForEntity;
import com.wounit.model.FooEntity;
import com.wounit.model.FooEntityWithRequiredField;
import com.wounit.model.LongPKEntity;
import com.wounit.model.StubEntity;
import com.wounit.model.SubFooEntity;
import com.wounit.stubs.StubTestCase;

import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXS;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestMockEditingContext extends AbstractEditingContextTest {
    @Mock
    private EOFetchSpecification mockFetchSpecification;

    @Test
    public void callAwakeFromInsertionOnMockInstance() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createSavedObject(FooEntity.class);

	assertThat(entity.awakeFromInsertionCount(), is(1));
    }

    @Test
    public void callAwakeFromInsertionOnMockInstanceInsertionWithTemporaryGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = new FooEntity();

	editingContext.insertSavedObject(entity);

	assertThat(entity.globalIdDuringAwakeFromInsertion().isTemporary(), is(true));
    }

    @Test
    public void callAwakeFromInsertionOnMockInstanceWithTemporaryGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createSavedObject(FooEntity.class);

	assertThat(entity.globalIdDuringAwakeFromInsertion().isTemporary(), is(true));
    }

    @Test
    public void cannotCreateMockInstanceForClassThatCannotBeRecognized() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance based on the provided class. Please, provide an entity name instead."));

	editingContext.createSavedObject(StubEntity.class);
    }

    @Test
    public void cannotCreateMockInstanceForInvalidEntityName() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Could not find EOClassDescription for entity name 'InvalidEntityName'."));

	editingContext.createSavedObject("InvalidEntityName");
    }

    @Test
    public void cannotCreateMockInstanceForNullClass() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null class."));

	editingContext.createSavedObject((Class<EOEnterpriseObject>) null);
    }

    @Test
    public void cannotCreateMockInstanceForNullEntityName() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null entity name."));

	editingContext.createSavedObject((String) null);
    }

    @Test
    public void clearIgnoredObjectsArrayAfterTestExecution() throws Exception {
	MockEditingContext editingContext = (MockEditingContext) initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity entity = new FooEntity();

	editingContext.insertSavedObject(entity);

	editingContext.after();

	assertThat(editingContext.ignoredObjects.isEmpty(), is(true));
    }

    @Override
    protected AbstractEditingContextRule createEditingContext(String... modelNames) {
	return new MockEditingContext(modelNames);
    }

    @Test
    public void createMockEditingContextWithoutLoadingModels() throws Exception {
	MockEditingContext editingContext = new MockEditingContext();

	assertThat(editingContext, notNullValue());
    }

    @Test
    public void createMockInstanceForClassWithAnotherEntityNameContainingEntityNameProperty() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	DifferentClassNameForEntity result = editingContext.createSavedObject(DifferentClassNameForEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void createMockInstanceForEntityNamed() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity result = editingContext.createSavedObject(FooEntity.ENTITY_NAME);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void createMockInstanceForExistingClass() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity result = editingContext.createSavedObject(FooEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void createSavedObjectForFieldAnnotatedAsDummy() throws Throwable {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	StubTestCase stubTestCase = new StubTestCase();

	editingContext.processor = new AnnotationProcessor(stubTestCase);

	editingContext.before();

	EOEnterpriseObject savedObject = stubTestCase.foo();

	assertThat(savedObject, notNullValue());
	assertThat(editingContext.ignoredObjects, hasItem(savedObject));
    }

    @Test
    public void doNotCallObjectWillChangeOnIgnoredObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntityWithRequiredField mockObject = new FooEntityWithRequiredField();

	editingContext.insertSavedObject(mockObject);

	mockObject.setRequired(null);

	editingContext.createSavedObject(FooEntityWithRequiredField.class);

	try {
	    editingContext.saveChanges();
	} catch (Throwable exception) {
	    fail("The mock object required field should be ignored");
	}
    }

    @Test
    public void doNotFetchObjectsFromDatabase() throws Exception {
	EOObjectStoreCoordinator mockCoordinator = mock(EOObjectStoreCoordinator.class);

	MockEditingContext editingContext = new MockEditingContext(mockCoordinator, TEST_MODEL_NAME);

	editingContext.objectsWithFetchSpecification(mockFetchSpecification, editingContext);

	verify(mockCoordinator, never()).objectsWithFetchSpecification(mockFetchSpecification, editingContext);
    }

    @Test
    public void doNotTryToForgetDeletedObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.deleteObject(mockFoo);

	editingContext = spy(editingContext);

	editingContext.saveChanges();

	verify(editingContext, never()).forgetObject(mockFoo);
    }

    @Test
    public void doNotTryToForgetObjectDeletedByCascade() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	FooEntityWithRequiredField mockFoo2 = FooEntityWithRequiredField.createFooEntityWithRequiredField(editingContext, 1);

	mockFoo2.setFooEntityRelationship(mockFoo);

	editingContext.deleteObject(mockFoo2);

	editingContext = spy(editingContext);

	editingContext.saveChanges();

	verify(editingContext, never()).forgetObject(mockFoo);
    }

    @Test
    public void incrementPermanentGlobalIdForEachMockInstance() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	for (int i = 0; i < 10; i++) {
	    FooEntity entity = editingContext.createSavedObject(FooEntity.class);

	    EOKeyGlobalID result = (EOKeyGlobalID) editingContext.globalIDForObject(entity);

	    assertThat((Integer) result.keyValues()[0], is(i + 1));
	}
    }

    @Test
    public void insertMockInstanceCallAwakeFromInsertion() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockInstance = spy(new FooEntity());

	editingContext.insertSavedObject(mockInstance);

	verify(mockInstance, times(1)).awakeFromInsertion(editingContext);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void insertMockInstanceForObject() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockInstance = new FooEntity();

	editingContext.insertSavedObject(mockInstance);

	NSArray<FooEntity> result = editingContext.registeredObjects();

	assertThat(result, hasItem(mockInstance));
    }

    @Test
    public void mockEditingContextHasParentMockObjectStoreCoordinator() throws Exception {
	MockEditingContext editingContext = new MockEditingContext();

	assertThat(editingContext.parentObjectStore(), instanceOf(MockObjectStoreCoordinator.class));
    }

    @Test
    public void mockInstanceHasPermanentGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createSavedObject(FooEntity.class);

	EOGlobalID result = editingContext.globalIDForObject(entity);

	assertThat(result.isTemporary(), is(false));
    }

    @Test
    public void objectsWithFetchSpecificationDoesNotReturnDeletedObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.deleteObject(mockFoo);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void objectsWithFetchSpecificationDoNotReturnSubEntityObjectsIfNotIsDeep() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity.createFooEntity(editingContext);
	SubFooEntity.createSubFooEntity(editingContext);

	when(mockFetchSpecification.isDeep()).thenReturn(false);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
    }

    @Test
    public void objectsWithFetchSpecificationFilterObjectsByEntityName() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	DifferentClassNameForEntity.createEntityWithDifferentClassName(editingContext);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result, hasItem(mockFoo));
    }

    @Test
    public void objectsWithFetchSpecificationFilterObjectsByQualifier() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	mockFoo.setBar("wrong");

	mockFoo = FooEntity.createFooEntity(editingContext);

	mockFoo.setBar("correct");

	EOQualifier qualifier = ERXQ.is(FooEntity.BAR_KEY, "correct");

	when(mockFetchSpecification.qualifier()).thenReturn(qualifier);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result, hasItem(mockFoo));
    }

    @Test
    public void objectsWithFetchSpecificationRespectsFetchLimit() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	for (int i = 0; i < 10; i++) {
	    FooEntity.createFooEntity(editingContext);
	}

	when(mockFetchSpecification.fetchLimit()).thenReturn(5);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(5));
    }

    @Test
    public void objectsWithFetchSpecificationReturnsAllObjectsIfFetchingLimitGreaterThanNumberOfObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity.createFooEntity(editingContext);

	when(mockFetchSpecification.fetchLimit()).thenReturn(2);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
    }

    @Test
    public void objectsWithFetchSpecificationReturnsInsertedObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result, hasItem(mockFoo));
    }

    @Test
    public void objectsWithFetchSpecificationReturnsMockObjects() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockFoo = editingContext.createSavedObject(FooEntity.class);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result, hasItem(mockFoo));
    }

    @Test
    public void objectsWithFetchSpecificationReturnSubEntityObjectsIfIsDeep() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity.createFooEntity(editingContext);
	SubFooEntity.createSubFooEntity(editingContext);

	when(mockFetchSpecification.isDeep()).thenReturn(true);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	assertThat(result.size(), is(2));
    }

    @Test
    public void objectsWithFetchSpecificationSortObjectsBySortingOrder() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	for (int i = 9; i > 0; i--) {
	    FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	    mockFoo.setBar(Integer.toString(i));
	}

	when(mockFetchSpecification.sortOrderings()).thenReturn(ERXS.ascs(FooEntity.BAR_KEY));

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(mockFetchSpecification);

	for (int i = 0; i < 9; i++) {
	    FooEntity foo = result.get(i);

	    assertThat(foo.bar(), is(Integer.toString(i + 1)));
	}
    }

    @Test
    public void recordMockInstanceForFutureFetching() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockEntity = editingContext.createSavedObject(FooEntity.class);

	EOFetchSpecification fetchSpecification = new EOFetchSpecification(FooEntity.ENTITY_NAME, null, null);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(fetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result.get(0), is(mockEntity));
    }

    @Test
    public void removeModelsLoadedAfterTestExecution() throws Throwable {
	AbstractEditingContextRule editingContext = initEditingContext(TEST_MODEL_NAME);

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME), nullValue());
    }

    @Test
    public void savedObjectHasNonTemporaryGlobalId() throws Exception {
	AbstractEditingContextRule editingContext = initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	assertThat(mockFoo.__globalID().isTemporary(), is(false));

	editingContext.after();
    }

    @Test
    public void savedObjectIncrementsGlobalFakeId() throws Exception {
	MockEditingContext editingContext = (MockEditingContext) initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	editingContext.createSavedObject(FooEntity.class);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	assertThat((Integer) ((EOKeyGlobalID) mockFoo.__globalID()).keyValues()[0], is(2));

	editingContext.after();
    }

    @Test
    public void savedObjectUsesGlobalFakeId() throws Exception {
	MockEditingContext editingContext = (MockEditingContext) initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	FooEntity mockFoo = editingContext.createSavedObject(FooEntity.class);

	assertThat((Integer) ((EOKeyGlobalID) mockFoo.__globalID()).keyValues()[0], is(2));

	editingContext.after();
    }

    @Test
    public void savedObjectUsesGlobalFakeIdInCaseOfALongPK() throws Exception {
	MockEditingContext editingContext = (MockEditingContext) initEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	LongPKEntity.createLongPKEntity(editingContext);

	editingContext.saveChanges();

	LongPKEntity mockLongPKEntity = editingContext.createSavedObject(LongPKEntity.class);

	assertThat(primaryKeyObjectForObject(mockLongPKEntity), instanceOf(Long.class));
	assertThat(primaryKeyObjectForObject(mockLongPKEntity), is((Object) 2L));

	editingContext.after();
    }

    @Before
    public void setup() {
	when(mockFetchSpecification.entityName()).thenReturn(FooEntity.ENTITY_NAME);
    }
}
