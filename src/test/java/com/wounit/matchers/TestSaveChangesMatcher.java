package com.wounit.matchers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSValidation;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSaveChangesMatcher {
    private SaveChangesMatcher<EOEditingContext> matcher;

    private Description mockDescription;

    @Mock
    private EOEditingContext mockEditingContext;

    @Test
    public void descriptionForSaveChangesFailure() throws Exception {
	Mockito.doThrow(new NSValidation.ValidationException("error"))
		.when(mockEditingContext).saveChanges();

	matcher.matchesSafely(mockEditingContext);
	matcher.describeTo(mockDescription);

	assertThat(
		mockDescription.toString(),
		is("successfully saved editing context\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"error\""));
    }

    @Test
    public void descriptionForSaveChangesSuccess() throws Exception {
	matcher.describeTo(mockDescription);

	assertThat(
		mockDescription.toString(),
		is("successfully saved editing context\n     but got: a successfully saved editing context"));
    }

    @Test
    public void descriptionForSaveChangesWithMessageFailure() throws Exception {
	matcher = new SaveChangesMatcher<EOEditingContext>("An expected error");

	Mockito.doThrow(
		new NSValidation.ValidationException("An unexpectedError"))
		.when(mockEditingContext).saveChanges();

	matcher.matchesSafely(mockEditingContext);
	matcher.describeTo(mockDescription);

	assertThat(
		mockDescription.toString(),
		is("expecting exception other than \"An expected error\" while saving the editing context\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"An unexpectedError\""));
    }

    @Test
    public void descriptionForSaveChangesWithMessageSuccess() throws Exception {
	matcher = new SaveChangesMatcher<EOEditingContext>("An expected error");

	matcher.describeTo(mockDescription);

	assertThat(
		mockDescription.toString(),
		is("expecting exception other than \"An expected error\" while saving the editing context\n     but got: no exception and the editing context was successfully saved"));
    }

    @Test
    public void matchesDoNotSaveChanges() throws Exception {
	Mockito.doNothing().when(mockEditingContext).saveChanges();

	boolean result = matcher.matchesSafely(mockEditingContext);

	assertThat(result, is(true));
    }

    @Test
    public void matchesDoNotSaveChangesWithMatchingMessage() throws Exception {
	matcher = new SaveChangesMatcher<EOEditingContext>("an expected error");

	Mockito.doThrow(new RuntimeException("an expected error"))
		.when(mockEditingContext).saveChanges();

	boolean result = matcher.matchesSafely(mockEditingContext);

	assertThat(result, is(false));
    }

    @Test
    public void matchesDoNotSaveChangesWithNoMatchingMessage() throws Exception {
	matcher = new SaveChangesMatcher<EOEditingContext>("an error");

	Mockito.doThrow(new RuntimeException("an unexpected error"))
		.when(mockEditingContext).saveChanges();

	boolean result = matcher.matchesSafely(mockEditingContext);

	assertThat(result, is(true));
    }

    @Test
    public void matchesSaveChanges() throws Exception {
	Mockito.doThrow(new RuntimeException("unexpected error"))
		.when(mockEditingContext).saveChanges();

	boolean result = matcher.matchesSafely(mockEditingContext);

	assertThat(result, is(false));
    }

    @Before
    public void setup() {
	matcher = new SaveChangesMatcher<EOEditingContext>();

	mockDescription = new StringDescription();
    }
}
