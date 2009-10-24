package scalatest

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import collection.mutable.Stack
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


/**
 * Test FlatSpec
 */
@RunWith(classOf[JUnitRunner])
class TestFlatSpec extends FlatSpec with ShouldMatchers
{
    "A stack" should "pop values inthe last in first out" in {
        val stack = new Stack[Int];

        stack.push(1)
        stack.push(2)
        stack.pop() should equal (2)
        stack.pop() should equal (1)
    }

    it should "throw NoSuchElementException if any empty stack is popped" in {
        val emptyStack = new Stack[String]
        evaluating { emptyStack.pop() } should produce [NoSuchElementException]
    }
}			