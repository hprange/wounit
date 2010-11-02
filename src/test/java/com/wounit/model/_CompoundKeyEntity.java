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

// $LastChangedRevision: 5773 $ DO NOT EDIT.  Make changes to CompoundKeyEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _CompoundKeyEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "CompoundKeyEntity";

	// Attributes

	// Relationships

  private static Logger LOG = Logger.getLogger(_CompoundKeyEntity.class);

  public CompoundKeyEntity localInstanceIn(EOEditingContext editingContext) {
    CompoundKeyEntity localInstance = (CompoundKeyEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


  public static CompoundKeyEntity createCompoundKeyEntity(EOEditingContext editingContext) {
    CompoundKeyEntity eo = (CompoundKeyEntity) EOUtilities.createAndInsertInstance(editingContext, _CompoundKeyEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<CompoundKeyEntity> fetchAllCompoundKeyEntities(EOEditingContext editingContext) {
    return _CompoundKeyEntity.fetchAllCompoundKeyEntities(editingContext, null);
  }

  public static NSArray<CompoundKeyEntity> fetchAllCompoundKeyEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _CompoundKeyEntity.fetchCompoundKeyEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<CompoundKeyEntity> fetchCompoundKeyEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_CompoundKeyEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<CompoundKeyEntity> eoObjects = (NSArray<CompoundKeyEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static CompoundKeyEntity fetchCompoundKeyEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _CompoundKeyEntity.fetchCompoundKeyEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static CompoundKeyEntity fetchCompoundKeyEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<CompoundKeyEntity> eoObjects = _CompoundKeyEntity.fetchCompoundKeyEntities(editingContext, qualifier, null);
    CompoundKeyEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (CompoundKeyEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one CompoundKeyEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CompoundKeyEntity fetchRequiredCompoundKeyEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _CompoundKeyEntity.fetchRequiredCompoundKeyEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static CompoundKeyEntity fetchRequiredCompoundKeyEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    CompoundKeyEntity eoObject = _CompoundKeyEntity.fetchCompoundKeyEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no CompoundKeyEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CompoundKeyEntity localInstanceIn(EOEditingContext editingContext, CompoundKeyEntity eo) {
    CompoundKeyEntity localInstance = (eo == null) ? null : (CompoundKeyEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
