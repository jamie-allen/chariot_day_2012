!SLIDE title-page

## Scala - The Kitchen Sink in 45 Minutes

Jamie Allen

Chariot Day 2012

!SLIDE transition=fade
# How Coding in Scala Makes Me Feel

<img src="snufflin.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# How I Program

* Pre-Scala: Make it work, make it work well, make it work fast
* With Scala: Make it work AND work well, make it work fast

!SLIDE transition=fade
# Not a Getting Started Talk
.notes That's boring as hell.  And you can read it in any one of a number of blog posts.  

* What does Scala give you?
* Simple examples of cool stuff you can do

!SLIDE transition=fade
# Major Language Features of Scala

* Object Oriented
* Case Classes and Pattern Matching
* Functional Programming
* Type Theory
* Actors
* Java Interoperability
* Implicits
* Category Theory

!SLIDE transition=fade
# Object Oriented Features
.notes Everything is an object.

!SLIDE transition=fade
# Default Parameter Values

    class Address(val State = "PA")

!SLIDE transition=fade
# Named Parameters

    new Person(lastName = "Doe")

!SLIDE transition=fade
# By-Name Parameters
.notes By-name parameters are evaluated every time they are referenced within the method they are passed to.

	def doSomething(t: => Long) = {
		println(t)
		println(t)
	}

!SLIDE transition=fade
# Lazy Definition
.notes Laziness is good for reducing initial instantiation time, reducing initial footprint, resolve ordering issues. But there is a cost in a guard field and synchronization, ensuring it is created when necessary.

	lazy val calculatedValue = (some big, expensive calcuation)

!SLIDE transition=fade
# Imports
.notes Can be anywhere in a class, embedded in code, allow for selecting multiple classes from a package, aliasing

	import scala.collection.mutable.{Map => MMap}
	import scala.collection.immutable.Map

!SLIDE transition=fade
# Objects

* Singletons within a JVM, no private constructor histrionics
* Companion Objects, used for factories and constants

!SLIDE transition=fade
# Case Classes
.notes DTOs done right, all class parameters are immutable, cannot be extended, equals() and hashCode() with no annoyance

	case class Person(firstName: String, lastName: String)

!SLIDE transition=fade
# Tuples
.notes Binds you to an implementation, very fragile.  But useful.  Great way to group values without a DTO.  How to return multiple values, but wrapped in a single object instance.

	case class Person(name: String)
	val (num: Int, person: Person) = (1, Person("Phil"))

!SLIDE transition=fade
# Pattern Matching

* One of my favorite Scala constructs
* Case statements on steroids
* Falls through to first complete match

!SLIDE transition=fade
# Pattern Matching
.notes Note that variable and wildcard are in conflict here. Use more specific cases first

    name match {
    	case "Lisa" => println("Found Lisa") // Constant
		case Person("Bob") => println("Found Bob") // Constructor
		case "Karen" | "Michelle" => println("Found Karen or Michelle") // Or (?)
		case Seq("Dave", "John") => println("Got Dave before John") // Sequence
		case Seq("Dave", "John", _*) => println("Got Dave before John") // Sequence
    	case ("Susan", "Steve") => println("Got Susan and Steve") // Tuple
		case x: Int if x > 5 => println("got a value greater than 5: " + x) // Type, guard
    	case x => println("Got something that wasn't an Int: " + x) // Variable
    	case _ => println("Not found") // Wildcard
    }

!SLIDE transition=fade
# Exception Handling
.notes Again, use more specific cases first in the match

    try { 
        // ... 
    } catch {
        case iae: IllegalArgumentException => ... 
        case e: Exception => ...
    }

!SLIDE transition=fade
# Functional Programming

* Very powerful programming paradigm
* Inverts imperative logic - apply your idempotent function to your data

!SLIDE transition=fade
# Immutability

* How many times have you been bitten by someone altering the contents of your collection?
* Extends beyond marking instances final, you must not leak mutability

!SLIDE transition=fade
# How Inadvertent Mutation Makes Me Feel

<img src="wtf_husky.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# Referential Transparency

* An expression is transparent if it can be replaced by its VALUE without changing the behavior of the program
* In math, all functions are referentially transparent

!SLIDE transition=fade
# Referentially Transparent

	val example1 = "jamie".reverse
	val example2 = example1.reverse
	println(example1 + example2) 

	eimajjamie

