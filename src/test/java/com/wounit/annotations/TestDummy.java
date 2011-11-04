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

package com.wounit.annotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.foundation.NSArray;
import com.wounit.model.FooEntity;
import com.wounit.model.FooEntityWithRequiredField;
import com.wounit.rules.MockEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDummy {
    @Dummy(size = 2)
    private NSArray<FooEntity> dummies;

    @Spy
    @Rule
    public MockEditingContext mockEditingContext = new MockEditingContext("Test");

    @Dummy
    private FooEntity mockEntity1, mockEntity2;

    @Dummy
    private FooEntityWithRequiredField mockEntity3;

    @Spy
    @Dummy
    private NSArray<FooEntity> spiedDummies;

    @Spy
    @Dummy
    private FooEntity spiedDummy;

    @Test
    public void verifyArrayContainsTheSpecifiedNumberOfDummyObjects() throws Exception {
	assertThat(dummies, notNullValue());
	assertThat(dummies.size(), is(2));
    }

    @Test
    public void verifyDummyObjectsCreation() throws Exception {
	assertThat(mockEntity1, notNullValue());
	assertThat(mockEntity2, notNullValue());
	assertThat(mockEntity3, notNullValue());
    }

    @Test
    public void verifySpiedDummyWasInsertedIntoMockEditingContext() throws Exception {
	verify(mockEditingContext).insertSavedObject(spiedDummy);
    }

    @Test
    public void verifySpiedNSArrayContainsInsertedObjects() throws Exception {
	verify(mockEditingContext).insertSavedObject(spiedDummies.get(0));
    }

    @Test
    public void verifySpiedNSArrayContainsSpiedObjects() throws Exception {
	try {
	    verify(spiedDummies.get(0), never()).toLongString();
	} catch (Throwable exception) {
	    fail("The object has not been spied as expected.");
	}

    }
}
