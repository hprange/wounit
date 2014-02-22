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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to BaseEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _BaseEntity extends er.extensions.partials.ERXPartialGenericRecord {
	public static final String ENTITY_NAME = "BaseEntity";

	// Attributes
	public static final String BASE_ATTRIBUTE_KEY = "baseAttribute";

	// Relationships

  private static Logger LOG = Logger.getLogger(_BaseEntity.class);

  public BaseEntity localInstanceIn(EOEditingContext editingContext) {
    BaseEntity localInstance = (BaseEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String baseAttribute() {
    return (String) storedValueForKey("baseAttribute");
  }

  public void setBaseAttribute(String value) {
    if (_BaseEntity.LOG.isDebugEnabled()) {
    	_BaseEntity.LOG.debug( "updating baseAttribute from " + baseAttribute() + " to " + value);
    }
    takeStoredValueForKey(value, "baseAttribute");
  }


  public static BaseEntity createBaseEntity(EOEditingContext editingContext) {
    BaseEntity eo = (BaseEntity) EOUtilities.createAndInsertInstance(editingContext, _BaseEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<BaseEntity> fetchAllBaseEntities(EOEditingContext editingContext) {
    return _BaseEntity.fetchAllBaseEntities(editingContext, null);
  }

  public static NSArray<BaseEntity> fetchAllBaseEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _BaseEntity.fetchBaseEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<BaseEntity> fetchBaseEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_BaseEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<BaseEntity> eoObjects = (NSArray<BaseEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static BaseEntity fetchBaseEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _BaseEntity.fetchBaseEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static BaseEntity fetchBaseEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<BaseEntity> eoObjects = _BaseEntity.fetchBaseEntities(editingContext, qualifier, null);
    BaseEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (BaseEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one BaseEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static BaseEntity fetchRequiredBaseEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _BaseEntity.fetchRequiredBaseEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static BaseEntity fetchRequiredBaseEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    BaseEntity eoObject = _BaseEntity.fetchBaseEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no BaseEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static BaseEntity localInstanceIn(EOEditingContext editingContext, BaseEntity eo) {
    BaseEntity localInstance = (eo == null) ? null : (BaseEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
