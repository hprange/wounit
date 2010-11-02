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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.foundation.NSArray;
import com.wounit.model.CompoundKeyEntity;
import com.wounit.model.DifferentClassNameForEntity;
import com.wounit.model.FooEntity;
import com.wounit.model.StubEntity;

/**
 * TODO: objectsWithFetchSpecification respects qualifier
 * <p>
 * TODO: objectsWithFetchSpecification respects sort ordering
 * <p>
 * TODO: objectsWithFetchSpecification respects isDeep
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestMockEditingContext extends AbstractEditingContextTest {
    @Test
    public void callAwakeFromInsertionOnMockInstance() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createMock(FooEntity.class);

	assertThat(entity.awakeFromInsertionCount(), is(1));
    }

    @Test
    public void callAwakeFromInsertionOnMockInstanceInsertionWithTemporaryGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = new FooEntity();

	editingContext.insertMock(entity);

	assertThat(entity.globalIdDuringAwakeFromInsertion().isTemporary(), is(true));
    }

    @Test
    public void callAwakeFromInsertionOnMockInstanceWithTemporaryGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createMock(FooEntity.class);

	assertThat(entity.globalIdDuringAwakeFromInsertion().isTemporary(), is(true));
    }

    @Test
    public void cannotCreateMockInstanceForClassThatCannotBeRecognized() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance based on the provided class. Please, provide an entity name instead."));

	editingContext.createMock(StubEntity.class);
    }

    @Test
    public void cannotCreateMockInstanceForInvalidEntityName() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Could not find EOClassDescription for entity name 'InvalidEntityName'."));

	editingContext.createMock("InvalidEntityName");
    }

    @Test
    public void cannotCreateMockInstanceForNullClass() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null class."));

	editingContext.createMock((Class<EOEnterpriseObject>) null);
    }

    @Test
    public void cannotCreateMockInstanceForNullEntityName() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("Cannot create an instance for a null entity name."));

	editingContext.createMock((String) null);
    }

    @Test
    public void cannotMockInstaceOfEntityWithCompoundPrimaryKey() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage(is("CompoundKeyEntity has a compound primary key and can't be used to create mock instances."));

	editingContext.createMock(CompoundKeyEntity.class);
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

	DifferentClassNameForEntity result = editingContext.createMock(DifferentClassNameForEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void createMockInstanceForEntityNamed() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity result = editingContext.createMock(FooEntity.ENTITY_NAME);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void createMockInstanceForExistingClass() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity result = editingContext.createMock(FooEntity.class);

	assertThat(result, notNullValue());
	assertThat(result.editingContext(), is((EOEditingContext) editingContext));
    }

    @Test
    public void doNotFetchObjectsFromDatabase() throws Exception {
	EOObjectStoreCoordinator mockCoordinator = mock(EOObjectStoreCoordinator.class);

	MockEditingContext editingContext = new MockEditingContext(mockCoordinator, TEST_MODEL_NAME);

	EOFetchSpecification mockFetchSpecification = null;

	editingContext.objectsWithFetchSpecification(mockFetchSpecification, editingContext);

	verify(mockCoordinator, never()).objectsWithFetchSpecification(mockFetchSpecification, editingContext);
    }

    @Test
    public void incrementPermanentGlobalIdForEachMockInstance() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	for (int i = 0; i < 10; i++) {
	    FooEntity entity = editingContext.createMock(FooEntity.class);

	    EOKeyGlobalID result = (EOKeyGlobalID) editingContext.globalIDForObject(entity);

	    assertThat((Integer) result.keyValues()[0], is(i + 1));
	}
    }

    @Test
    public void insertMockInstanceCallAwakeFromInsertion() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockInstance = spy(new FooEntity());

	editingContext.insertMock(mockInstance);

	verify(mockInstance, times(1)).awakeFromInsertion(editingContext);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void insertMockInstanceForObject() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockInstance = new FooEntity();

	editingContext.insertMock(mockInstance);

	assertThat(editingContext.registeredObjects(), hasItem(mockInstance));
    }

    @Test
    public void mockEditingContextHasParentMockObjectStoreCoordinator() throws Exception {
	MockEditingContext editingContext = new MockEditingContext();

	assertThat(editingContext.parentObjectStore(), instanceOf(MockObjectStoreCoordinator.class));
    }

    @Test
    public void mockInstanceHasPermanentGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity entity = editingContext.createMock(FooEntity.class);

	EOGlobalID result = editingContext.globalIDForObject(entity);

	assertThat(result.isTemporary(), is(false));
    }

    @Test
    public void recordMockInstanceForFutureFetching() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	FooEntity mockEntity = editingContext.createMock(FooEntity.class);

	EOFetchSpecification fetchSpecification = new EOFetchSpecification(FooEntity.ENTITY_NAME, null, null);

	@SuppressWarnings("unchecked")
	NSArray<FooEntity> result = editingContext.objectsWithFetchSpecification(fetchSpecification);

	assertThat(result.size(), is(1));
	assertThat(result.get(0), is(mockEntity));
    }

    @Test
    public void removeModelsLoadedAfterTestExecution() throws Throwable {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	editingContext.before();
	editingContext.after();

	assertThat(EOModelGroup.defaultGroup().modelNamed(TEST_MODEL_NAME), nullValue());
    }

    @Test
    public void savedObjectHasNonTemporaryGlobalId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	assertThat(mockFoo.__globalID().isTemporary(), is(false));

	editingContext.after();
    }

    @Test
    public void savedObjectIncrementsGlobalFakeId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	editingContext.createMock(FooEntity.class);

	FooEntity mockFoo = FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	assertThat((Integer) ((EOKeyGlobalID) mockFoo.__globalID()).keyValues()[0], is(2));

	editingContext.after();
    }

    @Test
    public void savedObjectUsesGlobalFakeId() throws Exception {
	MockEditingContext editingContext = new MockEditingContext(TEST_MODEL_NAME);

	editingContext.before();

	FooEntity.createFooEntity(editingContext);

	editingContext.saveChanges();

	FooEntity mockFoo = editingContext.createMock(FooEntity.class);

	assertThat((Integer) ((EOKeyGlobalID) mockFoo.__globalID()).keyValues()[0], is(2));

	editingContext.after();
    }
}
