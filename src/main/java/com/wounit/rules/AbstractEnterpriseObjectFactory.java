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
 * This class represents a simple factory to create enterprise objects.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
abstract class AbstractEnterpriseObjectFactory {
    /**
     * The editing context to create and insert objects.
     */
    protected final EOEditingContext editingContext;

    /**
     * Create a factory for <code>EOEnterpriseObjects</code>.
     * 
     * @param editingContext
     *            the editing context where objects should be created.
     */
    public AbstractEnterpriseObjectFactory(EOEditingContext editingContext) {
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

}
