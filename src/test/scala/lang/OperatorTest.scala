package lang

import org.specs.runner.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
/**
 * test operator methods
 */

class OperatorTest extends JUnitSuite with ShouldMatchersForJUnit
{
  class OpClass (var s: String, var i: Int)
  {
    /** prefix ops */
    def unary_+ () {this.i = this.i + 1}
    def unary_- () {this.i = - this.i }
  }

  def initialize()
  {
  }
  
  @Test def testInvocation()
  {
    // val op1 = new OpClass("a", 1)
  }


}