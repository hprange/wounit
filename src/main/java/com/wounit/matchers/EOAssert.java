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

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import er.extensions.eof.ERXKey;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static org.hamcrest.CoreMatchers.not;

/**
 * This class is the entry point for writing assertions to WebObjects related
 * unit tests.
 * <p>
 * Before start using this framework, remember to use the import static feature
 * in order to write more readable code:
 * 
 * <pre>
 * import static com.wounit.EOAssert.*;
 * </pre>
 *
 * The {@code EOAssert} class provides static methods to check whether an
 * enterprise object can be saved or deleted and if it has been saved or
 * deleted.
 * <p>
 * <b>Confirm that an enterprise object can/cannot be saved</b>
 *
 * <pre>
 * // Checks whether the eo can be saved
 * confirm(eo, canBeSaved());
 * </pre>
 * 
 * <pre>
 * // Checks whether the eo cannot be saved
 * confirm(eo, cannotBeSaved());
 * </pre>
 * 
 * <pre>
 * // Safer check whether eo cannot be saved
 * confirm(eo, cannotBeSavedBecause(&quot;The foo property cannot be null&quot;));
 * </pre>
 * <p>
 * <b>Confirm that an enterprise object can/cannot be deleted</b>
 *
 * <pre>
 * // Checks whether the eo can be deleted
 * confirm(eo, canBeDeleted());
 * </pre>
 * 
 * <pre>
 * // Checks whether the eo cannot be deleted
 * confirm(eo, cannotBeDeleted());
 * </pre>
 * 
 * <pre>
 * // Safer check whether eo cannot be deleted
 * confirm(eo, cannotBeDeletedBecause(&quot;It is a required object&quot;));
 * </pre>
 * <p>
 * <b>Confirm that an enterprise object has/hasn't been saved</b>
 *
 * <pre>
 * // Checks whether the eo has been saved and has no pending changes
 * confirm(eo, hasBeenSaved());
 * </pre>
 * 
 * <pre>
 * // Checks whether the eo has not been saved
 * confirm(eo, hasNotBeenSaved());
 * </pre>
 * <p>
 * <b>Confirm that an enterprise object has/hasn't been deleted</b>
 *
 * <pre>
 * // Checks whether the eo has been deleted
 * confirm(eo, hasBeenDeleted());
 * </pre>
 * 
 * <pre>
 * // Checks whether the eo has not been deleted
 * confirm(eo, hasNotBeenDeleted());
 * </pre>
 *
 * The {@code EOAssert} class also offers methods to check if an
 * {@code EOEditingContext} can save changes correctly.
 * <p>
 * <b>Confirm that an editing context save changes successfully or not</b>
 *
 * <pre>
 * // Checks whether the editing context save changes successfully
 * confirm(editingContext, saveChanges());
 * </pre>
 * 
 * <pre>
 * // Checks whether the editing context does not save changes successfully
 * confirm(editingContext, doNotSaveChanges());
 * </pre>
 * 
 * <pre>
 * // Safer check whether the editing context does not save changes successfully
 * confirm(editingContext, doNotSaveChangesBecause(&quot;The bar property of Foo cannot be null&quot;));
 * </pre>
 * <p>
 * The {@code EOAssert} class provides static methods to check where a value matches an expression
 * using the key-value coding mechanism.
 * <pre>
 * // Checks whether a value matches an expression using the key-value coding mechanism
 * assertThat(fooEntity, hasValueForKey(FooEntity.BAR_KEY, is("a value")));
 * </pre>
 *
 * <pre>
 *  // Checks whether an NSArray contains an item whose value matches an expression using the key-value coding mechanism
 *  assertThat(arrayOfFoos, hasItem(hasValueForKey(FooEntity.BAR_KEY, is("a value"))));
 *  </pre>
 *
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @see org.junit.Assert
 * @see org.junit.matchers.JUnitMatchers
 */
public class EOAssert {
    /**
     * Used to check whether an enterprise object can be deleted without
     * throwing any exceptions.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object can be
     *         deleted
     */
    public static Matcher<EOEnterpriseObject> canBeDeleted() {
	return new CanBeDeletedMatcher<EOEnterpriseObject>();
    }

    /**
     * Used to check whether an enterprise object can be saved without throwing
     * any exceptions.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object can be
     *         saved
     */
    public static Matcher<EOEnterpriseObject> canBeSaved() {
	return new CanBeSavedMatcher<EOEnterpriseObject>();
    }

    /**
     * Used to check whether an enterprise object can <b>NOT</b> be deleted.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object can
     *         <b>NOT</b> be deleted
     */
    public static Matcher<EOEnterpriseObject> cannotBeDeleted() {
	return not(canBeDeleted());
    }

    /**
     * Used to check whether an enterprise object can <b>NOT</b> be deleted
     * because of the matching expected exception.
     * 
     * @param message
     *            the expected exception message
     * @return a <code>Matcher</code> matching if the enterprise object can
     *         <b>NOT</b> be deleted
     */
    public static Matcher<EOEnterpriseObject> cannotBeDeletedBecause(String message) {
	return not(new CanBeDeletedMatcher<EOEnterpriseObject>(message));
    }

    /**
     * Used to check whether an enterprise object can <b>NOT</b> be saved.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object can
     *         <b>NOT</b> be saved
     */
    public static Matcher<EOEnterpriseObject> cannotBeSaved() {
	return not(canBeSaved());
    }

    /**
     * Used to check whether an enterprise object can <b>NOT</b> be saved
     * because of the matching expected exception.
     * 
     * @param message
     *            the expected exception message
     * @return a <code>Matcher</code> matching if the enterprise object can
     *         <b>NOT</b> be saved
     */
    public static Matcher<EOEnterpriseObject> cannotBeSavedBecause(String message) {
	return not(new CanBeSavedMatcher<EOEnterpriseObject>(message));
    }

