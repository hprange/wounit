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


  public AugmentedEntity initAugmentedEntity(EOEditingContext editingContext) {
    AugmentedEntity eo = (AugmentedEntity)this;    
    return eo;
  }
}
