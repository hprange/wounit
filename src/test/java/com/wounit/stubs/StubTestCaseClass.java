package com.wounit.stubs;

import com.wounit.annotations.Dummy;
import com.wounit.model.FooEntity;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class StubTestCaseClass {
    @Dummy
    private FooEntity foo;

    public FooEntity foo() {
	return foo;
    }
}
