package com.wounit.matchers;

import com.wounit.rules.AbstractEditingContextRule;
import com.wounit.rules.TemporaryEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestEOAssertWithTemporaryEditingContext extends AbstractEOAssertTest {
    @Override
    protected AbstractEditingContextRule createEditingContext(String... modelNames) {
	return new TemporaryEditingContext(modelNames);
    }
}
