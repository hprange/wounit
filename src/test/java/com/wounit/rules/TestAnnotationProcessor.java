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
package com.wounit.rules;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.wounit.annotations.Dummy;
import com.wounit.exceptions.WOUnitException;
import com.wounit.model.FooEntity;
import com.wounit.stubs.ChildStubTestCase;
import com.wounit.stubs.DummyArrayStubTestCase;
import com.wounit.stubs.RawArrayDeclarationForDummyStubTest;
import com.wounit.stubs.StubTestCase;
import com.wounit.stubs.WrongGenericTypeForDummyStubTestCase;
import com.wounit.stubs.WrongTypeForDummyStubTestCase;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestAnnotationProcessor {
    @Mock
    private AbstractEnterpriseObjectFactory mockFactory;

    @Mock
    private FooEntity mockFoo;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void createArrayOfDummiesBasedOnTheGenericType() throws Exception {
	DummyArrayStubTestCase mockTarget = new DummyArrayStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.arrayOfOneDummy().get(0), instanceOf(FooEntity.class));
    }

    @Test
    public void createArrayOfDummiesIfAnnotationAndSizePresent() throws Exception {
	DummyArrayStubTestCase mockTarget = new DummyArrayStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.arrayOfTwoDummies().size(), is(2));
    }

    @Test
    public void createArrayOfOneDummyIfAnnotationPresentButNoSize() throws Exception {
	DummyArrayStubTestCase mockTarget = new DummyArrayStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.arrayOfOneDummy(), notNullValue());
	assertThat(mockTarget.arrayOfOneDummy().size(), is(1));
    }

    @Test
    public void createObjectForInheritedFieldIfAnnotationPresent() throws Exception {
	ChildStubTestCase mockTarget = new ChildStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.foo(), is(mockFoo));
    }

    @Test
    public void createObjectIfAnnotationPresent() throws Exception {
	StubTestCase mockTarget = new StubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.foo(), is(mockFoo));
    }

    @Test
    public void doNotCreateObjectIfAnnotationIsAbsent() throws Exception {
	StubTestCase mockTarget = new StubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	processor.process(Dummy.class, mockFactory);

	assertThat(mockTarget.objectUnderTest(), nullValue());
    }

    @Test
    public void exceptionIfAnnotatedGenericTypeIsIncompatible() throws Exception {
	WrongGenericTypeForDummyStubTestCase mockTarget = new WrongGenericTypeForDummyStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	thrown.expect(WOUnitException.class);
	thrown.expectMessage(is("Cannot create object of type java.lang.String.\n Only fields and arrays of type com.webobjects.eocontrol.EOEnterpriseObject can be annotated with @Dummy."));

	processor.process(Dummy.class, mockFactory);
    }

    @Test
    public void exceptionIfAnnotatedTypeIsIncompatible() throws Exception {
	WrongTypeForDummyStubTestCase mockTarget = new WrongTypeForDummyStubTestCase();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	thrown.expect(WOUnitException.class);
	thrown.expectMessage(is("Cannot create object of type java.lang.String.\n Only fields and arrays of type com.webobjects.eocontrol.EOEnterpriseObject can be annotated with @Dummy."));

	processor.process(Dummy.class, mockFactory);
    }

    @Test
    public void exceptionIfRawArrayAnnotated() throws Exception {
	RawArrayDeclarationForDummyStubTest mockTarget = new RawArrayDeclarationForDummyStubTest();

	AnnotationProcessor processor = new AnnotationProcessor(mockTarget);

	thrown.expect(WOUnitException.class);
	thrown.expectMessage(is("Cannot create object for a raw type com.webobjects.foundation.NSArray. Please, provide a generic type."));

	processor.process(Dummy.class, mockFactory);
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
	when(mockFactory.create(Mockito.any(Class.class))).thenReturn(mockFoo);
    }
}
