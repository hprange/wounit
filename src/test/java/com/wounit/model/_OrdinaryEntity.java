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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to OrdinaryEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _OrdinaryEntity extends er.extensions.partials.ERXPartialGenericRecord {
	public static final String ENTITY_NAME = "OrdinaryEntity";

	// Attributes

	// Relationships
	public static final String RELATIONSHIP_TO_PARTIAL_KEY = "relationshipToPartial";

  private static Logger LOG = Logger.getLogger(_OrdinaryEntity.class);

  public OrdinaryEntity localInstanceIn(EOEditingContext editingContext) {
    OrdinaryEntity localInstance = (OrdinaryEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public com.wounit.model.BaseEntity relationshipToPartial() {
    return (com.wounit.model.BaseEntity)storedValueForKey("relationshipToPartial");
  }

  public void setRelationshipToPartialRelationship(com.wounit.model.BaseEntity value) {
    if (_OrdinaryEntity.LOG.isDebugEnabled()) {
      _OrdinaryEntity.LOG.debug("updating relationshipToPartial from " + relationshipToPartial() + " to " + value);
    }
    if (value == null) {
    	com.wounit.model.BaseEntity oldValue = relationshipToPartial();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "relationshipToPartial");
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, "relationshipToPartial");
    }
  }
  

  public static OrdinaryEntity createOrdinaryEntity(EOEditingContext editingContext) {
    OrdinaryEntity eo = (OrdinaryEntity) EOUtilities.createAndInsertInstance(editingContext, _OrdinaryEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<OrdinaryEntity> fetchAllOrdinaryEntities(EOEditingContext editingContext) {
    return _OrdinaryEntity.fetchAllOrdinaryEntities(editingContext, null);
  }

  public static NSArray<OrdinaryEntity> fetchAllOrdinaryEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _OrdinaryEntity.fetchOrdinaryEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<OrdinaryEntity> fetchOrdinaryEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_OrdinaryEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<OrdinaryEntity> eoObjects = (NSArray<OrdinaryEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static OrdinaryEntity fetchOrdinaryEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _OrdinaryEntity.fetchOrdinaryEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static OrdinaryEntity fetchOrdinaryEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<OrdinaryEntity> eoObjects = _OrdinaryEntity.fetchOrdinaryEntities(editingContext, qualifier, null);
    OrdinaryEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (OrdinaryEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one OrdinaryEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OrdinaryEntity fetchRequiredOrdinaryEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _OrdinaryEntity.fetchRequiredOrdinaryEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static OrdinaryEntity fetchRequiredOrdinaryEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    OrdinaryEntity eoObject = _OrdinaryEntity.fetchOrdinaryEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no OrdinaryEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static OrdinaryEntity localInstanceIn(EOEditingContext editingContext, OrdinaryEntity eo) {
    OrdinaryEntity localInstance = (eo == null) ? null : (OrdinaryEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
