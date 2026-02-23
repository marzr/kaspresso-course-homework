package lesson07.homework

import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher

data class Shape(
    val sideLength: Float,
    val sidesNumber: Int,
    val color: Color
) {
    val angleNumber = if (sidesNumber > 2) sidesNumber else 0
}

enum class Color {
    RED, BLUE, GREEN, YELLOW, BLACK, WHITE
}

class SideLengthMatcher(
    private val from: Float,
    private val to: Float
) : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description
            .appendText("shape with side length ")
            .appendValue(from)
            .appendText(" to ")
            .appendValue(to)
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.sideLength !in from..to) {
            mismatchDescription
                .appendText("side length was ")
                .appendValue(item.sideLength)
            return false
        }
        return true
    }
}

class AngleNumberMatcher(private val expectedAngleNumber: Int) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description
            .appendText("shape with ")
            .appendValue(expectedAngleNumber)
            .appendText(" angles")
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.angleNumber != expectedAngleNumber) {
            mismatchDescription
                .appendText("number of angles was ")
                .appendValue(item.angleNumber)
            return false
        }
        return true
    }
}

class ColorMatcher(private val expectedColor: Color) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description
            .appendText("shape with color ")
            .appendValue(expectedColor.name)
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.color != expectedColor) {
            mismatchDescription
                .appendText("color was ")
                .appendValue(item.color)
            return false
        }
        return true
    }
}

object EvenSidesNumberMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description
            .appendText("shape with even number of sides")
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.sidesNumber % 2 != 0) {
            mismatchDescription
                .appendText("number of sides was ")
                .appendValue(item.sidesNumber)
            return false
        }
        return true
    }
}

object PositiveSidesNumberMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("number of sides is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean = (item.sidesNumber >= 0).also {
        if (!it) {
            mismatchDescription
                .appendText("number of sides was negative: ")
                .appendValue(item.sidesNumber)
        }
    }
}

object PositiveSideLengthMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("side length is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean = (item.sidesNumber >= 0).also {
        if (!it) {
            mismatchDescription
                .appendText("side length was negative: ")
                .appendValue(item.sideLength)
        }
    }
}

fun hasSideLength(from: Float, to: Float) = SideLengthMatcher(from, to)
fun hasAngles(expectedAngleNumber: Int) = AngleNumberMatcher(expectedAngleNumber)
fun hasEvenSidesNumber() = EvenSidesNumberMatcher
fun hasColor(expectedColor: Color) = ColorMatcher(expectedColor)
fun hasValidSidesNumber() = PositiveSidesNumberMatcher
fun hasValidSideLength() = PositiveSideLengthMatcher

fun main() {
    val shapes = listOf(
        Shape(10f, 3, Color.RED),
        Shape(5f, 4, Color.BLUE),
        Shape(7f, 2, Color.GREEN),
        Shape(0.5f, 1, Color.YELLOW),
        Shape(-3f, 5, Color.BLACK),
        Shape(8f, -2, Color.WHITE),
        Shape(12f, 6, Color.RED),
        Shape(15f, 8, Color.BLUE),
        Shape(20f, 4, Color.GREEN),
        Shape(9f, 5, Color.YELLOW),
        Shape(2f, 3, Color.BLACK),
        Shape(11f, 7, Color.WHITE),
        Shape(6f, 10, Color.RED),
        Shape(3f, 2, Color.BLUE),
        Shape(4f, 1, Color.GREEN),
        Shape(25f, 12, Color.YELLOW),
        Shape(30f, 14, Color.BLACK),
        Shape(35f, 16, Color.WHITE),
        Shape(40f, 18, Color.RED),
        Shape(50f, 20, Color.BLUE)
    )

    val blueSquares = shapes.filter { shape ->
        allOf(
            hasValidSidesNumber(),
            hasValidSideLength(),
            hasAngles(4),
            hasColor(Color.BLUE)
        ).matches(shape)
    }
    println("blue squares: $blueSquares")

    val greenLines = shapes.filter { shape ->
        allOf(
            hasValidSidesNumber(),
            hasValidSideLength(),
            hasAngles(0),
            hasColor(Color.GREEN)
        ).matches(shape)
    }
    println("green lines: $greenLines")

    val redOrBlueValidFigures = shapes.filter { shape ->
        allOf(
            hasValidSidesNumber(),
            hasValidSideLength(),
            anyOf(
                hasColor(Color.RED),
                hasColor(Color.BLUE)
            )
        ).matches(shape)
    }
    println("red or blue valid figures: $redOrBlueValidFigures")

    val smallBlackFigures = shapes.filter { shape ->
        allOf(
            hasSideLength(0f, 3f),
            hasColor(Color.BLACK)
        ).matches(shape)
    }
    println("small black figures: $smallBlackFigures")

    val evenSidesRedFigures = shapes.filter { shape ->
        allOf(
            hasEvenSidesNumber(),
            hasColor(Color.RED)
        ).matches(shape)
    }
    println("even sides red figures figures: $evenSidesRedFigures")
}