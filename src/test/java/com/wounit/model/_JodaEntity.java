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
// $LastChangedRevision$ DO NOT EDIT.  Make changes to JodaEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _JodaEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "JodaEntity";

	// Attributes
	public static final String DATE_TIME_KEY = "dateTime";
	public static final String LOCAL_DATE_KEY = "localDate";
	public static final String LOCAL_DATE_TIME_KEY = "localDateTime";
	public static final String LOCAL_TIME_KEY = "localTime";

	// Relationships

  private static Logger LOG = Logger.getLogger(_JodaEntity.class);

  public JodaEntity localInstanceIn(EOEditingContext editingContext) {
    JodaEntity localInstance = (JodaEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public org.joda.time.DateTime dateTime() {
    return (org.joda.time.DateTime) storedValueForKey("dateTime");
  }

  public void setDateTime(org.joda.time.DateTime value) {
    if (_JodaEntity.LOG.isDebugEnabled()) {
    	_JodaEntity.LOG.debug( "updating dateTime from " + dateTime() + " to " + value);
    }
    takeStoredValueForKey(value, "dateTime");
  }

  public org.joda.time.LocalDate localDate() {
    return (org.joda.time.LocalDate) storedValueForKey("localDate");
  }

  public void setLocalDate(org.joda.time.LocalDate value) {
    if (_JodaEntity.LOG.isDebugEnabled()) {
    	_JodaEntity.LOG.debug( "updating localDate from " + localDate() + " to " + value);
    }
    takeStoredValueForKey(value, "localDate");
  }

  public org.joda.time.LocalDateTime localDateTime() {
    return (org.joda.time.LocalDateTime) storedValueForKey("localDateTime");
  }

  public void setLocalDateTime(org.joda.time.LocalDateTime value) {
    if (_JodaEntity.LOG.isDebugEnabled()) {
    	_JodaEntity.LOG.debug( "updating localDateTime from " + localDateTime() + " to " + value);
    }
    takeStoredValueForKey(value, "localDateTime");
  }

  public org.joda.time.LocalTime localTime() {
    return (org.joda.time.LocalTime) storedValueForKey("localTime");
  }

  public void setLocalTime(org.joda.time.LocalTime value) {
    if (_JodaEntity.LOG.isDebugEnabled()) {
    	_JodaEntity.LOG.debug( "updating localTime from " + localTime() + " to " + value);
    }
    takeStoredValueForKey(value, "localTime");
  }


  public static JodaEntity createJodaEntity(EOEditingContext editingContext) {
    JodaEntity eo = (JodaEntity) EOUtilities.createAndInsertInstance(editingContext, _JodaEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<JodaEntity> fetchAllJodaEntities(EOEditingContext editingContext) {
    return _JodaEntity.fetchAllJodaEntities(editingContext, null);
  }

  public static NSArray<JodaEntity> fetchAllJodaEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _JodaEntity.fetchJodaEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<JodaEntity> fetchJodaEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_JodaEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<JodaEntity> eoObjects = (NSArray<JodaEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static JodaEntity fetchJodaEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _JodaEntity.fetchJodaEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static JodaEntity fetchJodaEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<JodaEntity> eoObjects = _JodaEntity.fetchJodaEntities(editingContext, qualifier, null);
    JodaEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (JodaEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one JodaEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static JodaEntity fetchRequiredJodaEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _JodaEntity.fetchRequiredJodaEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static JodaEntity fetchRequiredJodaEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    JodaEntity eoObject = _JodaEntity.fetchJodaEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no JodaEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static JodaEntity localInstanceIn(EOEditingContext editingContext, JodaEntity eo) {
    JodaEntity localInstance = (eo == null) ? null : (JodaEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
