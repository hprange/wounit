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
class HasBeenSavedMatcher<T extends EOEnterpriseObject> extends TypeSafeMatcher<T> {
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
