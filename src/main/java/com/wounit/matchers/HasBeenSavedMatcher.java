package com.wounit.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSDictionary;

/**
 * Tests if the <code>EOEnterpriseObject</code> has been saved.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @param <T>
 *            a kind of <code>EOEnterpriseObject</code>
 */
public class HasBeenSavedMatcher<T extends EOEnterpriseObject> extends TypeSafeMatcher<T> {
    private String status;

    public void describeTo(Description description) {
	description.appendText(String.format("saved object\n     but got: an object with %s changes", status));
    }

    @Override
    public boolean matchesSafely(T enterpriseObject) {
	EOEditingContext editingContext = enterpriseObject.editingContext();

	if (editingContext == null) {
	    throw new IllegalArgumentException("The enterprise object has no editing context reference. Are you sure the enterprise object was inserted into an editing context?");
	}

	EOGlobalID globalId = editingContext.globalIDForObject(enterpriseObject);

	boolean hasBeenSaved = !(globalId == null || globalId.isTemporary());

	@SuppressWarnings("unchecked")
	NSDictionary<String, Object> committedSnapshotForObject = editingContext.committedSnapshotForObject(enterpriseObject);

	hasBeenSaved = hasBeenSaved && enterpriseObject.changesFromSnapshot(committedSnapshotForObject).isEmpty();

	status = hasBeenSaved ? "saved" : "unsaved";

	return hasBeenSaved;
    }
}
