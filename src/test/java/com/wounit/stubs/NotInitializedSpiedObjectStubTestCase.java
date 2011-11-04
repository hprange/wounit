package com.wounit.stubs;

import org.mockito.Spy;

import com.wounit.annotations.Dummy;
import com.wounit.model.FooEntity;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class NotInitializedSpiedObjectStubTestCase {
    @Spy
    @Dummy
    public FooEntity value = null;
}
