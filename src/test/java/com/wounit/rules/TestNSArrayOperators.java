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

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;

import com.webobjects.foundation.NSArray;
import com.wounit.annotations.Dummy;
import com.wounit.model.ExtendedPrototypeEntity;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestNSArrayOperators {
    @Rule
    public MockEditingContext editingContext = new MockEditingContext("Test");

    @Dummy(size = 2)
    private NSArray<ExtendedPrototypeEntity> entities;

    @Test
    public void supportNSArrayOperators() throws Exception {
	entities.get(0).setExtendedAttribute(BigDecimal.TEN);
	entities.get(1).setExtendedAttribute(BigDecimal.ONE);

	@SuppressWarnings("unchecked")
	NSArray<BigDecimal> result = (NSArray<BigDecimal>) entities.valueForKeyPath("@flatten." + ExtendedPrototypeEntity.EXTENDED_ATTRIBUTE_KEY);

	assertThat(result, hasItems(BigDecimal.TEN, BigDecimal.ONE));
    }
}
