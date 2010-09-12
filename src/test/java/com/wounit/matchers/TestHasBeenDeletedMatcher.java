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

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestHasBeenDeletedMatcher {
    private HasBeenDeletedMatcher<EOEnterpriseObject> matcher;

    private StringDescription mockDescription;

    @Mock
    private EOEditingContext mockEditingContext;

    @Mock
    private EOEnterpriseObject mockObject;

    @Test
    public void descriptionForHasBeenDeleted() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);

	Mockito.when(mockEditingContext.deletedObjects()).thenReturn(NSArray.<EOEnterpriseObject> emptyArray());

	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("deleted object\n     but got: an active object"));
    }

    @Test
    public void descriptionForHasNotBeenDeleted() throws Exception {
	matcher.matchesSafely(mockObject);
	matcher.describeTo(mockDescription);

	assertThat(mockDescription.toString(), is("deleted object\n     but got: a deleted object"));
    }

    @Test
    public void matchesHasBeenDeleted() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);

	NSArray<EOEnterpriseObject> deletedObjects = new NSArray<EOEnterpriseObject>(mockObject);

	Mockito.when(mockEditingContext.deletedObjects()).thenReturn(deletedObjects);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(true));
    }

    @Test
    public void matchesHasBeenDeletedWithNullEditingContext() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(null);

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(true));
    }

    @Test
    public void matchesHasNotBeenDeleted() throws Exception {
	Mockito.when(mockObject.editingContext()).thenReturn(mockEditingContext);

	Mockito.when(mockEditingContext.deletedObjects()).thenReturn(NSArray.<EOEnterpriseObject> emptyArray());

	boolean result = matcher.matchesSafely(mockObject);

	assertThat(result, is(false));
    }

    @Before
    public void setup() {
	matcher = new HasBeenDeletedMatcher<EOEnterpriseObject>();

	mockDescription = new StringDescription();
    }
}
