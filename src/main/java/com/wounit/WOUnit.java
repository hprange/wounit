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
package com.wounit;

import java.util.ResourceBundle;

/**
 * This class provides utility methods to obtain information about the WOUnit
 * framework in runtime.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
public class WOUnit {
    /**
     * This method can be used to obtain the current version of JUnit.
     * 
     * @return Returns the current version of WOUnit library.
     */
    public static String version() {
	return ResourceBundle.getBundle("wounit").getString("wounit.version");
    }

    /**
     * <code>WOUnit</code> is not intended to be instantiated.
     */
    private WOUnit() {
    }
}
