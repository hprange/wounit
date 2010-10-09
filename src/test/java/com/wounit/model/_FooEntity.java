// $LastChangedRevision: 5810 $ DO NOT EDIT.  Make changes to FooEntity.java instead.
package com.wounit.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _FooEntity extends er.extensions.eof.ERXGenericRecord {
	public static final String ENTITY_NAME = "FooEntity";

	// Attributes
	public static final String BAR_KEY = "bar";

	// Relationships

  private static Logger LOG = Logger.getLogger(_FooEntity.class);

  public FooEntity localInstanceIn(EOEditingContext editingContext) {
    FooEntity localInstance = (FooEntity)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String bar() {
    return (String) storedValueForKey("bar");
  }

  public void setBar(String value) {
    if (_FooEntity.LOG.isDebugEnabled()) {
    	_FooEntity.LOG.debug( "updating bar from " + bar() + " to " + value);
    }
    takeStoredValueForKey(value, "bar");
  }


  public static FooEntity createFooEntity(EOEditingContext editingContext) {
    FooEntity eo = (FooEntity) EOUtilities.createAndInsertInstance(editingContext, _FooEntity.ENTITY_NAME);    
    return eo;
  }

  public static NSArray<FooEntity> fetchAllFooEntities(EOEditingContext editingContext) {
    return _FooEntity.fetchAllFooEntities(editingContext, null);
  }

  public static NSArray<FooEntity> fetchAllFooEntities(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _FooEntity.fetchFooEntities(editingContext, null, sortOrderings);
  }

  public static NSArray<FooEntity> fetchFooEntities(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_FooEntity.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<FooEntity> eoObjects = (NSArray<FooEntity>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static FooEntity fetchFooEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _FooEntity.fetchFooEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooEntity fetchFooEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<FooEntity> eoObjects = _FooEntity.fetchFooEntities(editingContext, qualifier, null);
    FooEntity eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (FooEntity)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one FooEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooEntity fetchRequiredFooEntity(EOEditingContext editingContext, String keyName, Object value) {
    return _FooEntity.fetchRequiredFooEntity(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static FooEntity fetchRequiredFooEntity(EOEditingContext editingContext, EOQualifier qualifier) {
    FooEntity eoObject = _FooEntity.fetchFooEntity(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no FooEntity that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static FooEntity localInstanceIn(EOEditingContext editingContext, FooEntity eo) {
    FooEntity localInstance = (eo == null) ? null : (FooEntity)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
