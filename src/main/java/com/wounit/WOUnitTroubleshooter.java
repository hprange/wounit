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

import static com.wounit.WOUnitTroubleshooter.Utils.findAvailableModels;
import static com.wounit.WOUnitTroubleshooter.Utils.findSimilarModelName;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSMutableArray;

/**
 * This class provides utility methods to help diagnose problems when running
 * WOUnit tests. This class is intended for internal use.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.3
 */
public class WOUnitTroubleshooter {
    protected static class Utils {
	protected static String extractModelName(String path) {
	    Pattern pattern = Pattern.compile("(.*?)\\.eomodeld");
	    Matcher matcher = pattern.matcher(path);

	    if (matcher.find()) {
		String match = matcher.group(1);

		return match.substring(match.lastIndexOf("/") + 1);
	    }

	    return null;
	}

	protected static Set<String> findAvailableModels() {
	    List<String> paths = new ArrayList<String>();

	    try {
		ClassLoader classloader = WOUnitTroubleshooter.class.getClassLoader();

		URL[] urls = ((URLClassLoader) classloader).getURLs();

		for (URL url : urls) {
		    if (url.getPath().endsWith(".jar")) {
			JarFile jar = new JarFile(url.getPath());

			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
			    String name = entries.nextElement().getName();

			    if (name.contains("eomodeld/index.eomodeld")) {
				paths.add(name);
			    }
			}
		    } else {
			File file = new File(url.toURI());

			paths.addAll(findModelsRecursively(file));
		    }
		}

		File resourcesFolder = new File(WOUnitTroubleshooter.class.getResource("/").toURI());

		paths.addAll(findModelsRecursively(resourcesFolder));
	    } catch (Exception exception) {
		// Ignore the exception and keep trying to diagnose
	    }

	    NSMutableArray<NSBundle> bundles = new NSMutableArray<NSBundle>(NSBundle.frameworkBundles());

	    bundles.add(NSBundle.mainBundle());

	    for (NSBundle bundle : bundles) {
		NSArray<String> modelPaths = bundle.resourcePathsForDirectories("eomodeld", null);

		for (String modelPath : modelPaths) {
		    paths.add(modelPath);
		}
	    }

	    Set<String> modelNames = new TreeSet<String>();

	    for (String path : paths) {
		modelNames.add(extractModelName(path));
	    }

	    return modelNames;
	}

	protected static List<String> findModelsRecursively(File base) {
	    if (base.isDirectory()) {
		File[] files = base.listFiles();

		List<String> models = new ArrayList<String>();

		for (File file : files) {
		    models.addAll(findModelsRecursively(file));
		}

		return models;
	    }

	    if (base.getAbsolutePath().contains("eomodeld/index.eomodeld")) {
		return Arrays.asList(new String[] { base.getAbsolutePath() });
	    }

	    return Collections.emptyList();
	}

	protected static String findSimilarModelName(Set<String> names, String wrongName) {
	    String bestMatch = null;
	    int bestDistance = 5;

	    for (String name : names) {
		int distance = levenshteinDistance(name.toLowerCase(), wrongName.toLowerCase(), 5);

		if (distance <= bestDistance) {
		    bestMatch = name;
		    bestDistance = distance;
		}
	    }

	    return bestMatch;
	}

	protected static int levenshteinDistance(CharSequence x, CharSequence y, int limit) {
	    return levenshteinDistance(x, y, limit, 0);
	}

	private static int levenshteinDistance(CharSequence x, CharSequence y, int limit, int accumulator) {
	    if (accumulator > limit) {
		return accumulator;
	    }

	    int n = x.length();
	    int m = y.length();

	    if (n == 0) {
		return accumulator + m;
	    }

	    if (m == 0) {
		return accumulator + n;
	    }

	    int cost = 0;

	    if (x.charAt(n - 1) != y.charAt(m - 1)) {
		cost = 1;
	    }

	    return Math.min(levenshteinDistance(x.subSequence(0, n - 1), y, limit, accumulator + 1), Math.min(levenshteinDistance(x, y.subSequence(0, m - 1), limit, accumulator + 1), levenshteinDistance(x.subSequence(0, n - 1), y.subSequence(0, m - 1), limit, accumulator + cost)));
	}
    }

    static final AtomicBoolean ALREADY_DIAGNOSED = new AtomicBoolean(false);

    /**
     * Diagnose eomodel not found errors.
     * 
     * @param modelName
     *            the name of the model not found
     */
    public static void diagnoseModelNotFound(String modelName) {
	if (!ALREADY_DIAGNOSED.compareAndSet(false, true)) {
	    return;
	}

	Set<String> models = findAvailableModels();

	System.out.print(String.format("A model named '%s' could not be found.", modelName));

	if (modelName != null) {
	    String suggestedName = findSimilarModelName(models, modelName);

	    if (suggestedName != null) {
		System.out.print(String.format(" Did you mean '%s'?", suggestedName));
	    }
	}

	System.out.println(" Available models:");

	for (String model : models) {
	    System.out.println(String.format("  - %s", model));
	}
    }
}
