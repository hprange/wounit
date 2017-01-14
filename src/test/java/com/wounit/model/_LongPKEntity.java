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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to LongPKEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _LongPKEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "LongPKEntity";

	// Attributes
	public static final String BAR_KEY = "bar";
	public static final String TYPE_KEY = "type";

	// Relationships

  private static Logger LOG = Logger.getLogger(_LongPKEntity.class);

  public LongPKEntity localInstanceIn(EOEditingContext editingContext) {
    LongPKEntity localInstance = (LongPKEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String bar() {
    return (String) storedValueForKey("bar");
  }

  public void setBar(String value) {
    if (_LongPKEntity.LOG.isDebugEnabled()) {
    	_LongPKEntity.LOG.debug( "updating bar from " + bar() + " to " + value);
    }
    takeStoredValueForKey(value, "bar");
  }

  public Integer type() {
    return (Integer) storedValueForKey("type");
  }

  public void setType(Integer value) {
    if (_LongPKEntity.LOG.isDebugEnabled()) {
    	_LongPKEntity.LOG.debug( "updating type from " + type() + " to " + value);
    }
    takeStoredValueForKey(value, "type");
  }


  public static LongPKEntity createLongPKEntity(EOEditingContext editingContext) {
    LongPKEntity eo = (LongPKEntity) EOUtilities.createAndInsertInstance(editingContext, _LongPKEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<LongPKEntity> fetchAllLongPKEntities(EOEditingContext editingContext) {
    return _LongPKEntity.fetchAllLongPKEntities(editingContext, null);
  }

  public static NSArray<LongPKEntity> fetchAllLongPKEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _LongPKEntity.fetchLongPKEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<LongPKEntity> fetchLongPKEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_LongPKEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<LongPKEntity> eoObjects = (NSArray<LongPKEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static LongPKEntity fetchLongPKEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _LongPKEntity.fetchLongPKEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static LongPKEntity fetchLongPKEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<LongPKEntity> eoObjects = _LongPKEntity.fetchLongPKEntities(editingContext, qualifier, null);
    LongPKEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (LongPKEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one LongPKEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static LongPKEntity fetchRequiredLongPKEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _LongPKEntity.fetchRequiredLongPKEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static LongPKEntity fetchRequiredLongPKEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    LongPKEntity eoObject = _LongPKEntity.fetchLongPKEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no LongPKEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static LongPKEntity localInstanceIn(EOEditingContext editingContext, LongPKEntity eo) {
    LongPKEntity localInstance = (eo == null) ? null : (LongPKEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
