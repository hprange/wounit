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
package com.wounit.rules;

import static com.wounit.rules.WOUnitTroubleshooter.Utils.extractModelName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.wounit.rules.WOUnitTroubleshooter;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWOUnitTroubleshooter {
    @Mock
    private PrintStream output;
    private PrintStream systemOutput;

    @Test
    public void doNotSuggestModelNameWhenNoGoodMatchFound() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("AAAABBBBBCCCCCDDDDDEEEE");

	verify(output, never()).print(Mockito.contains("Did you mean"));
    }

    @Test
    public void doNotSuggestWhenModelNameIsNull() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound(null);

	verify(output, never()).print(" Did you mean 'null'?");
    }

    @Test
    public void modelNameForFilePath() throws Exception {
	String result = extractModelName("/Users/user/Documents/workspace/project/Resources/Sample.eomodeld/index.eomodeld");

	assertThat(result, is("Sample"));
    }

    @Test
    public void modelNameForNSBundlePath() throws Exception {
	String result = extractModelName("Nonlocalized.lproj/Sample.eomodeld");

	assertThat(result, is("Sample"));
    }

    @Test
    public void modelNameIsNullWhenNoModelNameMatches() throws Exception {
	String result = extractModelName("Nonlocalized.lproj/Sample.xxx");

	assertThat(result, nullValue());
    }

    @Test
    public void printAvailableModelNames() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("xxx");

	verify(output).println(" Available models:");
	verify(output).println("  - AnotherTest");
	verify(output).println("  - Test");
	verify(output).println("  - erprototypes");
    }

    @Test
    public void printDiagnosesOnlyOnce() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("xxx");

	verify(output).print("A model named 'xxx' could not be found.");

	reset(output);

	WOUnitTroubleshooter.diagnoseModelNotFound("xxx");

	verifyNoMoreInteractions(output);
    }

    @Test
    public void printModelNotFoundMessage() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("xxx");

	verify(output).print("A model named 'xxx' could not be found.");
    }

    @Before
    public void setup() {
	systemOutput = System.out;

	System.setOut(output);

	WOUnitTroubleshooter.ALREADY_DIAGNOSED.set(false);
    }

    @Test
    public void suggestModelNameIgnoresCaseWhenFindingBestMatch() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("ERPROTOTYPES");

	verify(output).print(" Did you mean 'erprototypes'?");
    }

    @Test
    public void suggestModelNameWhenPossible() throws Exception {
	WOUnitTroubleshooter.diagnoseModelNotFound("Teste");

	verify(output).print(" Did you mean 'Test'?");
    }

    @After
    public void tearDown() {
	System.setOut(systemOutput);
    }
}
