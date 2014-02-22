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
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.wounit.annotations.UnderTest;
import com.wounit.model.AugmentedEntity;
import com.wounit.model.BaseEntity;
import com.wounit.model.OrdinaryEntity;

import er.extensions.foundation.ERXProperties;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class TestPartialEntities {
    private static String UPDATE_INVERSE_RELATIONSHIPS;

    @BeforeClass
    public static void prepareProperties() {
	UPDATE_INVERSE_RELATIONSHIPS = ERXProperties.stringForKey("er.extensions.ERXEnterpriseObject.updateInverseRelationships");

	ERXProperties.setStringForKey("true", "er.extensions.ERXEnterpriseObject.updateInverseRelationships");
    }

    @AfterClass
    public static void revertProperties() {
	if (UPDATE_INVERSE_RELATIONSHIPS == null) {
	    ERXProperties.removeKey("er.extensions.ERXEnterpriseObject.updateInverseRelationships");
	} else {
	    ERXProperties.setStringForKey(UPDATE_INVERSE_RELATIONSHIPS, "er.extensions.ERXEnterpriseObject.updateInverseRelationships");
	}
    }

    @UnderTest
    private BaseEntity base;

    @Rule
    public MockEditingContext editingContext = new MockEditingContext("PartialsTest");

    @Test
    public void canCreateAndSavePartialEntity() throws Exception {
	confirm(base, canBeSaved());
    }

    @Test
    public void canCreatePartialForClass() throws Exception {
	base.partialForClass(AugmentedEntity.class);
    }

    @Test
    public void setPartialReverseRelationshipWhenSettingOrdinaryRelationship() throws Exception {
	OrdinaryEntity ordinary = OrdinaryEntity.createOrdinaryEntity(editingContext);

	ordinary.setRelationshipToPartialRelationship(base);

	AugmentedEntity augmented = base.partialForClass(AugmentedEntity.class);

	assertThat(augmented.relationshipToOrdinaries(), hasItem(ordinary));
    }
}
