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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.foundation.NSArray;
import com.wounit.model.FooEntity;
import com.wounit.model.FooEntityWithRequiredField;
import com.wounit.rules.MockEditingContext;

import er.extensions.foundation.ERXArrayUtilities;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUnderTest {
    @Rule
    public final MockEditingContext mockEditingContext = new MockEditingContext("Test");

    @UnderTest(size = 2)
    private NSArray<FooEntity> objectsUnderTest;

    @Spy
    @UnderTest
    private FooEntity spiedFoo;

    @UnderTest
    private FooEntity underTest1, underTest2;

    @UnderTest
    private FooEntityWithRequiredField underTest3;

    @Test
    public void verifyArrayContainsTheSpecifiedNumberOfObjectsUnderTest() throws Exception {
	assertThat(objectsUnderTest, notNullValue());
	assertThat(objectsUnderTest.size(), is(2));
    }

    @Test
    public void verifyDummyObjectsCreation() throws Exception {
	assertThat(underTest1, notNullValue());
	assertThat(underTest2, notNullValue());
	assertThat(underTest3, notNullValue());
    }

    @Test
    public void verifySpiedObjectHasBeenInserted() throws Exception {
	assertThat(ERXArrayUtilities.arrayBySelectingInstancesOfClass(mockEditingContext.insertedObjects(), FooEntity.class), hasItem(spiedFoo));
    }
}
