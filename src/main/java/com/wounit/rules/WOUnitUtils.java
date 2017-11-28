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

import java.util.Enumeration;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSet;

/**
 * This class was created to avoid a binary incompatibility between Wonder 6 and
 * 7 and make WOUnit compatible with both versions.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.2
 */
class WOUnitUtils {
    /**
     * Subtracts the contents of one array from another. The order of the array
     * should be preseved.
     *
     * @param main
     *            array to have values removed from it.
     * @param minus
     *            array of values to remove from the main array
     * @return array after performing subtraction.
     */
    static <T> NSArray<T> arrayMinusArray(NSArray<T> main, NSArray<?> minus) {
	NSSet<Object> minusSet = new NSSet<Object>(minus);
	NSMutableArray<T> mutableResult = new NSMutableArray<T>(main.count());
	Enumeration<T> e = main.objectEnumerator();

	while (e.hasMoreElements()) {
	    T obj = e.nextElement();

	    if (!minusSet.containsObject(obj)) {
		mutableResult.addObject(obj);
	    }
	}

	return mutableResult.immutableClone();
    }

    private WOUnitUtils() {
	// Cannot be instantiated
    }
}
