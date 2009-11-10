package lang

import org.junit.Test
import org.scalatest.junit.{JUnitSuite, AssertionsForJUnit, ShouldMatchersForJUnit}

/**
 * Test how scala class can be defined
 */

// @RunWith(classOf[JUnitRunner])
class ClassTest extends JUnitSuite with ShouldMatchersForJUnit 
{
  class EmptyClass { }

  // pojo with no constructor -- with braces
  // TODO figure out why default values are needed here
  class PojoClass {var x: Int = 1; var y: Int = 3; var s: String = "2"}

  // pojo with constructor -- with parenthesis
  class PojoCtorClass (private val x: Int, val y: Int, val s: String)

  // pojo with constructor -- same as PojoCtorClass
  class PojoCtorClass2 (val a: Int, val b: Int, val c: String)
  {
    private val x = a
    val y = b
    val s = c
  }

  class ParanPojoClass (val a: Int, val b: Int)

  def initialize()
  {
  	
  }
  /** instantiate classes */
  @Test
  def testInit()
  {
    val empty = new EmptyClass()

    val pojo1 = new PojoCtorClass(1, 2, "a")
    /**  missing 2.8 default arguments
    	val pojo2 = new PojoCtorClass(1, 3)
    	val pojo3 = new PojoCtorClass(1)
     */

    empty.getClass.getFields
    println("empty=" + empty)
    println("pojo1=" + pojo1)
    /** missing 2.8 default arguments 
	    println("pojo2=" + pojo2)
	    println("pojo3=" + pojo3)
	   */
  }

//  def get(obj: Any)
//  {
//    StringBuffer sb = new StringBuffer("[")
//
//    if(obj==null)
//      return "null";
//    else
//      for(field <- obj.getClass.getFields)
//      {
//        val f = field
//      }
//
//    sb append "]"
//
//  }
}