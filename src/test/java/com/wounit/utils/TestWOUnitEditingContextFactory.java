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
package com.wounit.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.webobjects.eocontrol.EOEditingContext;
import com.wounit.rules.MockEditingContext;
import com.wounit.utils.WOUnitEditingContextFactory;

import er.extensions.eof.ERXEC;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestWOUnitEditingContextFactory {
    @Rule
    public MockEditingContext editingContext = new MockEditingContext();

    @Test
    public void returnEditingContextRuleWhenUsingWOUnitEditingContextFactory() throws Exception {
	WOUnitEditingContextFactory factory = new WOUnitEditingContextFactory(editingContext);

	ERXEC.setFactory(factory);

	EOEditingContext result = ERXEC.newEditingContext();

	assertThat(result, is((EOEditingContext) editingContext));
    }
}
