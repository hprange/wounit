WOUnit
======

The WOUnit framework contains a set of utilities for testing WebObjects
applications using JUnit 4.7 or later capabilities. This library can be
useful if you write unit/integration tests for Enterprise Objects or
employ the TDD technique on your projects.

**Version**: 1.0-beta-4

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
* **Easy to use**: No extension required for test classes. The WOUnit library make use of
the MethodRule approach provided by JUnit 4.7. It also load EOModels automatically.

Installation
------------

Maven users have to add the dependency declaration:

	<dependency>
		<groupId>com.wounit</groupId>
		<artifactId>wounit</artifactId>
		<version>1.0-beta-4</version>
	</dependecy>

Non Maven users have to:

1. Download the wounit.jar.
2. Add the wounit library to the build path.

Usage
-----

	import static com.wounit.matchers.EOAssert.*;

	public class TestMyEntity {
		@Rule
		public TemporaryEditingContext ec = new TemporaryEditingContext("MyModel");

		@Test
		public void cannotSaveFooIfBarIsNull() {
			Foo foo = Foo.createFoo(ec);

			foo.setBar(null);

			confirm(foo, cannotBeSavedBecause("The bar property cannot be null"));
		}
	}

About
-----

* **Site**: http://github.com/hprange/wounit
* **E-mail**: hprange at gmail.com
* **Twitter**: @hprange