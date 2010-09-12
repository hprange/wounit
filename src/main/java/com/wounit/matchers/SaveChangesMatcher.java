package com.wounit.matchers;

import org.hamcrest.Description;

import com.webobjects.eocontrol.EOEditingContext;

/**
 * Tests if the <code>EOEditingContext</code> save changes successfully.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @param <T>
 *            a kind of <code>EOEditingContext</code>.
 */
class SaveChangesMatcher<T extends EOEditingContext> extends AbstractEnhancedTypeSafeMatcher<T> {
    public SaveChangesMatcher() {
	super();
    }

    public SaveChangesMatcher(String message) {
	super(message);
    }

    public void describeTo(Description description) {
	if (message != null) {
	    description.appendText("expecting exception other than \"");
	    description.appendText(message);
	    description.appendText("\" while saving the editing context\n     but got: ");

	    if (exception == null) {
		description.appendText("no exception and the editing context was successfully saved");

		return;
	    }

	    description.appendText(exception.getClass().getName());
	    description.appendText(": \"");
	    description.appendText(exception.getMessage());
	    description.appendText("\"");

	    return;
	}

	if (exception != null) {
	    description.appendText("successfully saved editing context\n     but got: ");
	    description.appendText(exception.getClass().getName());
	    description.appendText(": \"");
	    description.appendText(exception.getMessage());
	    description.appendText("\"");

	    return;
	}

	description.appendText("successfully saved editing context\n     but got: a successfully saved editing context");
    }

    @Override
    protected void matchesWithPossibleException(T editingContext) throws Exception {
	editingContext.saveChanges();
    }
}
