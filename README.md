[![Build Status](https://travis-ci.org/hprange/wounit.svg?branch=master)](https://travis-ci.org/hprange/wounit)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

WOUnit
======

The WOUnit framework contains a set of utilities for testing WebObjects
applications using JUnit 4.7 or later capabilities. This library can be
useful if you write unit/integration tests for Enterprise Objects or
employ the TDD technique on your projects.

**Version**: 1.3.1

Requirements
------------

* [JUnit](http://www.junit.org/) 4.7 or later
* [Project Wonder](http://wiki.objectstyle.org/confluence/display/WONDER/Home) 6 or later
* WebObjects 5
* Java 8

Features
--------

* **No Database Access Needed**: all the logic is handled in memory for fast unit testing
and integration testing.
* **Wonderful**: developed on top of Wonder classes, make possible the use of the augmented
transaction process specified by the ERXEnterpriseObject interface.
* **Easy to use**: no extension required for test classes. The WOUnit library makes use of
generics, annotations and the rule approach provided by JUnit 4.7.
* **Simple but not simpler**: only one line of code and you are ready to start writing tests.
The rules are responsible for loading eomodels, initializing and cleaning up before/after
test executions.

Installation
------------

Maven users have to add the dependency declaration:

	<dependency>
		<groupId>com.wounit</groupId>
		<artifactId>wounit</artifactId>
		<version>1.3.1</version>
	</dependency>

Non Maven users have to:

1. Download the wounit.jar.
2. Add the wounit library to the build path.

Usage
-----

	import static com.wounit.matchers.EOAssert.*;
	import com.wounit.rules.MockEditingContext;
	import com.wounit.annotations.Dummy;
    import com.wounit.annotations.UnderTest;

	public class MyEntityTest {
		@Rule
		public MockEditingContext ec = new MockEditingContext("MyModel");

		@Dummy
		private Bar dummyBar;

		@UnderTest
		private Foo foo;

		@Test
		public void cantSaveFooWithOnlyOneBar() {
			foo.addToBarRelationship(dummyBar);

			confirm(foo, cannotBeSavedBecause("Foo must have at least 2 bars related to it"));
		}
	}

OR

	import static com.wounit.matchers.EOAssert.*;
	import com.wounit.rules.TemporaryEditingContext;
	import com.wounit.annotations.UnderTest;

	public class MyEntityTest {
		@Rule
		public TemporaryEditingContext ec = new TemporaryEditingContext("MyModel");

		@UnderTest
		private Foo foo;

		@Test
		public void cannotSaveFooIfBarIsNull() {
			foo.setBar(null);

			confirm(foo, cannotBeSavedBecause("The bar property cannot be null"));
		}
	}

Building From Source
--------------------

### Building with Maven

WOUnit requires WebObjects and Wonder libraries that are not available in the Maven Central repository. Check steps 2 and 3 in the [Quick Start guide](http://wiki.wocommunity.org/display/WOL/Quick+Start) for more information about how to setup the WOCommunity repository and how to install WebObjects in the local Maven repository.

WOUnit can be built running the Maven command:

	mvn clean install

**Note**: WOUnit can only be successfully built if the WOCommunity repository is correctly configured.

### Building with Ant

WOUnit can be built running the Ant command:

	ant package

The required libraries will be automatically download to the `lib` folder in the WOUnit project root.

**Note**: WebObjects must be installed. If it isn't installed in the default location, a parameter can be used to define the path to the WebObjects libraries.

	ant package -Dwebobjects.lib=${path_to_webobjects_lib_folder}

### Importing into Eclipse

Maven users should install the [m2e](http://eclipse.org/m2e/) plug-in for Eclipse and use the _Import Maven Project_ option.

Ant users can add the libraries downloaded into the `lib` folder to the project's build path. The WebObjects libraries must be added manually.

Acknowledge
-----------

This project is an evolution of the original [WOUnitTest 2](http://wounittest.sourceforge.net/)
framework and is heavily inspired by it.

About
-----

* **Site**: http://hprange.github.com/wounit
* **E-mail**: hprange at gmail.com
* **Twitter**: @hprange