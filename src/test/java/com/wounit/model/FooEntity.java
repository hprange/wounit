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

package com.wounit.model;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;

@SuppressWarnings("serial")
public class FooEntity extends _FooEntity {
    private int awakeFromInsertionCount = 0;

    private boolean canBeDeleted = true;

    private boolean canBeSaved = true;

    private EOGlobalID globalIdDuringAwakeFromInsertion;

    @Override
    public void awakeFromInsertion(EOEditingContext editingContext) {
	super.awakeFromInsertion(editingContext);

	awakeFromInsertionCount++;

	globalIdDuringAwakeFromInsertion = editingContext.globalIDForObject(this);
    }

    public int awakeFromInsertionCount() {
	return awakeFromInsertionCount;
    }

    public EOGlobalID globalIdDuringAwakeFromInsertion() {
	return globalIdDuringAwakeFromInsertion;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
	this.canBeDeleted = canBeDeleted;
    }

    public void setCanBeSaved(boolean canBeSaved) {
	this.canBeSaved = canBeSaved;
    }

    @Override
    public void validateForDelete() throws ValidationException {
	super.validateForDelete();

	if (!canBeDeleted) {
	    throw new ValidationException("This foo object can't be deleted");
	}
    }

    @Override
    public void validateForSave() throws ValidationException {
	super.validateForSave();

	if (!canBeSaved) {
	    throw new ValidationException("This foo object can't be saved");
	}
    }
}
