package testrtags

import rtags._
import rx._
import utest._

import scalatags.JsDom.all._

object RTagsSpec extends TestSuite {

  val tests = this {
    'rClassValue {
      val rClass1: Var[String] = Var("anaga")
      val rClass2: Rx[String] = rClass1.map(_.reverse)
      val rClass3: Rx.Dynamic[String] = rClass1.map(_ + "-mountains")

      //If test stop throwing exception on below line, finish the test
      val t = div(rClass1, rClass2, rClass3).render

      //now at least test if it compiles

      val t = div(cls := rClass1, cls := rClass2, cls := rClass3)
    }

    'rStyleValue {
      val rColor: Var[String] = Var("blue")
      val rBorderStyleTop: Rx[String] = rColor.map(x => if(x.size > 3) borderTopStyle.dashed.v else borderTopStyle.solid.v)
      val rBorderStyleBottom: Rx.Dynamic[String] = rColor.map(x => if(x.size > 3) borderBottomStyle.dotted.v else borderBottomStyle.none.v)

      val t = div(color := rColor, borderTopStyle := rBorderStyleTop, borderBottomStyle := rBorderStyleBottom).render

      assert(
        t.style.getPropertyValue(color.jsName) == "blue",
        t.style.getPropertyValue(borderTopStyle.jsName) == borderTopStyle.dashed.v,
        t.style.getPropertyValue(borderBottomStyle.jsName) == borderBottomStyle.dotted.v
      )

      rColor() = "red"
      assert(
        t.style.getPropertyValue(color.jsName) == "red",
        t.style.getPropertyValue(borderTopStyle.jsName) == borderTopStyle.solid.v,
        t.style.getPropertyValue(borderBottomStyle.jsName) == borderBottomStyle.none.v
      )
    }

    'rAttrValue {

      val rWidth: Var[Int] = Var(10)
      val rHeight: Rx[String] = rWidth.map(_ + 10 + "px")
      val rName: Rx.Dynamic[String] = rWidth.map(x => s"div_$x")

      val t = div(name := rName, heightA := rHeight, widthA:= rWidth).render

      var outerHtml: String = t.outerHTML
      assert(
        outerHtml == """<div name="div_10" height="20px" width="10"/>"""
      )

      rWidth() = 15; outerHtml = t.outerHTML
      assert(
        outerHtml == """<div name="div_15" height="25px" width="15"/>"""
      )
    }

  }
}
