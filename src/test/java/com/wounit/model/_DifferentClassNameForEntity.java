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

// $LastChangedRevision: 5773 $ DO NOT EDIT.  Make changes to DifferentClassNameForEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _DifferentClassNameForEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "EntityWithDifferentClassName";

	// Attributes

	// Relationships

  private static Logger LOG = Logger.getLogger(_DifferentClassNameForEntity.class);

  public DifferentClassNameForEntity localInstanceIn(EOEditingContext editingContext) {
    DifferentClassNameForEntity localInstance = (DifferentClassNameForEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


  public static DifferentClassNameForEntity createEntityWithDifferentClassName(EOEditingContext editingContext) {
    DifferentClassNameForEntity eo = (DifferentClassNameForEntity) EOUtilities.createAndInsertInstance(editingContext, _DifferentClassNameForEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<DifferentClassNameForEntity> fetchAllEntityWithDifferentClassNames(EOEditingContext editingContext) {
    return _DifferentClassNameForEntity.fetchAllEntityWithDifferentClassNames(editingContext, null);
  }

  public static NSArray<DifferentClassNameForEntity> fetchAllEntityWithDifferentClassNames(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _DifferentClassNameForEntity.fetchEntityWithDifferentClassNames(editingContext, null, sortOrderings);
  }

  public static NSArray<DifferentClassNameForEntity> fetchEntityWithDifferentClassNames(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_DifferentClassNameForEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<DifferentClassNameForEntity> eoObjects = (NSArray<DifferentClassNameForEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static DifferentClassNameForEntity fetchEntityWithDifferentClassName(EOEditingContext editingContext, String keyName, Object value) {
    return _DifferentClassNameForEntity.fetchEntityWithDifferentClassName(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static DifferentClassNameForEntity fetchEntityWithDifferentClassName(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<DifferentClassNameForEntity> eoObjects = _DifferentClassNameForEntity.fetchEntityWithDifferentClassNames(editingContext, qualifier, null);
    DifferentClassNameForEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (DifferentClassNameForEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one EntityWithDifferentClassName that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static DifferentClassNameForEntity fetchRequiredEntityWithDifferentClassName(EOEditingContext editingContext, String keyName, Object value) {
    return _DifferentClassNameForEntity.fetchRequiredEntityWithDifferentClassName(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static DifferentClassNameForEntity fetchRequiredEntityWithDifferentClassName(EOEditingContext editingContext, EOQualifier qualifier) {
    DifferentClassNameForEntity eoObject = _DifferentClassNameForEntity.fetchEntityWithDifferentClassName(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no EntityWithDifferentClassName that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static DifferentClassNameForEntity localInstanceIn(EOEditingContext editingContext, DifferentClassNameForEntity eo) {
    DifferentClassNameForEntity localInstance = (eo == null) ? null : (DifferentClassNameForEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
