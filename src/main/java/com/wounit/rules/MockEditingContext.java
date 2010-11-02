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
import com.webobjects.eocontrol.EOTemporaryGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import er.extensions.foundation.ERXArrayUtilities;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class MockEditingContext extends AbstractEditingContextRule {
    private int globalFakeId = 1;

    /**
     * Constructor only for test purposes.
     */
    MockEditingContext(EOObjectStoreCoordinator objectStore, String... modelNames) {
	super(objectStore, modelNames);
    }

    public MockEditingContext(String... modelNames) {
	this(new MockObjectStoreCoordinator(), modelNames);
    }

    /**
     * Create an instance of the specified class and insert into the temporary
     * editing context.
     * 
     * @param <T>
     *            the static type of the instance that should be instantiated
     * @param clazz
     *            the class of the entity that should be instantiated
     * @return an instance of the given class
     */
    @SuppressWarnings("unchecked")
    public <T extends EOEnterpriseObject> T createMock(Class<T> clazz) {
	if (clazz == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null class.");
	}

	String entityName = clazz.getSimpleName();

	if (EOModelGroup.defaultGroup().entityNamed(entityName) != null) {
	    return (T) createMock(entityName);
	}

	try {
	    Field field = clazz.getField("ENTITY_NAME");

	    entityName = (String) field.get(null);
	} catch (Exception exception) {
	    throw new IllegalArgumentException("Cannot create an instance based on the provided class. Please, provide an entity name instead.", exception);
	}

	T instance = (T) createMock(entityName);

	return instance;
    }

    /**
     * Create an instance of the specified entity named and insert into the
     * temporary editing context.
     * 
     * @param <T>
     *            the static type of the enterprise object returned by this
     *            method
     * @param entityName
     *            the name of the entity that should be instantiated
     * @return an instance of the given entity named
     */
    public <T extends EOEnterpriseObject> T createMock(String entityName) {
	if (entityName == null) {
	    throw new IllegalArgumentException("Cannot create an instance for a null entity name.");
	}

	EOClassDescription classDescription = EOClassDescription.classDescriptionForEntityName(entityName);

	if (classDescription == null) {
	    throw new IllegalArgumentException(String.format("Could not find EOClassDescription for entity name '%s'.", entityName));
	}

	@SuppressWarnings("unchecked")
	T eo = (T) classDescription.createInstanceWithEditingContext(this, null);

	insertMock(eo);

	return eo;
    }

    private EOGlobalID createPermanentGlobalFakeId(String entityName) {
	EOEntity entity = EOUtilities.entityNamed(this, entityName);

	NSArray<EOAttribute> primaryKeyAttributes = entity.primaryKeyAttributes();

	if (primaryKeyAttributes.count() != 1) {
	    throw new IllegalArgumentException(String.format("%s has a compound primary key and can't be used to create mock instances.", entityName));
	}

	String primaryKeyName = (primaryKeyAttributes.objectAtIndex(0)).name();

	NSDictionary<String, Integer> primaryKeyDictionary = new NSDictionary<String, Integer>(globalFakeId, primaryKeyName);

	globalFakeId++;

	return entity.globalIDForRow(primaryKeyDictionary);
    }

    public void insertMock(EOEnterpriseObject eo) {
	EOGlobalID globalId = createPermanentGlobalFakeId(eo.entityName());

	recordObject(eo, globalId);

	((EOCustomObject) eo).__setGlobalID(new EOTemporaryGlobalID());

	eo.awakeFromInsertion(this);

	((EOCustomObject) eo).__setGlobalID(globalId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NSArray<EOEnterpriseObject> objectsWithFetchSpecification(EOFetchSpecification eofetchspecification, EOEditingContext eoeditingcontext) {
	return registeredObjects();
    }

    @Override
    public void saveChanges() {
	@SuppressWarnings("unchecked")
	NSArray<EOEnterpriseObject> insertedObjects = ERXArrayUtilities.arrayMinusArray(insertedObjects(), deletedObjects());

	super.saveChanges();

	for (EOEnterpriseObject insertedObject : insertedObjects) {
	    forgetObject(insertedObject);

	    EOGlobalID globalId = createPermanentGlobalFakeId(insertedObject.entityName());

	    recordObject(insertedObject, globalId);
	}
    }
}
