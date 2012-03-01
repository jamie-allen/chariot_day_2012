!SLIDE title-page

## Scala - How to Make Java Annoy You

Jamie Allen

Chariot Day 2012

!SLIDE transition=fade

# How coding in Scala makes me feel

<img src="snufflin.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade

# Not a Getting Started Talk
.notes That's boring as hell.  And you can read it in any one of a number of blog posts.  

* What does Scala give you?
* Simple examples of cool stuff you can do

!SLIDE transition=fade

# What are the major language features of Scala?
.notes Everything is an object. FP and TT go hand in hand.

* Object Oriented
* Functional Programming
	* Immutability
	* Referential Transparency
	* Higher order functions
* Type Theory
* Actors
* Java Interoperability
* Implicits
* Pattern Matching
* Type Inference
* Category Theory

!SLIDE transition=fade

# Object Oriented Features
.notes Laziness is good for reducing initial instantiation time, reducing initial footprint, resolve ordering issues. But there is a cost in a guard field and synchronization, ensuring it is created when necessary. By-name parameters are evaluated every time they are referenced within the method they are passed to.

* Default parameter values
    class Address(val State = "PA")
* Named parameters
    new Person(lastName = "Allen")
* By-name parameters
	def doSomething(t: => Long) = {
		println(t)
		println(t)
	}
* Lazy definition
	lazy val calculatedValue = (some big, expensive calcuation)

!SLIDE transition=fade

# Imports

* Can be anywhere in a class
* Allow for selecting multiple classes from a package
* Aliasing, to rename an imported type and avoid confusion
	import scala.collection.mutable.{Map => MMap}
	import scala.collection.immutable.Map

!SLIDE transition=fade

# Objects

* Singletons within a JVM, no private constructor histrionics
* Companion Objects, used for factories and constants

!SLIDE transition=fade

# Case Classes

* DTOs done right
* All class parameters are immutable
* Cannot be extended
* equals() and hashCode() with no annoyance

	case class Person(firstName: String, lastName: String)

!SLIDE transition=fade
# Tuples
.notes Binds you to an implementation, very fragile.  But useful.

* Great way to group values without a DTO
* How to return multiple values, but wrapped in a single object instance

!SLIDE transition=fade
# Functional Programming

* Very powerful programming paradigm
* Inverts imperative logic - apply your idempotent function to your data

!SLIDE transition=fade
# Immutability

* How many times have you been bitten by someone altering the contents of your collection?
* Extends beyond marking instances final, you must not leak mutability

!SLIDE transition=fade
# How inadvertent mutation makes me feel

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
# How I program

* Pre-Scala: Make it work, make it work well, make it work fast
* With Scala: Make it work AND work well, make it work fast

!SLIDE transition=fade

# First, Collections Classes

* Immutable and Mutable
* Rich implementations, extremely flexible
	* Map
	* Set
	* Sequence
	* List
	* Vector

!SLIDE transition=fade
# Rich higher order function support
.notes Applying lambdas to collections.  Function literals, function values.  Convert methods into functions. head/tail on linear sequences and lists.

	val numbers = 1 to 20 // Range(1, 2, 3, ... 20)

	numbers.map(_ + 1) // Vector(2, 3, 4, ... 21)
	numbers.filter(_ < 5) // Vector(1, 2, 3, 4)
	numbers.head // Int = 1
	numbers.tail // Range(2, 3, 4, ... 20)
	numbers.take(5) // Range(1, 2, 3, 4)
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
	
	val names = List("Jamie", "Al", "Steve")
	names map { _.toUpperCase } // List(JAMIE, AL, STEVE)
	names flatMap { _.toUpperCase } // List(J, A, M, I, E, A, L, S, T, E, V, E)


!SLIDE transition=fade
# Function types

* Function literals (lambdas & closures)
* Function types	

!SLIDE transition=fade
# Type Theory

!SLIDE transition=fade
# Type Inference
.notes Still a good idea to show types on public interfaces, though

