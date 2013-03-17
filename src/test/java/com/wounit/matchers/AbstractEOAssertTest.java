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

import static com.wounit.matchers.EOAssert.canBeDeleted;
import static com.wounit.matchers.EOAssert.canBeSaved;
import static com.wounit.matchers.EOAssert.cannotBeDeleted;
import static com.wounit.matchers.EOAssert.cannotBeDeletedBecause;
import static com.wounit.matchers.EOAssert.cannotBeSaved;
import static com.wounit.matchers.EOAssert.cannotBeSavedBecause;
import static com.wounit.matchers.EOAssert.confirm;
import static com.wounit.matchers.EOAssert.doNotSaveChanges;
import static com.wounit.matchers.EOAssert.doNotSaveChangesBecause;
import static com.wounit.matchers.EOAssert.hasBeenDeleted;
import static com.wounit.matchers.EOAssert.hasBeenSaved;
import static com.wounit.matchers.EOAssert.hasNotBeenDeleted;
import static com.wounit.matchers.EOAssert.hasNotBeenSaved;
import static com.wounit.matchers.EOAssert.saveChanges;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.wounit.annotations.UnderTest;
import com.wounit.model.FooEntity;
import com.wounit.rules.AbstractEditingContextRule;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractEOAssertTest<T extends AbstractEditingContextRule> {
    @Rule
    public final T editingContext = createEditingContext("Test");

    @UnderTest
    private FooEntity foo;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void canBeDeletedFailure() throws Exception {
	foo.setCanBeDeleted(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: valid for delete enterprise object\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be deleted\""));

	confirm(foo, canBeDeleted());
    }

    @Test
    public void canBeDeletedSuccess() throws Exception {
	foo.setCanBeDeleted(true);

	confirm(foo, canBeDeleted());
    }

    @Test
    public void canBeSavedFailure() throws Exception {
	foo.setCanBeSaved(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: valid for save enterprise object\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be saved\""));

	confirm(foo, canBeSaved());
    }

    @Test
    public void canBeSavedSuccess() throws Exception {
	foo.setCanBeSaved(true);

	confirm(foo, canBeSaved());
    }

    @Test
    public void cannotBeDeletedFailure() throws Exception {
	foo.setCanBeDeleted(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not valid for delete enterprise object\n     but got: a valid one"));

	confirm(foo, cannotBeDeleted());
    }

    @Test
    public void cannotBeDeletedSuccess() throws Exception {
	foo.setCanBeDeleted(false);

	confirm(foo, cannotBeDeleted());
    }

    @Test
    public void cannotBeDeletedWithMessageFailure() throws Exception {
	foo.setCanBeDeleted(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"This foo object can't be deleted\"\n     but got: no validation exception"));

	confirm(foo, cannotBeDeletedBecause("This foo object can't be deleted"));
    }

    @Test
    public void cannotBeDeletedWithMessageSuccess() throws Exception {
	foo.setCanBeDeleted(false);

	confirm(foo, cannotBeDeletedBecause("This foo object can't be deleted"));
    }

    @Test
    public void cannotBeDeletedWithWrongCauseFailure() throws Exception {
	foo.setCanBeDeleted(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"The wrong exception\"\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be deleted\""));

	confirm(foo, cannotBeDeletedBecause("The wrong exception"));
    }

    @Test
    public void cannotBeSavedFailure() throws Exception {
	foo.setCanBeSaved(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not valid for save enterprise object\n     but got: a valid one"));

	confirm(foo, cannotBeSaved());
    }

    @Test
    public void cannotBeSavedSuccess() throws Exception {
	foo.setCanBeSaved(false);

	confirm(foo, cannotBeSaved());
    }

    @Test
    public void cannotBeSavedWithMessageFailure() throws Exception {
	foo.setCanBeSaved(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"This foo object can't be saved\"\n     but got: no validation exception"));

	confirm(foo, cannotBeSavedBecause("This foo object can't be saved"));
    }

    @Test
    public void cannotBeSavedWithMessageSuccess() throws Exception {
	foo.setCanBeSaved(false);

	confirm(foo, cannotBeSavedBecause("This foo object can't be saved"));
    }

    @Test
    public void cannotBeSavedWithWrongCauseFailure() throws Exception {
	foo.setCanBeSaved(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"The wrong exception\"\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be saved\""));

	confirm(foo, cannotBeSavedBecause("The wrong exception"));
    }

    protected abstract T createEditingContext(String... modelNames);

    @Test
    public void doNotSaveChangesFailure() throws Exception {
	foo.setCanBeSaved(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not successfully saved editing context\n     but got: a successfully saved editing context"));

	confirm(editingContext, doNotSaveChanges());
    }

    @Test
    public void doNotSaveChangesSuccess() throws Exception {
	foo.setCanBeSaved(false);

	confirm(editingContext, doNotSaveChanges());
    }

    @Test
    public void doNotSaveChangesWithMessageFailure() throws Exception {
	foo.setCanBeSaved(true);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"This foo object can't be saved\" while saving the editing context\n     but got: no exception and the editing context was successfully saved"));

	confirm(editingContext, doNotSaveChangesBecause("This foo object can't be saved"));
    }

    @Test
    public void doNotSaveChangesWithMessageSuccess() throws Exception {
	foo.setCanBeSaved(false);

	confirm(editingContext, doNotSaveChangesBecause("This foo object can't be saved"));
    }

    @Test
    public void doNotSaveWithWrongCauseFailure() throws Exception {
	foo.setCanBeSaved(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not expecting exception other than \"The wrong exception\" while saving the editing context\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be saved\""));

	confirm(editingContext, doNotSaveChangesBecause("The wrong exception"));
    }

    @Test
    public void hasBeenDeletedFailure() throws Exception {
	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: deleted object\n     but got: an active object"));

	confirm(foo, hasBeenDeleted());
    }

    @Test
    public void hasBeenDeletedSuccessAfterSaveChanges() throws Exception {
	editingContext.deleteObject(foo);
	editingContext.saveChanges();

	confirm(foo, hasBeenDeleted());
    }

    @Test
    public void hasBeenDeletedSucessBeforeSaveChanges() throws Exception {
	editingContext.deleteObject(foo);

	confirm(foo, hasBeenDeleted());
    }

    @Test
    public void hasBeenSavedFailure() throws Exception {
	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: saved object\n     but got: an object with unsaved changes"));

	confirm(foo, hasBeenSaved());
    }

    @Test
    public void hasBeenSavedSuccess() throws Exception {
	editingContext.saveChanges();

	confirm(foo, hasBeenSaved());
    }

    @Test
    public void hasNotBeenDeletedFailure() throws Exception {
	editingContext.deleteObject(foo);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not deleted object\n     but got: a deleted object"));

	confirm(foo, hasNotBeenDeleted());
    }

    @Test
    public void hasNotBeenDeletedSuccess() throws Exception {
	confirm(foo, hasNotBeenDeleted());
    }

    @Test
    public void hasNotBeenSavedFailure() throws Exception {
	editingContext.saveChanges();

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: not saved object\n     but got: an object with saved changes"));

	confirm(foo, hasNotBeenSaved());
    }

    @Test
    public void hasNotBeenSavedSuccess() throws Exception {
	confirm(foo, hasNotBeenSaved());
    }

    @Test
    public void hasUnsavedChangesAfterSaveChanges() throws Exception {
	editingContext.saveChanges();

	foo.setBar("test");

	confirm(foo, hasNotBeenSaved());
    }

    @Test
    public void saveChagesFailure() throws Exception {
	foo.setCanBeSaved(false);

	thrown.expect(AssertionError.class);
	thrown.expectMessage(is("\nExpected: successfully saved editing context\n     but got: com.webobjects.foundation.NSValidation$ValidationException: \"This foo object can't be saved\""));

	confirm(editingContext, saveChanges());
    }

    @Test
    public void saveChagesSuccess() throws Exception {
	foo.setCanBeSaved(true);

	confirm(editingContext, saveChanges());
    }

    @Before
    public void setup() {
	thrown.handleAssertionErrors();
    }
}
