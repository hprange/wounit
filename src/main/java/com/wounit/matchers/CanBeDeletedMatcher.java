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

import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * Tests if the <code>EOEnterpriseObject</code> can be deleted.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @param <T>
 *            a kind of <code>EOEnterpriseObject</code>
 */
class CanBeDeletedMatcher<T extends EOEnterpriseObject> extends AbstractEnhancedTypeSafeMatcher<T> {
    public CanBeDeletedMatcher() {
	super();
    }

    public CanBeDeletedMatcher(String message) {
	super(message);
    }

    public void describeTo(Description description) {
	if (message != null) {
	    description.appendText("expecting exception other than \"");
	    description.appendText(message);
	    description.appendText("\"\n     but got: ");

	    if (exception == null) {
		description.appendText("no validation exception");

		return;
	    }

	    description.appendText(exception.getClass().getName());
	    description.appendText(": \"");
	    description.appendText(exception.getMessage());
	    description.appendText("\"");

	    return;
	}

	description.appendText("valid for delete enterprise object");
	description.appendText("\n     but got: ");

	if (exception == null) {
	    description.appendText("a valid one");

	    return;
	}

	description.appendText(exception.getClass().getName());
	description.appendText(": ");
	description.appendValue(exception.getMessage());
    }

    @Override
    protected void matchesWithPossibleException(T eo) throws Exception {
	eo.validateForDelete();
    }
}
