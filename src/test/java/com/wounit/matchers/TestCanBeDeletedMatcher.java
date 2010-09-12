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

import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSValidation;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCanBeDeletedMatcher {
    @Mock
    private EOEnterpriseObject mockObject;

    private CanBeDeletedMatcher<EOEnterpriseObject> matcher;

    private StringDescription mockDescription;

    @Test
    public void descriptionForCanBeDeletedFailure() throws Exception {
	Mockito.doThrow(new NSValidation.ValidationException("error")).when(mockObject).validateForDelete();

	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("valid for delete enterprise object\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"error\""));
    }

    @Test
    public void descriptionForCanBeDeletedSuccess() throws Exception {
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("valid for delete enterprise object\n     but got: a valid one"));
    }

    @Test
    public void descriptionForCanBeDeletedWithMessageFailure() throws Exception {
	matcher = new CanBeDeletedMatcher<EOEnterpriseObject>("An expected error");

	Mockito.doThrow(new NSValidation.ValidationException("An unexpectedError")).when(mockObject).validateForDelete();

	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("expecting exception other than \"An expected error\"\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"An unexpectedError\""));
    }

    @Test
    public void descriptionForCanBeDeletedWithMessageSuccess() throws Exception {
	matcher = new CanBeDeletedMatcher<EOEnterpriseObject>("An expected error");

	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("expecting exception other than \"An expected error\"\n     but got: no validation exception"));
    }

    @Test
    public void matchesCanBeDeleted() throws Exception {
	Mockito.doNothing().when(mockObject).validateForDelete();

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(true));
    }

    @Test
    public void matchesCannotBeDeleted() throws Exception {
	Mockito.doThrow(new NSValidation.ValidationException("error")).when(mockObject).validateForDelete();

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Test
    public void matchesCannotBeDeletedWithNoMatchingMessage() throws Exception {
	matcher = new CanBeDeletedMatcher<EOEnterpriseObject>("another error");

	Mockito.doThrow(new NSValidation.ValidationException("error")).when(mockObject).validateForDelete();

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(true));
    }

    @Test
    public void matchesCannotBeSavedWithMatchingMessage() throws Exception {
	matcher = new CanBeDeletedMatcher<EOEnterpriseObject>("error");

	Mockito.doThrow(new NSValidation.ValidationException("error")).when(mockObject).validateForDelete();

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Before
    public void setup() {
	matcher = new CanBeDeletedMatcher<EOEnterpriseObject>();

	mockDescription = new StringDescription();
    }
}
