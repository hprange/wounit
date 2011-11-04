WOUnit
======

The WOUnit framework contains a set of utilities for testing WebObjects
applications using JUnit 4.7 or later capabilities. This library can be
useful if you write unit/integration tests for Enterprise Objects or
employ the TDD technique on your projects.

**Version**: 1.2

Requirements
------------

* [JUnit](http://www.junit.org/) 4.7 or later
* [Project Wonder](http://wiki.objectstyle.org/confluence/display/WONDER/Home) 5
* WebObjects 5

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
		<version>1.2</version>
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

Acknowledge
-----------

This project is an evolution of the original [WOUnitTest 2](http://wounittest.sourceforge.net/)
framework and is heavily inspired by it.

About
-----

* **Site**: http://hprange.github.com/wounit
* **E-mail**: hprange at gmail.com
* **Twitter**: @hprange