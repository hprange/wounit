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

import com.webobjects.foundation.NSKeyValueCodingAdditions;
import org.hamcrest.Condition;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Condition.matched;
import static org.hamcrest.Condition.notMatched;

/**
 * Matcher that asserts that a value obtained via the key-value mechanism meets the provided matcher.
 *
 * @param <T> the type of the object being checked
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.5
 */
class HasValueForKeyMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    public static <T> Matcher<T> hasValueForKey(String key, Matcher<?> valueMatcher) {
        return new HasValueForKeyMatcher<>(key, valueMatcher);
    }

    private final String key;
    private final Matcher<?> valueMatcher;

    public HasValueForKeyMatcher(String key, Matcher<?> valueMatcher) {
        this.key = key;
        this.valueMatcher = valueMatcher;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matchesSafely(T object, Description mismatch) {
        return hasKeyOn(object, mismatch)
                .and(hasValueForKey(object))
                .matching((Matcher<Object>) valueMatcher);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hasValueForKey(")
                .appendValue(key)
                .appendText(", ")
                .appendDescriptionOf(valueMatcher)
                .appendText(")");
    }

    private Condition<String> hasKeyOn(T object, Description mismatch) {
        if (key == null) {
            mismatch.appendText("No value for null key");
            return notMatched();
        }

        try {
            NSKeyValueCodingAdditions.Utility.valueForKeyPath(object, key);
            return matched(key, mismatch);
        } catch (Exception exception) {
            mismatch.appendText(exception.getMessage());
            return notMatched();
        }
    }

    private Condition.Step<String, Object> hasValueForKey(Object object) {
        return (key, mismatch) -> matched(NSKeyValueCodingAdditions.Utility.valueForKeyPath(object, key), mismatch);
    }
}
