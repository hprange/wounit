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

// $LastChangedRevision: 5773 $ DO NOT EDIT.  Make changes to SubFooEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _SubFooEntity extends com.wounit.model.FooEntity {
	public static final String ENTITY_NAME = "SubFooEntity";

	// Attributes
	public static final String BAR_KEY = "bar";
	public static final String TYPE_KEY = "type";

	// Relationships

  private static Logger LOG = Logger.getLogger(_SubFooEntity.class);

  public SubFooEntity localInstanceIn(EOEditingContext editingContext) {
    SubFooEntity localInstance = (SubFooEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }


  public static SubFooEntity createSubFooEntity(EOEditingContext editingContext) {
    SubFooEntity eo = (SubFooEntity) EOUtilities.createAndInsertInstance(editingContext, _SubFooEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<SubFooEntity> fetchAllSubFooEntities(EOEditingContext editingContext) {
    return _SubFooEntity.fetchAllSubFooEntities(editingContext, null);
  }

  public static NSArray<SubFooEntity> fetchAllSubFooEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _SubFooEntity.fetchSubFooEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<SubFooEntity> fetchSubFooEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_SubFooEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<SubFooEntity> eoObjects = (NSArray<SubFooEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static SubFooEntity fetchSubFooEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _SubFooEntity.fetchSubFooEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static SubFooEntity fetchSubFooEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<SubFooEntity> eoObjects = _SubFooEntity.fetchSubFooEntities(editingContext, qualifier, null);
    SubFooEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (SubFooEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one SubFooEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static SubFooEntity fetchRequiredSubFooEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _SubFooEntity.fetchRequiredSubFooEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static SubFooEntity fetchRequiredSubFooEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    SubFooEntity eoObject = _SubFooEntity.fetchSubFooEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no SubFooEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static SubFooEntity localInstanceIn(EOEditingContext editingContext, SubFooEntity eo) {
    SubFooEntity localInstance = (eo == null) ? null : (SubFooEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
