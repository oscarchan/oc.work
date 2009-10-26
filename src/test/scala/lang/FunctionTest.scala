package lang

import org.scalatest.junit.{JUnitSuite, ShouldMatchersForJUnit}
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * User: ochan
 * Date: Oct 24, 2009
 * Time: 7:55:43 PM
 * To change this template use File | Settings | File Templates.
 */

class FunctionTest extends JUnitSuite with ShouldMatchersForJUnit
{
  @Test def testFullFunc()
  {
    intFunc(1)
    intFunc(2 + 3)

    intParamLessFunc(1)
    intParamLessFunc(2 + 3)
  }

  def intFunc(x: Int)
  {
    println("intFunc: " + x)
  }

  def intParamLessFunc(x: => Int)
  {
    println("intParamLessFunc: " + x)
  }
}