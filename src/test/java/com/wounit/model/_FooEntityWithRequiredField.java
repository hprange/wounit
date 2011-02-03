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

// $LastChangedRevision: 5773 $ DO NOT EDIT.  Make changes to FooEntityWithRequiredField.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _FooEntityWithRequiredField extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "FooEntityWithRequiredField";

	// Attributes
	public static final String REQUIRED_KEY = "required";

	// Relationships
	public static final String FOO_ENTITY_KEY = "fooEntity";

  private static Logger LOG = Logger.getLogger(_FooEntityWithRequiredField.class);

  public FooEntityWithRequiredField localInstanceIn(EOEditingContext editingContext) {
    FooEntityWithRequiredField localInstance = (FooEntityWithRequiredField)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public Integer required() {
    return (Integer) storedValueForKey("required");
  }

  public void setRequired(Integer value) {
    if (_FooEntityWithRequiredField.LOG.isDebugEnabled()) {
    	_FooEntityWithRequiredField.LOG.debug( "updating required from " + required() + " to " + value);
    }
    takeStoredValueForKey(value, "required");
  }

  public com.wounit.model.FooEntity fooEntity() {
    return (com.wounit.model.FooEntity)storedValueForKey("fooEntity");
  }

  public void setFooEntityRelationship(com.wounit.model.FooEntity value) {
    if (_FooEntityWithRequiredField.LOG.isDebugEnabled()) {
      _FooEntityWithRequiredField.LOG.debug("updating fooEntity from " + fooEntity() + " to " + value);
    }
    if (value == null) {
    	com.wounit.model.FooEntity oldValue = fooEntity();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "fooEntity");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "fooEntity");
    }
  }
  

  public static FooEntityWithRequiredField createFooEntityWithRequiredField(EOEditingContext editingContext, Integer required
) {
    FooEntityWithRequiredField eo = (FooEntityWithRequiredField) EOUtilities.createAndInsertInstance(editingContext, _FooEntityWithRequiredField.ENTITY_NAME);    
		eo.setRequired(required);
    return eo;
  }

  public static NSArray<FooEntityWithRequiredField> fetchAllFooEntityWithRequiredFields(EOEditingContext editingContext) {
    return _FooEntityWithRequiredField.fetchAllFooEntityWithRequiredFields(editingContext, null);
  }

  public static NSArray<FooEntityWithRequiredField> fetchAllFooEntityWithRequiredFields(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _FooEntityWithRequiredField.fetchFooEntityWithRequiredFields(editingContext, null, sortOrderings);
  }

  public static NSArray<FooEntityWithRequiredField> fetchFooEntityWithRequiredFields(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_FooEntityWithRequiredField.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<FooEntityWithRequiredField> eoObjects = (NSArray<FooEntityWithRequiredField>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static FooEntityWithRequiredField fetchFooEntityWithRequiredField(EOEditingContext editingContext, String keyName, Object value) {
    return _FooEntityWithRequiredField.fetchFooEntityWithRequiredField(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooEntityWithRequiredField fetchFooEntityWithRequiredField(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<FooEntityWithRequiredField> eoObjects = _FooEntityWithRequiredField.fetchFooEntityWithRequiredFields(editingContext, qualifier, null);
    FooEntityWithRequiredField eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (FooEntityWithRequiredField)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one FooEntityWithRequiredField that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooEntityWithRequiredField fetchRequiredFooEntityWithRequiredField(EOEditingContext editingContext, String keyName, Object value) {
    return _FooEntityWithRequiredField.fetchRequiredFooEntityWithRequiredField(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooEntityWithRequiredField fetchRequiredFooEntityWithRequiredField(EOEditingContext editingContext, EOQualifier qualifier) {
    FooEntityWithRequiredField eoObject = _FooEntityWithRequiredField.fetchFooEntityWithRequiredField(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no FooEntityWithRequiredField that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooEntityWithRequiredField localInstanceIn(EOEditingContext editingContext, FooEntityWithRequiredField eo) {
    FooEntityWithRequiredField localInstance = (eo == null) ? null : (FooEntityWithRequiredField)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
