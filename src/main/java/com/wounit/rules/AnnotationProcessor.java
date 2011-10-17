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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.wounit.exceptions.WOUnitException;

/**
 * This class can inspect fields of a target object and determine if an
 * annotation is present. It uses a factory to create enterprise objects when
 * desired annotation is available.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
class AnnotationProcessor {
    private static List<Field> getAllFields(Class<?> type) {
	List<Field> fields = new ArrayList<Field>();

	for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
	    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
	}

	return fields;
    }

    /**
     * All the fields declared for the target object's class.
     */
    private final List<Field> fields;

    /**
     * The target object to be examined.
     */
    private final Object target;

    /**
     * Create an annotation processor for the given target object.
     * 
     * @param target
     *            an object, usually a test case.
     */
    AnnotationProcessor(Object target) {
	this.target = target;
	this.fields = getAllFields(target.getClass());
    }

    private Object createObjectForType(Class<?> type, Field field, AbstractEnterpriseObjectFactory factory) {
	if (NSArray.class.isAssignableFrom(type)) {
	    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
	    type = (Class<?>) genericType.getActualTypeArguments()[0];

	    NSMutableArray<EOEnterpriseObject> objects = new NSMutableArray<EOEnterpriseObject>();

	    EOEnterpriseObject object = factory.create(type.asSubclass(EOEnterpriseObject.class));

	    objects.add(object);

	    return objects;
	}

	if (EOEnterpriseObject.class.isAssignableFrom(type)) {
	    return factory.create(type.asSubclass(EOEnterpriseObject.class));
	}

	return null;
    }

    /**
     * Examine the fields of this target object and create enterprise objects
     * for the fields which the given annotation is present.
     * 
     * @param annotation
     *            the annotation to search for.
     * @param factory
     *            a factory of enterprise objects.
     */
    void process(Class<? extends Annotation> annotation, AbstractEnterpriseObjectFactory factory) {
	for (Field field : fields) {
	    if (!field.isAnnotationPresent(annotation)) {
		continue;
	    }

	    Class<?> type = field.getType();

	    Object object = createObjectForType(type, field, factory);

	    if (object == null) {
		throw new WOUnitException("Cannot create object of type " + type.getName() + ".\n Only fields of type " + EOEnterpriseObject.class.getName() + " can be annotated with @" + annotation.getSimpleName() + ".");
	    }

	    field.setAccessible(true);

	    try {
		field.set(target, object);
	    } catch (Exception exception) {
		throw new WOUnitException("Something really wrong happened here. Probably a bug.\nPlease, report to http://github.com/hprange/wounit/issues.", exception);
	    }

	}
    }

}
