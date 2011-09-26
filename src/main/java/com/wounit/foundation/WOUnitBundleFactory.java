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
package com.wounit.foundation;

import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.development.NSBundleFactory;
import com.webobjects.foundation.development.NSLegacyBundle;
import com.webobjects.foundation.development.NSLegacyBundle.Factory;

/**
 * The <code>WOUnitBundleFactory</code> is a {@link NSBundleFactory} capable of
 * delegating the creation of bundles to the correct bundle factory (
 * {@link NSLegacyBundle.Factory}) according to the bundle path.
 * <p>
 * This class is not intended to be used outside of the context of testing.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.2
 */
public class WOUnitBundleFactory extends NSBundleFactory {
    private final NSBundleFactory factory;

    public WOUnitBundleFactory() {
	this(new NSLegacyBundle.Factory());
    }

    WOUnitBundleFactory(Factory factory) {
	this.factory = factory;
    }

    /**
     * Delegate the creation of the bundle if the path contains .framework or
     * .woa. Return <code>null</code> otherwise.
     * 
     * @see com.webobjects.foundation.development.NSBundleFactory#bundleForPath(java.lang.String,
     *      boolean, boolean)
     */
    @Override
    public NSBundle bundleForPath(String path, boolean shouldCreateBundle, boolean newIsJar) {
	if (!path.contains(".framework") && !path.contains(".woa")) {
	    return null;
	}

	return factory.bundleForPath(path, shouldCreateBundle, newIsJar);
    }
}
