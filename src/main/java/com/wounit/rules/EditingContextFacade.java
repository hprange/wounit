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

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * This class represents a simple facade to create and insert enterprise
 * objects.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
abstract class EditingContextFacade {
    /**
     * The editing context to create and insert objects.
     */
    protected final EOEditingContext editingContext;

    /**
     * Create a facade to create and insert <code>EOEnterpriseObjects</code>.
     * 
     * @param editingContext
     *            the editing context where objects should be created/inserted.
     */
    public EditingContextFacade(EOEditingContext editingContext) {
	this.editingContext = editingContext;

    }

    /**
     * Create and insert a new enterprise object for the type specified.
     * 
     * @param type
     *            the type of the enterprise object to be created.
     * @return Returns the new enterprise object.
     */
    public abstract EOEnterpriseObject create(Class<? extends EOEnterpriseObject> type);

    /**
     * Insert a new enterprise object into the editing context.
     * 
     * @param object
     *            the object to be inserted into the editing context
     */
    public abstract void insert(EOEnterpriseObject object);
}
