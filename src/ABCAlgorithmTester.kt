const val ITERATIONS_COUNT: Int = 1000
const val ITERATIONS_PER_STEP: Int = 20

class ABCAlgorithmTester(private val algorithm: ABCAlgorithm) {
    fun test() {
        var bestResult = algorithm.calculateChromaticNumber()
        algorithm.resetAlgorithm()
        for (iteration in 0..ITERATIONS_COUNT) {
            if (iteration % ITERATIONS_PER_STEP == 0) {
                println("on iteration $iteration best result is $bestResult")
            }
            val newChromaticNumber = algorithm.calculateChromaticNumber()
            if (newChromaticNumber < bestResult) {
                bestResult = newChromaticNumber
                algorithm.graph.printColors()
            }
            algorithm.resetAlgorithm()
        }
    }
}