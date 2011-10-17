package com.wounit.stubs;

import com.webobjects.foundation.NSArray;
import com.wounit.annotations.Dummy;
import com.wounit.model.FooEntity;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class DummyArrayTestCase {
    @Dummy
    private NSArray<FooEntity> dummies;

    public NSArray<FooEntity> dummies() {
	return dummies;
    }
}
