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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

/**
 * Convenient base class for Matchers that can check if a produced exception is
 * the expected one.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @param T
 *            the type of the object to be matched
 */
abstract class AbstractEnhancedTypeSafeMatcher<T> extends TypeSafeMatcher<T> {
    protected Exception exception;

    protected final String message;

    /**
     * Creates a Matcher with no expected exception message.
     */
    public AbstractEnhancedTypeSafeMatcher() {
	this.message = null;
    }

    /**
     * Creates a Matcher with the message of an expected exception that will be
     * thrown.
     * 
     * @param message
     *            the expected exception message
     */
    public AbstractEnhancedTypeSafeMatcher(String message) {
	this.message = message;
    }

    /**
     * Checks whether an object matches an expected condition.
     * 
     * @see org.junit.internal.matchers.TypeSafeMatcher#matchesSafely(java.lang.Object)
     */
    @Override
    public final boolean matchesSafely(T item) {
	try {
	    matchesWithPossibleException(item);
	} catch (Exception exception) {
	    this.exception = exception;

	    if (message != null) {
		Matcher<String> matcher = is(not(message));

		return matcher.matches(exception.getMessage());
	    }

	    return false;
	}

	return true;
    }

    /**
     * Try to match a condition throwing an exception in some cases.
     * 
     * @param item
     *            the object to be matched
     * @throws Exception
     *             the <code>Exception</code> produced while trying to evaluate
     *             the matching condition
     */
    protected abstract void matchesWithPossibleException(T item) throws Exception;
}
