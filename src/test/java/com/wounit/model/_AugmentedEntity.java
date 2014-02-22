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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to AugmentedEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _AugmentedEntity extends er.extensions.partials.ERXPartial<com.wounit.model.BaseEntity> {
	public static final String ENTITY_NAME = "BaseEntity";

	// Attributes
	public static final String AUGMENTED_ATTRIBUTE_KEY = "augmentedAttribute";

	// Relationships
	public static final String RELATIONSHIP_TO_ORDINARIES_KEY = "relationshipToOrdinaries";

  private static Logger LOG = Logger.getLogger(_AugmentedEntity.class);

  public Integer augmentedAttribute() {
    return (Integer) storedValueForKey("augmentedAttribute");
  }

  public void setAugmentedAttribute(Integer value) {
    if (_AugmentedEntity.LOG.isDebugEnabled()) {
    	_AugmentedEntity.LOG.debug( "updating augmentedAttribute from " + augmentedAttribute() + " to " + value);
    }
    takeStoredValueForKey(value, "augmentedAttribute");
  }

  public NSArray<com.wounit.model.OrdinaryEntity> relationshipToOrdinaries() {
    return (NSArray<com.wounit.model.OrdinaryEntity>)storedValueForKey("relationshipToOrdinaries");
  }

  public NSArray<com.wounit.model.OrdinaryEntity> relationshipToOrdinaries(EOQualifier qualifier) {
    return relationshipToOrdinaries(qualifier, null, false);
  }

  public NSArray<com.wounit.model.OrdinaryEntity> relationshipToOrdinaries(EOQualifier qualifier, boolean fetch) {
    return relationshipToOrdinaries(qualifier, null, fetch);
  }

  public NSArray<com.wounit.model.OrdinaryEntity> relationshipToOrdinaries(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<com.wounit.model.OrdinaryEntity> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(com.wounit.model.OrdinaryEntity.RELATIONSHIP_TO_PARTIAL_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray qualifiers = new NSMutableArray();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = com.wounit.model.OrdinaryEntity.fetchOrdinaryEntities(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = relationshipToOrdinaries();
      if (qualifier != null) {
        results = (NSArray<com.wounit.model.OrdinaryEntity>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<com.wounit.model.OrdinaryEntity>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToRelationshipToOrdinariesRelationship(com.wounit.model.OrdinaryEntity object) {
    if (_AugmentedEntity.LOG.isDebugEnabled()) {
      _AugmentedEntity.LOG.debug("adding " + object + " to relationshipToOrdinaries relationship");
    }
    addObjectToBothSidesOfRelationshipWithKey(object, "relationshipToOrdinaries");
  }

  public void removeFromRelationshipToOrdinariesRelationship(com.wounit.model.OrdinaryEntity object) {
    if (_AugmentedEntity.LOG.isDebugEnabled()) {
      _AugmentedEntity.LOG.debug("removing " + object + " from relationshipToOrdinaries relationship");
    }
    removeObjectFromBothSidesOfRelationshipWithKey(object, "relationshipToOrdinaries");
  }

  public com.wounit.model.OrdinaryEntity createRelationshipToOrdinariesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName("OrdinaryEntity");
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, "relationshipToOrdinaries");
    return (com.wounit.model.OrdinaryEntity) eo;
  }

  public void deleteRelationshipToOrdinariesRelationship(com.wounit.model.OrdinaryEntity object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, "relationshipToOrdinaries");
    editingContext().deleteObject(object);
  }

  public void deleteAllRelationshipToOrdinariesRelationships() {
    Enumeration objects = relationshipToOrdinaries().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteRelationshipToOrdinariesRelationship((com.wounit.model.OrdinaryEntity)objects.nextElement());
    }
  }


  public AugmentedEntity initAugmentedEntity(EOEditingContext editingContext) {
    AugmentedEntity eo = (AugmentedEntity)this;    
    return eo;
  }
}
