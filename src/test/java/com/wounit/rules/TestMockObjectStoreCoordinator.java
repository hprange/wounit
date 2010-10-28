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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import com.webobjects.eocontrol.EOEditingContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestMockObjectStoreCoordinator {
    @Test
    public void doNothingOnSaveChangesInEditingContext() throws Exception {
	MockObjectStoreCoordinator store = new MockObjectStoreCoordinator();

	EOEditingContext mockEditingContext = mock(EOEditingContext.class);

	store.saveChangesInEditingContext(mockEditingContext);

	verifyZeroInteractions(mockEditingContext);
    }
}