!SLIDE transition=fade
# Referentially Opaque

	val example1 = new StringBuffer("Jamie").reverse
	val example2 = example1.reverse
	println(example1 append example2) 

	jamiejamie

!SLIDE transition=fade
# Collections

* Immutable and Mutable
* Rich implementations, extremely flexible
	* Map
	* Set
	* Sequence
	* List
	* Vector

!SLIDE transition=fade
# Higher Order Functions
.notes Applying lambdas to collections.  Function literals, function values.  Convert methods into functions. head/tail on linear sequences and lists.

	val numbers = 1 to 20 // Range(1, 2, 3, ... 20)

	numbers.map(_ + 1) // Vector(2, 3, 4, ... 21)
	numbers.filter(_ < 5) // Vector(1, 2, 3, 4)
	numbers.head // Int = 1
	numbers.tail // Range(2, 3, 4, ... 20)
	numbers.take(5) // Range(1, 2, 3, 4, 5)
	numbers.drop(5) // Range(6, 7, 8, ... 20)

!SLIDE transition=fade
# groupBy
	
	numbers.groupBy(_ % 3)

	Map(
		1 -> Vector(1, 4, 7, 10, 13, 16, 19), 
		2 -> Vector(2, 5, 8, 11, 14, 17, 20), 
		0 -> Vector(3, 6, 9, 12, 15, 18)
	)

!SLIDE transition=fade
# flatMap
	
	val names = List("Barb", "Phil", "Pat")
	names map { _.toUpperCase } // List(BARB, PHIL, PAT)
	names flatMap { _.toUpperCase } // List(B, A, R, B, P, H, I, L, P, A, T)

!SLIDE transition=fade
# fold

    val sum = numbers.foldLeft(0){ case (acc, currentVal) => acc + currentVal }

    sum: Ints = 210

!SLIDE transition=fade
# Currying
.notes Take a function that takes 2 parameters (Product), and curry it to create a new function that only takes one parameter (Doubler).  We "fix" a value and use it to apply a specific implementation of a product with semantic value.  Functions are automatically curry-able in ML and Haskell, but have to be defined explicitly as such in Scala.  Note the _ is what explicitly marks this as curried.

	def product(i: Int)(j: Int) = i * j // product: (i: Int)(j: Int)Int
	val doubler = product(2)_ // doubler: Int => Int = <function1>
	doubler(3) // Int = 6
	doubler(4) // Int = 8

	val tripler = product(3)_ // tripler: Int => Int = <function1>
	tripler(4) // Int = 12
	tripler(5) // Int = 15

!SLIDE transition=fade
# Function Types
.notes lambdas & closures defined without binding to a definition

* Function literals
* Function types	

!SLIDE transition=fade
# Type Theory

!SLIDE transition=fade
# Type Inference
.notes Still a good idea to show types on public interfaces, though

