package com.wounit.stubs;

import org.mockito.Spy;

import com.wounit.annotations.Dummy;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class WrongTypeForSpiedObjectStubTestCase {
    @Spy
    @Dummy
    public String text = "text";
}
