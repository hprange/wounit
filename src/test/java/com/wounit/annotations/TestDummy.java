package com.wounit.annotations;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.wounit.model.FooEntity;
import com.wounit.model.FooEntityWithRequiredField;
import com.wounit.rules.MockEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestDummy {
    @Rule
    public final MockEditingContext mockEditingContext = new MockEditingContext("Test");

    @Dummy
    private FooEntity mockEntity1, mockEntity2;

    @Dummy
    private FooEntityWithRequiredField mockEntity3;

    @Test
    public void verifyDummyObjectsCreation() throws Exception {
	assertThat(mockEntity1, notNullValue());
	assertThat(mockEntity2, notNullValue());
	assertThat(mockEntity3, notNullValue());
    }
}
