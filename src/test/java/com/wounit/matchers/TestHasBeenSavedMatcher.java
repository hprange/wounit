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
import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestHasBeenSavedMatcher {
    private HasBeenSavedMatcher<EOEnterpriseObject> matcher;

    private Description mockDescription;

    @Mock
    private EOEditingContext mockEditingContext;

    @Mock
    private EOGlobalID mockGlobalId;

    @Mock
    private EOEnterpriseObject mockObject;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void descriptionForHasBeenSaved() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);
	Mockito.when(mockEditingContext.globalIDForObject(mockObject)).thenReturn(mockGlobalId);
	Mockito.when(mockGlobalId.isTemporary()).thenReturn(false);

	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("saved object\n     but got: an object with saved changes"));
    }

    @Test
    public void descriptionForHasNotBeenDeleted() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);

	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("saved object\n     but got: an object with unsaved changes"));
    }

    @Test
    public void exceptionIfEditingContextIsNull() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(null);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("The enterprise object has no editing context reference. Are you sure the enterprise object was inserted into an editing context?");

	matcher.matchesSafely(mockObject);
    }

    @Test
    public void matchesHasBeenSaved() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);
	Mockito.when(mockEditingContext.globalIDForObject(mockObject)).thenReturn(mockGlobalId);
	Mockito.when(mockGlobalId.isTemporary()).thenReturn(false);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(true));
    }

    @Test
    public void matchesHasNotBeenSavedWithChangesInSnapshot() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);
	Mockito.when(mockEditingContext.globalIDForObject(mockObject)).thenReturn(mockGlobalId);
	Mockito.when(mockGlobalId.isTemporary()).thenReturn(false);

	NSDictionary<String, Object> changes = new NSMutableDictionary<String, Object>("foo", "bar");

	Mockito.when(mockObject.changesFromSnapshot(changes)).thenReturn(changes);
	Mockito.when(mockEditingContext.committedSnapshotForObject(mockObject)).thenReturn(changes);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Test
    public void matchesHasNotBeenSavedWithNullGlobalId() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);
	Mockito.when(mockEditingContext.globalIDForObject(mockObject)).thenReturn(null);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Test
    public void matchesHasNotBeenSavedWithTemporaryGlobalId() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);
	Mockito.when(mockEditingContext.globalIDForObject(mockObject)).thenReturn(mockGlobalId);
	Mockito.when(mockGlobalId.isTemporary()).thenReturn(true);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Before
    public void setup() {
	matcher = new HasBeenSavedMatcher<EOEnterpriseObject>();

	mockDescription = new StringDescription();

	Mockito.when(mockObject.changesFromSnapshot(Matchers.any(NSDictionary.class))).thenReturn(NSDictionary.emptyDictionary());
    }
}
