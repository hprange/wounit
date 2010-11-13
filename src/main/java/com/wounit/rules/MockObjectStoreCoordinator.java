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

import er.extensions.eof.ERXObjectStoreCoordinator;

/**
 * <code>MockObjectStoreCoordinator</code> is a subclass of
 * <code>ERXObjectStoreCoordinator</code> that reimplements some methods in
 * order to avoid database access during the test execution.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
class MockObjectStoreCoordinator extends ERXObjectStoreCoordinator {
    /**
     * Reimplemented to do nothing.
     * 
     * @see com.webobjects.eocontrol.EOObjectStoreCoordinator#saveChangesInEditingContext(com.webobjects.eocontrol.EOEditingContext)
     */
    @Override
    public void saveChangesInEditingContext(EOEditingContext context) {
	// DO NOTHING
    }
}
