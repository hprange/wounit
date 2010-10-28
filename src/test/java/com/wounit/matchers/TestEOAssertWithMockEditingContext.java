package com.wounit.matchers;

import com.wounit.rules.AbstractEditingContextRule;
import com.wounit.rules.MockEditingContext;

public class TestEOAssertWithMockEditingContext extends AbstractEOAssertTest {

    @Override
    protected AbstractEditingContextRule createEditingContext(String... modelNames) {
	return new MockEditingContext(modelNames);
    }
}