    /**
     * Confirms that <code>enterpriseObject</code> satisfies the condition
     * specified by <code>matcher</code>. If not, an {@link AssertionError} is
     * thrown with information about the matcher and failing value. Example:
     * 
     * <pre>
     * confirm(eo, hasBeenSaved());
     * </pre>
     * 
     * The confirm word was selected to avoid conflicts with assertThat from
     * JUnit and verify from most mock objects libraries.
     * 
     * @param <T>
     *            the static type accepted by the matcher
     * @param enterpriseObject
     *            the enterprise object to be checked
     * @param matcher
     *            an expression, built of {@link Matcher}s, specifying allowed
     *            values
     * @see org.hamcrest.CoreMatchers
     * @see org.junit.matchers.JUnitMatchers
     */
    public static <T extends EOEnterpriseObject> void confirm(T enterpriseObject, Matcher<T> matcher) {
	confirmImplementation(enterpriseObject, matcher);
    }

    /**
     * Confirms that <code>actual</code> satisfies the condition specified by
     * <code>matcher</code>. If not, an {@link AssertionError} is thrown with
     * information about the matcher and failing value. Example:
     * 
     * <pre>
     * confirm(editingContext, doNotSaveChanges());
     * </pre>
     * 
     * The confirm word was selected to avoid conflicts with assertThat from
     * JUnit and verify from most mock objects libraries.
     * 
     * @param <T>
     *            the static type accepted by the matcher
     * @param actual
     *            the editing context to be checked
     * @param matcher
     *            an expression, built of {@link Matcher}s, specifying allowed
     *            values
     * @see org.hamcrest.CoreMatchers
     * @see org.junit.matchers.JUnitMatchers
     */
    public static <T extends EOEditingContext> void confirm(T actual, Matcher<T> matcher) {
	confirmImplementation(actual, matcher);
    }

    private static <T> void confirmImplementation(T actual, Matcher<T> matcher) {
	if (matcher.matches(actual)) {
	    return;
	}

	Description description = new StringDescription();

	description.appendText("\nExpected: ");
	description.appendDescriptionOf(matcher);

	throw new AssertionError(description.toString());
    }

    /**
     * Used to check whether an editing context is <b>NOT</b> able to save
     * changes.
     * 
     * @return a <code>Matcher</code> matching if the editing context can
     *         <b>NOT</b> be saved
     */
    public static Matcher<EOEditingContext> doNotSaveChanges() {
	return not(saveChanges());
    }

    /**
     * Used to check whether an editing context is <b>NOT</b> able to save
     * changes because of the matching expected exception.
     * 
     * @param message
     *            the expected exception message
     * @return a <code>Matcher</code> matching if the editing context can
     *         <b>NOT</b> be saved
     */
    public static Matcher<EOEditingContext> doNotSaveChangesBecause(String message) {
	return not(new SaveChangesMatcher<EOEditingContext>(message));
    }

    /**
     * Used to check whether an enterprise object has been deleted and is no
     * longer managed by an editing context.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object has been
     *         deleted
     */
    public static Matcher<EOEnterpriseObject> hasBeenDeleted() {
	return new HasBeenDeletedMatcher<EOEnterpriseObject>();
    }

    /**
     * Used to check whether an enterprise object has been saved and has no
     * pending changes to be saved.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object has been
     *         saved
     */
    public static Matcher<EOEnterpriseObject> hasBeenSaved() {
	return new HasBeenSavedMatcher<EOEnterpriseObject>();
    }

    /**
     * Used to check whether an enterprise object has <b>NOT</b> been deleted
     * and is still managed by an editing context.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object has
     *         <b>NOT</b> been deleted
     */
    public static Matcher<EOEnterpriseObject> hasNotBeenDeleted() {
	return not(hasBeenDeleted());
    }

    /**
     * Used to check whether an enterprise object has <b>NOT</b> been saved or
     * still has pending changes to be saved.
     * 
     * @return a <code>Matcher</code> matching if the enterprise object has
     *         <b>NOT</b> been saved
     */
    public static Matcher<EOEnterpriseObject> hasNotBeenSaved() {
	return not(hasBeenSaved());
    }

    /**
     * Used to check whether an editing context is able to save changes
     * successfully.
     * 
     * @return a <code>Matcher</code> matching if the editing context can save
     *         changes correctly
     */
    public static Matcher<EOEditingContext> saveChanges() {
	return new SaveChangesMatcher<EOEditingContext>();
    }

    /**
     * Used to check whether a value matches an expression using the key-value coding mechanism.
     *
     * @param key          identifies the property to retrieve
     * @param valueMatcher a matcher for the value obtained using the key of the examined object
     * @return a <code>Matcher</code> matching if the value matches an expression using the key-value
     * coding mechanism.
     */
    public static <T> Matcher<T> hasValueForKey(String key, Matcher<?> valueMatcher) {
        return HasValueForKeyMatcher.hasValueForKey(key, valueMatcher);
    }

    /**
     * Used to check whether a value matches an expression using the key-value coding mechanism.
     *
     * @param key          identifies the property to retrieve
     * @param valueMatcher a matcher for the value obtained using the key of the examined object
     * @return a <code>Matcher</code> matching if the value matches an expression using the key-value
     * coding mechanism.
     */
    public static <T, V> Matcher<T> hasValueForKey(ERXKey<V> key, Matcher<V> valueMatcher) {
        return HasValueForKeyMatcher.hasValueForKey(key.key(), valueMatcher);
    }

    /**
     * <code>EOAssert</code> is not intended to be instantiated.
     */
    private EOAssert() {
    }
}
