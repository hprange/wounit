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

import static com.wounit.rules.WOUnitUtils.arrayMinusArray;

import java.lang.reflect.Field;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOTemporaryGlobalID;
import com.webobjects.eocontrol._EOIntegralKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSRange;
import com.wounit.annotations.Dummy;

import er.extensions.eof.ERXModelGroup;
import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXS;

/**
 * <code>MockEditingContext</code> is a subclass of
 * {@link AbstractEditingContextRule} that provides means for fast in-memory
 * testing of <code>EOEnterpriseObject</code>s.
 * <p>
 * This class is useful for unit testing because it allows the creation of saved
 * objects that don't participate in the augmented transaction process. This
 * kind of feature is useful to validate the behavior of a unit in isolation.
 * 
 * <pre>
 * public class TestMyModel {
 *     &#064;Rule
 *     public MockEditingContext editingContext = new MockEditingContext(&quot;MyModel&quot;);
 * 
 *     &#064;Test
 *     public void testingMyModelLogic() throws Exception {
 * 	MyEntity instance = MyEntity.createMyEntity(editingContext);
 * 
 * 	AnotherEntity mockInstance = editingContext.createSavedObject(AnotherEntity.class);
 * 
 * 	// Do something with instance using the mockInstance...
 *     }
 * }
 * </pre>
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class MockEditingContext extends AbstractEditingContextRule {
    /**
     * This facade creates or inserts dummy enterprise objects using the
     * {@link MockEditingContext#createSavedObject(Class)} method.
     */
    static class DummyFacade extends EditingContextFacade {
	public DummyFacade(MockEditingContext editingContext) {
	    super(editingContext);
	}

	@Override
	public EOEnterpriseObject create(Class<? extends EOEnterpriseObject> type) {
	    return ((MockEditingContext) editingContext).createSavedObject(type);
	}

	@Override
	public void insert(EOEnterpriseObject object) {
	    ((MockEditingContext) editingContext).insertSavedObject(object);
	}
    }

    /**
     * Entity name key representation.
     */
    private static final String ENTITY_NAME_KEY = "entityName";

    private static final long serialVersionUID = 1L;

    private static boolean hasLongPrimaryKey(String entityName) {
	EOEntity entity = ERXModelGroup.defaultGroup().entityNamed(entityName);

	NSArray<EOAttribute> primaryKeys = entity.primaryKeyAttributes();

	return primaryKeys.size() == 1 && primaryKeys.get(0).valueTypeClassName().equals(Long.class.getName());
    }

    /**
     * A counter for fake global IDs.
     */
    private long globalFakeId = 0L;

    /**
     * An array of objects whose changes must be ignored during the test cycle.
     */
    final NSMutableArray<EOEnterpriseObject> ignoredObjects = new NSMutableArray<EOEnterpriseObject>();

    /**
     * Constructor only for test purposes.
     */
    MockEditingContext(EOObjectStoreCoordinator objectStore, String... modelNames) {
	super(objectStore, modelNames);
    }

    /**
     * Creates a <code>MockEditingContext</code> and loads all models with name
     * specified by parameter.
     * 
     * @param modelNames
     *            the name of all models required by unit tests.
     */
    public MockEditingContext(String... modelNames) {
	this(new MockObjectStoreCoordinator(), modelNames);
    }

    /**
     * Clear the ignored objects array after the test execution.
     *
     * @see com.wounit.rules.AbstractEditingContextRule#after()
     */
    @Override
    protected void after() {
	ignoredObjects.clear();

	super.after();
    }

    /**
     * Create dummy objects for fields annotated with @Dummy before the test
     * execution.
     *
     * @see com.wounit.rules.AbstractEditingContextRule#before()
     */
    @Override
    protected void before() {
	super.before();

	processor.process(Dummy.class, new DummyFacade(this));
    }

    private EOGlobalID createPermanentGlobalFakeId(String entityName) {
	globalFakeId++;

	if (hasLongPrimaryKey(entityName)) {
	    return new _EOIntegralKeyGlobalID(entityName, globalFakeId);
	}

	return new _EOIntegralKeyGlobalID(entityName, (int) globalFakeId);
    }

    /**
     * Create an instance of the specified class and insert into the mock
     * editing context.
     * 
     * @param <T>
     *            the static type of the instance that should be instantiated
     * @param clazz
     *            the class of the entity that should be instantiated
     * @return an instance of the given class
     */
    @SuppressWarnings("unchecked")
    public <T extends EOEnterpriseObject> T createSavedObject(Class<T> clazz) {
	if (clazz == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null class.");
	}

	String entityName = clazz.getSimpleName();

	if (EOModelGroup.defaultGroup().entityNamed(entityName) != null) {
	    return (T) createSavedObject(entityName);
	}

	try {
	    Field field = clazz.getField("ENTITY_NAME");

	    entityName = (String) field.get(null);
	} catch (Exception exception) {
	    throw new IllegalArgumentException("Cannot create an instance based on the provided class. Please, provide an entity name instead.", exception);
	}

	T instance = (T) createSavedObject(entityName);

	return instance;
    }

    /**
     * Create an instance of the specified entity named and insert into the mock
     * editing context.
     * 
     * @param <T>
     *            the static type of the enterprise object returned by this
     *            method
     * @param entityName
     *            the name of the entity that should be instantiated
     * @return an instance of the given entity named
     */
    public <T extends EOEnterpriseObject> T createSavedObject(String entityName) {
	if (entityName == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null entity name.");
	}

	EOClassDescription classDescription = EOClassDescription.classDescriptionForEntityName(entityName);

	if (classDescription == null) {
	    throw new IllegalArgumentException(String.format("Could not find EOClassDescription for entity name '%s'.", entityName));
	}

	@SuppressWarnings("unchecked")
	T eo = (T) classDescription.createInstanceWithEditingContext(this, null);

	insertSavedObject(eo);

	return eo;
    }

    /**
     * Insert the instance specified by parameter into the mock editing context.
     * 
     * @param eo
     *            The <code>EOEnterpriseObject</code> that should inserted
     */
    public void insertSavedObject(EOEnterpriseObject eo) {
	ignoredObjects.add(eo);

	EOGlobalID globalId = createPermanentGlobalFakeId(eo.entityName());

	recordObject(eo, globalId);

	((EOCustomObject) eo).__setGlobalID(new EOTemporaryGlobalID());

	eo.awakeFromInsertion(this);

	((EOCustomObject) eo).__setGlobalID(globalId);
    }

    /**
     * Overrides the original method to return only the objects registered in
     * memory.
     * 
     * @see er.extensions.eof.ERXEC#objectsWithFetchSpecification(com.webobjects.eocontrol.EOFetchSpecification,
     *      com.webobjects.eocontrol.EOEditingContext)
     */
    @Override
    public NSArray<EOEnterpriseObject> objectsWithFetchSpecification(EOFetchSpecification fetchSpecification, EOEditingContext editingContext) {
	@SuppressWarnings("unchecked")
	NSArray<EOEnterpriseObject> availableObjects = arrayMinusArray(registeredObjects(), deletedObjects());

	String entityName = fetchSpecification.entityName();

	EOQualifier qualifier = ERXQ.is(ENTITY_NAME_KEY, entityName);

	if (fetchSpecification.isDeep()) {
	    NSArray<EOEntity> subEntities = EOUtilities.entityNamed(editingContext, entityName).subEntities();

	    for (EOEntity entity : subEntities) {
		qualifier = ERXQ.or(qualifier, ERXQ.is(ENTITY_NAME_KEY, entity.name()));
	    }
	}

	qualifier = ERXQ.and(qualifier, fetchSpecification.qualifier());

	availableObjects = ERXQ.filtered(availableObjects, qualifier);

	availableObjects = ERXS.sorted(availableObjects, fetchSpecification.sortOrderings());

	int limit = fetchSpecification.fetchLimit();

	if (limit > 0 && limit < availableObjects.size()) {
	    return availableObjects.subarrayWithRange(new NSRange(0, limit));
	}

	return availableObjects;
    }

    /**
     * Overrides the implementation inherited from
     * <code>EOEditingContext</code> to not call the super behavior for objects
     * registered with {@link #insertSavedObject(EOEnterpriseObject)} or {@link #createSavedObject(Class)}.
     *
     * @param object
     *            the object whose state is to be recorded
     * 
     * @see er.extensions.eof.ERXEC#objectWillChange(java.lang.Object)
     */
    @Override
    public void objectWillChange(Object object) {
	if (!ignoredObjects.contains(object)) {
	    super.objectWillChange(object);
	}
    }

    /**
     * Overrides the original method to set a fake <code>EOGlobalID</code> for
     * inserted objects being saved for the first time. A fake permanent
     * <code>EOGlobalID</code> is required to determine if an object was saved
     * or not.
     * 
     * @see er.extensions.eof.ERXEC#saveChanges()
     */
    @Override
    public void saveChanges() {
	@SuppressWarnings("unchecked")
	NSArray<EOEnterpriseObject> insertedObjects = arrayMinusArray(insertedObjects(), deletedObjects());

	super.saveChanges();

	for (EOEnterpriseObject insertedObject : insertedObjects) {
	    if (globalIDForObject(insertedObject) == null) {
		continue;
	    }

	    forgetObject(insertedObject);

	    EOGlobalID globalId = createPermanentGlobalFakeId(insertedObject.entityName());

	    recordObject(insertedObject, globalId);
	}
    }
}