* You don't have to specify a type when declaring a variable/value
* You don't have to specify return types of methods/functions
* Local versus Global, as you would have in ML (see Daniel Spiewak's ETE 2011 talk)

!SLIDE transition=fade
# Cool Type Tricks
.notes path dependent is specifying an instance on which that type must exist. Projections are across all instances of that type

	// Path dependent types 
    foo.Bar

	// Type projections
    Foo#Bar 

    // Structural types
	def loadProperties(c:{ def getProperties():Properties })

!SLIDE transition=fade
# Type Constraints
.notes Covariance is substitute a type with its parent type - expecting mammal will take cat.  Contravariance is substituting a type with its child type - expecting cat will take mammal.  A Function object in Scala is covariant on return type and contravariant on argument type.	  

* Upper bounds (supertyping restrictions)
* Lower bounds (subtyping restrictions)
* Covariance and Contravariance

!SLIDE transition=fade
# Higher Kinded Types 
.notes Like higher order functions from earlier took another function to make a new function, use types to create new types

* Use other types to construct a new type
* Also called type constructors 

!SLIDE transition=fade
# Type Lambdas 
.notes I want to establish a pattern here, wait until we get to functional programming

* Just like function lambdas
* Define a type dynamically in code without binding

!SLIDE transition=fade
# Actors

* Erlang/OTP
* Akka will replace language actors
* Concurrency paradigm using networks of independent objects that can (should) only communicate via messaging
* Example later

!SLIDE transition=fade
# Java Interoperability

* Pretty easy to call Java from Scala
* Much more difficult to call Scala from Java, due to how you reference idiomatic concepts
* If you must access Scala from Java, use an abstraction layer like OSGi eventing or a message queue

!SLIDE transition=fade
# Implicits

* At compilation time, looks for definitions that will fix type incompatibility
* Exist in other languages, like C type coercion

!SLIDE transition=fade
# Implicits Are Voodoo!

* Do not use them until you understand them
* Limit scope as much as possible (see Josh Suereth's NE Scala 2011 talk)
* Modern IDEs will warn you with an underline

<img src="double_facepalm.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# Category Theory

* Makes many of us feel like idiots because we don't know what the eggheads are talking about
* Annoys the eggheads because they feel like "Java Joe" won't take the time to learn what they're doing

<img src="triple_facepalm.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# Category Theory
.notes Morphism is chewy brownie to a hard brownie, functor would convert a brownie to a cookie, and a chewy brownie to a chewy cookie and hard brownie into hard cookie, but also chewy cookies into hard cookies just like the brownie because the morphism is preserved.

* Concepts (types) and Arrows (functions to convert concepts from one to another) 
* Morphisms change one value in a category to another in the same category, from one type to another where types are the category
* Functors are transformations from one category to another that can also transform/preserve morphisms

(From Josh Suereth's Scala in Depth)

!SLIDE transition=fade
# Monads 
.notes like a collection with flatMap. you won't know what they are by looking at code at first. Monads are ephemeral - they have to meet the laws of monads.

* Just something that can be flattened 
* Combine functor applications

!SLIDE transition=fade
# Option/Either
.notes No more NPEs

* Option allows you to replace null with None, meaning you can ignore the value in your higher-order functions
* Either is a unified type of either an error condition on the left or a correct value on the right

!SLIDE transition=fade
# Is Scala too complex?
.notes You tell me.  I think not.  You can start by using Scala as a DSL for Java and make your code more concise, more readable and more correct.  As your abilities with the language grows, try expanding what you're doing, but keep in mind your limitations.

* Is the language trying to support too many paradigms at the expense of usability?
* Should a language be responsible for providing convention as well as capability?

!SLIDE transition=fade
# Loading Data to Be Processed
.notes 

	object DataProcessor {
	  import scala.collection.mutable.{MultiMap, HashMap => MMap, Set => MSet}
	  import scala.io.Source
	  def getMapFromFile(fileName: String, keyIndex: Int, valueIndex: Int): MMap[String, MSet[String]] = {
	    val result = new MMap[String, MSet[String]]() with MultiMap[String, String]
	    for {line <- Source.fromFile(fileName).getLines.drop(1)
	        val lineTokens = line.split(",")}
	      result.addBinding(lineTokens(keyIndex), lineTokens(valueIndex))
	    result
	  }
	}

!SLIDE transition=fade
# Flatmapping Across Data Collections Example

	  val macsByAccountNumber = getMapFromFile("Accounts-MAC.csv"), 0, 3)
	  val rateCodesByMac = getMapFromFile("MAC-RCs.csv", 0, 1)
	  val bsgHandlesByRateCode = getMapFromFile("RC-BSG.csv", 5, 6)
	  val sourceIdByBsgHandle = getMapFromFile("BSG-SourceId.csv", 2, 4)

	  val sourceIdsByAcctAndMac = new MMap[(String, String), MSet[String]]() with MultiMap[(String, String), String]
	  for {
	    acc <- macsByAccountNumber.keys
	  	mac <- macsByAccountNumber.getOrElse(acc, MSet().empty)
	  	rc <- rateCodesByMac.get(mac).flatten
	  	bsg <- bsgHandlesByRateCode.get(rc).flatten
	  	src <- sourceIdByBsgHandle.get(bsg).flatten
      } yield sourceIdsByAcctAndMac.addBinding((acc, mac), src)
	}

!SLIDE transition=fade
# Producer/Consumer Actors example

!SLIDE transition=fade
# Credits

* Sources
	* Fast Track to Scala courseware by Typesafe
	* Scala in Depth, by Josh Suereth (MEAP)
	* Wikipedia
	* Runar Bjarnson's NE Scala 2011 talk
	* Josh Suereth's NE Scala 2011 talk

* Contributors
	* Dave Esterkin

http://nescala.org/2011/
