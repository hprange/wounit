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

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.wounit.annotations.Dummy;
import com.wounit.model.FooEntity;
import com.wounit.model.FooEntityWithRequiredField;
import com.wounit.rules.MockEditingContext;
import er.extensions.eof.ERXKey;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.wounit.matchers.EOAssert.hasValueForKey;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestHasValueForKeyMatcher {
    @Rule
    public final MockEditingContext editingContext = new MockEditingContext("Test");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Dummy
    FooEntity foo;

    @Dummy
    FooEntityWithRequiredField relatedFoo;

    NSArray<FooEntity> foos;

    @Before
    public void setup() {
        thrown.handleAssertionErrors();

        foo.setBar("sample");

        foos = new NSMutableArray<>();
        foos.add(foo);

        relatedFoo.setFooEntityRelationship(foo);
    }

    @Test
    public void hasValueForKeySuccess() {
        assertThat(foo, hasValueForKey(FooEntity.BAR_KEY, is("sample")));
    }

    @Test
    public void hasValueForKeyWhenValueIsWrong() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: hasValueForKey(\"bar\", is \"wrong\")\n     but: was \"sample\""));

        assertThat(foo, hasValueForKey(FooEntity.BAR_KEY, is("wrong")));
    }

    @Test
    public void hasValueForKeyInArray() {
        assertThat(foos, hasItem(hasValueForKey(FooEntity.BAR_KEY, is("sample"))));
    }

    @Test
    public void hasValueForKeyInEmptyArray() {
        foos.clear();

        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: a collection containing hasValueForKey(\"bar\", is \"sample\")\n     but: "));

        assertThat(foos, hasItem(hasValueForKey(FooEntity.BAR_KEY, is("sample"))));
    }

    @Test
    public void hasValueForKeyInArrayWithWrongValue() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: a collection containing hasValueForKey(\"bar\", is \"wrong\")\n     but: was \"sample\""));

        assertThat(foos, hasItem(hasValueForKey(FooEntity.BAR_KEY, is("wrong"))));
    }

    @Test
    public void hasValueForKeyWhenKeyDoesNotExist() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(both(startsWith("\nExpected: hasValueForKey(\"unknown\", is \"sample\")\n     but: <com.wounit.model.FooEntity"))
                .and(endsWith("valueForKey(): lookup of unknown key: 'unknown'.\nThis class does not have an instance variable of the name unknown or _unknown, nor a method of the name unknown, _unknown, getUnknown, or _getUnknown")));

        assertThat(foo, hasValueForKey("unknown", is("sample")));
    }

    @Test
    public void hasValueForKeyPath() {
        String keypath = FooEntityWithRequiredField.FOO_ENTITY_KEY + "." + FooEntity.BAR_KEY;

        assertThat(relatedFoo, hasValueForKey(keypath, is("sample")));
    }

    @Test
    public void hasValueForKeyWhenKeyPathDoesNotExist() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(both(startsWith("\nExpected: hasValueForKey(\"fooEntity.unknown\", is \"sample\")\n     but: <com.wounit.model.FooEntity"))
                .and(endsWith("valueForKey(): lookup of unknown key: 'unknown'.\nThis class does not have an instance variable of the name unknown or _unknown, nor a method of the name unknown, _unknown, getUnknown, or _getUnknown")));

        String keypath = FooEntityWithRequiredField.FOO_ENTITY_KEY + ".unknown";

        assertThat(relatedFoo, hasValueForKey(keypath, is("sample")));
    }

    @Test
    public void hasValueForKeyWhenObjectIsNull() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: hasValueForKey(\"bar\", is \"sample\")\n     but: was null"));

        assertThat(null, hasValueForKey(FooEntity.BAR_KEY, is("sample")));
    }

    @Test
    public void hasValueForKeyWhenKeyIsNull() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: hasValueForKey(null, is \"sample\")\n     but: No value for null key"));

        assertThat(foo, hasValueForKey((String) null, is("sample")));
    }

    @Test
    public void hasValueForKeyWhenValueIsNull() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(is("\nExpected: hasValueForKey(\"bar\", is null)\n     but: was \"sample\""));

        assertThat(foo, hasValueForKey(FooEntity.BAR_KEY, is((String) null)));
    }

    @Test
    public void hasValueForKeyWithERXKey() {
        ERXKey<String> key = new ERXKey<>(FooEntity.BAR_KEY);

        assertThat(foo, hasValueForKey(key, is("sample")));
    }

    @Test
    public void hasValueForKeyPathWithERXKey() {
        ERXKey<String> keypath = new ERXKey<>(FooEntityWithRequiredField.FOO_ENTITY_KEY).dot(new ERXKey<>(FooEntity.BAR_KEY));

        assertThat(relatedFoo, hasValueForKey(keypath, is("sample")));
    }
}
