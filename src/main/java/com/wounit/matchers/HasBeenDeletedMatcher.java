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

/**
 * Tests if the <code>EOEnterpriseObject</code> has been deleted.
 * <p>
 * The <code>HasBeenDeletedMatcher</code> expects the common usage of enterprise
 * objects. If an enterprise object is created and not added to an editing
 * context, the object will be considered successfully deleted even if it had
 * never been inserted into an editing context.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @param <T>
 *            a kind of <code>EOEnterpriseObject</code>
 */
class HasBeenDeletedMatcher<T extends EOEnterpriseObject> extends TypeSafeMatcher<T> {
    private String status;

    public void describeTo(Description description) {
	description.appendText(String.format("deleted object\n     but got: %s object", status));
    }

    private boolean isDeletedEO(T eo) {
	EOEditingContext editingContext = eo.editingContext();

	if (editingContext == null) {
	    return true;
	}

	return editingContext.deletedObjects().contains(eo);
    }

    @Override
    public boolean matchesSafely(T eo) {
	boolean isDeleted = isDeletedEO(eo);

	status = isDeleted ? "a deleted" : "an active";

	return isDeleted;
    }
}
