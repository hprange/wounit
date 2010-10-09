package com.wounit.rules;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class AbstractEditingContextTest {
    protected static final String TEST_MODEL_NAME = "Test";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
}
