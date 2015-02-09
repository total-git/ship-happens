import shiphappens._
import org.scalatest._

class BoardTest extends FlatSpec with Matchers {

  // use assymetric values
  val w = 10
  val h = 8
  var b = new Board(w,h)

  "A new board with size 10x8" should "return correct size" in {
    b.height should be (h)
    b.width should be (w)
  }

  it should "reject ships out of bounds" in {
    b.setShip(Ship("Testship", 3), (w,h), Orientation.Horizontal) shouldBe empty
  }

  it should "reject ships at negative positions" in {
    b.setShip(Ship("Testship", 1), (0,-1), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 1), (-1,0), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 1), (-1,-1), Orientation.Horizontal) shouldBe empty
  }

  it should "allow to place a ship in the center" in {
    b.setShip(Ship("Testship", 1), (w/2,h/2), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", 1), (w/2,h/2), Orientation.Vertical) should not be empty
  }

  it should "allow to place a ship in the top left corner" in {
    b.setShip(Ship("Testship", w), (0,0), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", h), (0,0), Orientation.Vertical) should not be empty
  }

  it should "allow to place a ship in the bottom left corner" in {
    b.setShip(Ship("Testship", w), (0,h-1), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", 1), (0,h-1), Orientation.Vertical) should not be empty
  }
  it should "allow to place a ship in the top right corner" in {
    b.setShip(Ship("Testship", 1), (w-1,0), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", h), (w-1,0), Orientation.Vertical) should not be empty
  }

  it should "allow to place a ship in the bottom corner" in {
    b.setShip(Ship("Testship", 1), (w-1,h-1), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", 1), (w-1,h-1), Orientation.Vertical) should not be empty
  }

  b = b.setShip(Ship("Testship", 3), new Coordinates("B1"), Orientation.Vertical).get

  "A board with one length 3 vertical ship starting at B1" should "have a shipAt the correct positions" in {
    b.shipAt(1,0) should not be empty
    b.shipAt(1,1) should not be empty
    b.shipAt(1,2) should not be empty
  }

  it should "reject ships at overlapping positions" in {
    b.setShip(Ship("Testship", 3), (1,0), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 3), (1,1), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 3), (1,2), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 3), (0,1), Orientation.Vertical) shouldBe empty
  }

  it should "reject ships next to the first one" in {
    b.setShip(Ship("Testship", 3), (0,0), Orientation.Vertical) shouldBe empty
    b.setShip(Ship("Testship", 3), (2,0), Orientation.Vertical) shouldBe empty
    b.setShip(Ship("Testship", 3), (0,3), Orientation.Vertical) shouldBe empty
    b.setShip(Ship("Testship", 3), (1,3), Orientation.Vertical) shouldBe empty
    b.setShip(Ship("Testship", 3), (2,3), Orientation.Vertical) shouldBe empty

    b.setShip(Ship("Testship", 3), (2,0), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 3), (2,2), Orientation.Horizontal) shouldBe empty
    b.setShip(Ship("Testship", 3), (2,3), Orientation.Horizontal) shouldBe empty
  }

  it should "allow placement of ships at other positions" in {
    b.setShip(Ship("Testship", 1), (3,0), Orientation.Horizontal) should not be empty
    b.setShip(Ship("Testship", h-3), (3,0), Orientation.Vertical) should not be empty
  }

  "A ship at length 3 vertical ship from B1 to B3" should "be have 3 lives at beginning" in {
    b.shipAt("B1") should not be empty
    b.shipAt("B1").get.lives shouldBe 3
  }

  it should "return the same ship on all three positions" in {
    b.shipAt("B1") shouldBe b.shipAt("B2")
    b.shipAt("B1") shouldBe b.shipAt("B3")
  }

  it should "be unprobed" in {
    b.isProbed("B1") shouldBe false
    b.isProbed("B2") shouldBe false
    b.isProbed("B3") shouldBe false
  }

  it should "be have 2 lives after bombing B1" in {
    val (b1,r1) = b.probeSquare("B1")
    r1 shouldBe Result.Hit
    b1.shipAt("B1").get.lives shouldBe 2
  }

  it should "be sund and won after three hits" in {
    val (b1,r1) = b.probeSquare("B1")
    val (b2,r2) = b1.probeSquare("B2")
    val (b3,r3) = b2.probeSquare("B3")
    b1.shipAt("B1").get.lives shouldBe 2
    r1 shouldBe Result.Hit
    b2.shipAt("B1").get.lives shouldBe 1
    r2 shouldBe Result.Hit
    b3.shipAt("B1").get.lives shouldBe 0
    r3 shouldBe Result.Won
  }

  it should "not change something to hit the same field twice" in {
    val (b1,r1) = b.probeSquare("B1")
    val (b2,r2) = b1.probeSquare("B1")
    b2.shipAt("B1").get.lives shouldBe b1.shipAt("B1").get.lives
    r2 shouldBe Result.AlreadyProbed
  }


}

