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

package com.wounit.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wounit.rules.MockEditingContext;
import com.wounit.rules.TemporaryEditingContext;

/**
 * This annotation allows shorthand creation of saved objects, minimizing
 * repetitive creation code. It must be used in companion of the
 * {@link MockEditingContext} class.
 * 
 * <pre>
 * public class FooTest {
 *     &#064;Dummy
 *     private Bar bar;
 * 
 *     &#064;Rule
 *     public MockEditingContext ec = new MockEditingContext(&quot;SampleModel&quot;);
 * 
 *     private Foo foo;
 * 
 *     &#064;Before
 *     public void setup() {
 * 	foo = Foo.createFoo(ec);
 * 
 * 	foo.setBar(bar);
 *     }
 * }
 * </pre>
 * 
 * The <code>@Dummy</code> annotation has no effect when used with the
 * {@link TemporaryEditingContext}.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Dummy {
}
