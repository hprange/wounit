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

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStore;
import com.wounit.rules.MockEditingContext;

import er.extensions.eof.ERXEC;

/**
 * The <code>WOUnitEditingContextFactory</code> is a helper class created to
 * make testing easier. In some scenarios, programmers may not have access to
 * the creation of editing contexts. This can happen when the code under test
 * uses one of the <code>ERXEC.newEditingContext</code> factory methods. This
 * class extends the <code>ERXEC.DefaultFactory</code> class. It returns the
 * provided editing context for every call to
 * <code>ERXEC.newEditingContext</code> factory methods.
 * <p>
 * The <code>WOUnitEditingContextFactory</code> may be used with the
 * {@link MockEditingContext} like this:
 * 
 * <pre>
 * public MockEditingContext editingContext = new MockEditingContext();
 * 
 * &#064;Before
 * public void setup() {
 *     ERXEC.Factory factory = new WOUnitEditingContextFactory(editingContext);
 * 
 *     ERXEC.setFactory(factory);
 * }
 * </pre>
 * <p>
 * Any code that call one of the <code>ERXEC.newEditingContext</code> factory
 * methods after that will receive the mock editing context as a result.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.3
 * @see ERXEC.Factory
 */
public class WOUnitEditingContextFactory extends ERXEC.DefaultFactory {
    private final EOEditingContext editingContext;

    /**
     * Create an editing context factory that always returns the provided
     * editing context as a result.
     * 
     * @param editingContext
     *            An editing context
     */
    public WOUnitEditingContextFactory(EOEditingContext editingContext) {
	super();

	this.editingContext = editingContext;
    }

    @Override
    protected EOEditingContext _createEditingContext(EOObjectStore parent) {
	return editingContext;
    }
}
