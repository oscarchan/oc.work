package lang

import junit.framework.Test
import org.scalatest.junit.{JUnitSuite, AssertionsForJUnit, ShouldMatchersForJUnit}

/**
 * Test how scala class can be defined
 */

// @RunWith(classOf[JUnitRunner])
class ClassTest extends JUnitSuite with ShouldMatchersForJUnit 
{
  class EmptyClass { }

  // pojo with no constructor -- with braces
  class PojoClass {val x: Int; val y: Int = 2; val s: String = "a"}

  // pojo with constructor -- with parenthesis
  class PojoCtorClass (private val x: Int, val y: Int = 2, val s: String ="a")

  // pojo with constructor -- same as PojoCtorClass
  class PojoCtorClass2 (val a: Int, val c: Int =2, val c: String ="a")
  {
    private val x = a
    val y = b
    val s = c
  }

  class ParanPojoClass (val a: Int, val b: Int)


  /** instantiate classes */
  @Test
  def testInit()
  {
    val empty = new EmptyClass()

    val pojo1 = new PojoCtorClass(1, 2, "a")
    val pojo2 = new PojoCtorClass(1, 3)
    val pojo3 = new PojoCtorClass(1)

    empty.getClass.getFields
    println("empty=" + empty)
    println("pojo1=" + pojo1)
    println("pojo2=" + pojo2)
    println("pojo3=" + pojo3)
  }

  def get(obj: Any)
  {
    StringBuffer sb = new StringBuffer("]");

    if(obj==null)
      return "null";
    else
      for(field <- obj.getClass.getFields)
      {
        val f = field

      }

    sb append "]"

  }
}