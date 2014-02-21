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

import static com.wounit.matchers.EOAssert.canBeSaved;
import static com.wounit.matchers.EOAssert.confirm;

import org.junit.Rule;
import org.junit.Test;

import com.wounit.model.BaseEntity;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestPartialEntities {
    @Rule
    public MockEditingContext editingContext = new MockEditingContext("PartialsTest");

    @Test
    public void canCreateAndSavePartialEntity() throws Exception {
	BaseEntity base = BaseEntity.createBaseEntity(editingContext);

	confirm(base, canBeSaved());
    }
}
