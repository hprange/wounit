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

import static com.wounit.matchers.EOAssert.confirm;
import static com.wounit.matchers.EOAssert.saveChanges;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Rule;
import org.junit.Test;

import com.wounit.annotations.UnderTest;
import com.wounit.model.JodaEntity;

public class TestJodaTimeSupport {
    @Rule
    public MockEditingContext editingContext = new MockEditingContext("Test");

    @UnderTest
    private JodaEntity entity;

    @Test
    public void useJodaTimeAttributesAndSaveChangesSuccessfully() throws Exception {
	entity.setDateTime(new DateTime());
	entity.setLocalDate(new LocalDate());
	entity.setLocalDateTime(new LocalDateTime());
	entity.setLocalTime(new LocalTime());

	confirm(editingContext, saveChanges());
    }
}
