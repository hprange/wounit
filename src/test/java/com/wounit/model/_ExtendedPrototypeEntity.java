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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to ExtendedPrototypeEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _ExtendedPrototypeEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "ExtendedPrototypeEntity";

	// Attributes
	public static final String EXTENDED_ATTRIBUTE_KEY = "extendedAttribute";

	// Relationships

  private static Logger LOG = Logger.getLogger(_ExtendedPrototypeEntity.class);

  public ExtendedPrototypeEntity localInstanceIn(EOEditingContext editingContext) {
    ExtendedPrototypeEntity localInstance = (ExtendedPrototypeEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public java.math.BigDecimal extendedAttribute() {
    return (java.math.BigDecimal) storedValueForKey("extendedAttribute");
  }

  public void setExtendedAttribute(java.math.BigDecimal value) {
    if (_ExtendedPrototypeEntity.LOG.isDebugEnabled()) {
    	_ExtendedPrototypeEntity.LOG.debug( "updating extendedAttribute from " + extendedAttribute() + " to " + value);
    }
    takeStoredValueForKey(value, "extendedAttribute");
  }


  public static ExtendedPrototypeEntity createExtendedPrototypeEntity(EOEditingContext editingContext, java.math.BigDecimal extendedAttribute
) {
    ExtendedPrototypeEntity eo = (ExtendedPrototypeEntity) EOUtilities.createAndInsertInstance(editingContext, _ExtendedPrototypeEntity.ENTITY_NAME);    
		eo.setExtendedAttribute(extendedAttribute);
    return eo;
  }

  public static NSArray<ExtendedPrototypeEntity> fetchAllExtendedPrototypeEntities(EOEditingContext editingContext) {
    return _ExtendedPrototypeEntity.fetchAllExtendedPrototypeEntities(editingContext, null);
  }

  public static NSArray<ExtendedPrototypeEntity> fetchAllExtendedPrototypeEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _ExtendedPrototypeEntity.fetchExtendedPrototypeEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<ExtendedPrototypeEntity> fetchExtendedPrototypeEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_ExtendedPrototypeEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<ExtendedPrototypeEntity> eoObjects = (NSArray<ExtendedPrototypeEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static ExtendedPrototypeEntity fetchExtendedPrototypeEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _ExtendedPrototypeEntity.fetchExtendedPrototypeEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static ExtendedPrototypeEntity fetchExtendedPrototypeEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<ExtendedPrototypeEntity> eoObjects = _ExtendedPrototypeEntity.fetchExtendedPrototypeEntities(editingContext, qualifier, null);
    ExtendedPrototypeEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (ExtendedPrototypeEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one ExtendedPrototypeEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static ExtendedPrototypeEntity fetchRequiredExtendedPrototypeEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _ExtendedPrototypeEntity.fetchRequiredExtendedPrototypeEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static ExtendedPrototypeEntity fetchRequiredExtendedPrototypeEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    ExtendedPrototypeEntity eoObject = _ExtendedPrototypeEntity.fetchExtendedPrototypeEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no ExtendedPrototypeEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static ExtendedPrototypeEntity localInstanceIn(EOEditingContext editingContext, ExtendedPrototypeEntity eo) {
    ExtendedPrototypeEntity localInstance = (eo == null) ? null : (ExtendedPrototypeEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
