package nonograms.model

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * TODO: Document this file.
 */
class Game(val grid: MutableGrid, val rowRules: List<Rule>, val columnRules: List<Rule>) {
    init {
        require(rowRules.size == grid.rowCount) { "Wrong number of row rules, need ${grid.rowCount}" }
        require(columnRules.size == grid.columnCount) { "Wrong number of column rules, need ${grid.columnCount}" }
    }

    companion object {
        fun fromBitmapResource(path: String): Game {
            val url = this::class.java.getResource("/Sample.png")
                ?: throw IllegalArgumentException("Resource not found: $path")
            val bitmap = ImageIO.read(url)
            return fromBitmap(bitmap)
        }

        fun fromBitmap(bitmap: BufferedImage): Game {
            val grid = MutableGrid(bitmap.width, bitmap.height)
            val rowRules = (0 until bitmap.height).map { row ->
                (0 until bitmap.width)
                    .asSequence()
                    .map { col -> (bitmap.getRGB(col, row) and 0xFFFFFF) != 0 }
                    .counts()
                    .toList()
                    .let { Rule(it) }
            }
            val colRules = (0 until bitmap.width).map { col ->
                (0 until bitmap.height)
                    .asSequence()
                    .map { row -> (bitmap.getRGB(col, row) and 0xFFFFFF) != 0 }
                    .counts()
                    .toList()
                    .let { Rule(it) }
            }
            return Game(grid, rowRules, colRules)
        }

        private fun Sequence<Boolean>.counts(): Sequence<Int> {
            return sequence {
                var currentCount = 0
                forEach { isSet ->
                    if (isSet) {
                        currentCount++
                    } else {
                        if (currentCount > 0) {
                            yield(currentCount)
                            currentCount = 0
                        }
                    }
                }
                if (currentCount > 0) {
                    yield(currentCount)
                    currentCount = 0
                }
            }
        }
    }
}