* Local versus Global, as you would have in ML (see Daniel Spiewak's ETE 2011 talk)
* You don't have to specify a type when declaring a variable/value

!SLIDE transition=fade
# Cool type tricks
.notes path dependent is specifying an instance on which that type must exist. Projections are across all instances of that type

* Path dependent types 
    foo.Bar
* Type projections
    Foo#Bar
* Structural types

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
# Type lambdas 
.notes I want to establish a pattern here, wait until we get to functional programming

* Just like function lambdas
* Define a type dynamically in code without binding

!SLIDE transition=fade
# Actors

* Erlang/OTP
* Akka will replace language actors in Scala v2.10
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
* Exist in other languages, like C

!SLIDE transition=fade
# Implicits are Voodoo!

* Do not use them until you understand them
* Limit scope as much as possible (see Josh Suereth's NE Scala 2011 talk)
* Modern IDEs will warn you with an underline

<img src="double_facepalm.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# Pattern Matching

* One of my favorite Scala constructs
* Case statements on steroids
* Falls through to first complete match

!SLIDE transition=fade
# Many different ways to do it
.notes Note that variable and wildcard are in conflict here

    name match {
    	case "Al" => println("Found Al") // Constant
		case Person("Steve") => println("Found Steve") // Constructor
		case Seq("Al", "Steve") => println("Got Al before Steve") // Sequence
    	case ("Al", "Steve") => println("Got Al and Steve") // Tuple
		case x: Int if x > 5 => println("got a value greater than 5: " + x) // Type, guard
    	case x => println("Got something that wasn't an Int: " + x) // Variable
    	case _ => println("Not found") // Wildcard
    }

!SLIDE transition=fade
# Category Theory

* Makes many of us feel like idiots because we don't know what the eggheads are talking about
* Annoys the eggheads because they feel like "Java Joe" won't take the time to learn what they're doing

<img src="triple_facepalm.jpg" class="illustration" note="final slash needed"/>

!SLIDE transition=fade
# Category Theory
.notes Morphism is dim cat to glowing cat, functor would convert a cat to a dog, and a dim cats into dim dogs and glowing cats into glowing dogs, but also dim dogs into glowing dogs just like the cat because the morphism is preserved.

* Concepts (types) and Arrows (functions to convert concepts from one to another) 
* Functors are transformations from one category to another that can also transform/preserve morphisms
* Morphisms change one value in a category to another in the same category, from one type to another where types are the category

!SLIDE transition=fade
# Monads 

* Merely something that can be flattened (like flatMap)
* Combine functor applications

!SLIDE transition=fade
# Option/Either

* Option allows you to replace null with None, meaning you can ignore the value in your higher-order functions
* Either allows you to specify a unified type of either an error condition on the left or a correct value on the right

!SLIDE transition=fade
# Currying

* Currying is the conversion of a function of multiple parameters into a chain of functions that accept a single parameter. A curried function accepts one of its arguments an returns a function that acccepts the next argument. 
* Most common example you'll see in Scala is a fold over a collection
    val sum = numbers.foldLeft(0){ case (acc, currentVal) => acc + currentVal }

    sum: Ints = 210

!SLIDE transition=fade
# Is Scala too complex?
.notes You tell me.  I think not.  You can start by using Scala as a DSL for Java and make your code more concise, more readable and more correct.  As your abilities with the language grows, try expanding what you're doing, but keep in mind your limitations.

* Is the language trying to support too many paradigms at the expense of usability?
* Should a language be responsible for providing convention as well as capability?

!SLIDE transition=fade
# Flatmapping across data collections example

!SLIDE transition=fade
# Producer/Consumer Actors example

!SLIDE transition=fade
# Credits

* Scala in Depth, by Josh Suereth (MEAP)
* Wikipedia
* Runar Bjarnson's NE Scala 2011 talk
* Josh Suereth's NE Scala 2011 talk

http://nescala.org/2011/
