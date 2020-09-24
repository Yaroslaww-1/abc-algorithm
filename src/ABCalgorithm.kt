const val EMPLOYED_BEES_COUNT: Int = 2
const val ONLOOKER_BEES_COUNT: Int = 28
const val PALETTE_SIZE: Int = 10000

class ABCAlgorithm(private val initialGraph: Graph) {
    var graph = Graph(initialGraph.getAdjMatrix())
    private var availableVertices: Array<Int> = graph.getVertexArray()
    private val palette: Array<Int> = Array(PALETTE_SIZE) { i -> i }
    private var usedColors: MutableList<Int> = mutableListOf()

    fun calculateChromaticNumber(): Int {
        while (!this.isFinished()) {
            val selectedVertices = this.sendEmployedBees()
            this.sendOnlookerBees(selectedVertices)
        }
        val chromaticNumber = usedColors.size
        return chromaticNumber
    }

    fun resetAlgorithm() {
        usedColors = mutableListOf()
        availableVertices = graph.getVertexArray()
        graph = Graph(initialGraph.getAdjMatrix())
    }

    private fun isFinished(): Boolean {
        return graph.isAllVerticesValidColored()
    }

    private fun sendEmployedBees(): Array<Int> {
        val selectedVertices: MutableList<Int> = MutableList(0) { i -> i }
        for (employedBee in 0 until EMPLOYED_BEES_COUNT) {
            val randomSelectedVertexIndex = (0 until availableVertices.size).random()
            val randomSelectedVertex = availableVertices[randomSelectedVertexIndex]
            availableVertices = availableVertices
                .filter { vertex -> vertex != randomSelectedVertex }
                .toTypedArray()
            selectedVertices += randomSelectedVertex
        }
        return selectedVertices.toTypedArray()
    }

    private fun sendOnlookerBees(selectedVertices: Array<Int>) {
        val selectedVerticesDegrees = selectedVertices.map { vertex ->
            graph.getVertexDegree(vertex)
        }.toTypedArray()
        val onlookerBeesSplit = this.getOnlookerBeesSplit(selectedVerticesDegrees)
        selectedVertices.forEachIndexed { selectedVertexIndex, selectedVertex ->
            val onlookerBeesCountForVertex = onlookerBeesSplit[selectedVertexIndex]
            val connectedVertices = graph.getConnectedVertexes(selectedVertex)
            this.colorConnectedVertices(connectedVertices, onlookerBeesCountForVertex)
            this.colorVertex(selectedVertex)
        }
    }

    private fun getOnlookerBeesSplit(selectedVerticesDegrees: Array<Int>): Array<Int> {
        val nectarValues = this.getNectarValues(selectedVerticesDegrees)
        var onlookerBeesCount = ONLOOKER_BEES_COUNT
        return nectarValues.mapIndexed { index, nectar ->
            if (index == nectarValues.size) onlookerBeesCount
            else {
                val onlookerBeesCountForCurrentVertex = (onlookerBeesCount * nectar).toInt()
                onlookerBeesCount -= onlookerBeesCountForCurrentVertex
                onlookerBeesCountForCurrentVertex
            }
        }.toTypedArray()
    }

    private fun getNectarValues(selectedVerticesDegrees: Array<Int>): Array<Double> {
        val summarySelectedVerticesDegree = selectedVerticesDegrees.sum()
        return selectedVerticesDegrees.map { vertexDegree ->
            (vertexDegree / summarySelectedVerticesDegree).toDouble()
        }.toTypedArray()
    }

    private fun colorConnectedVertices(connectedVertices: Array<Int>, onlookerBeesCount: Int) {
        connectedVertices.forEachIndexed { connectedVertexIndex, connectedVertex ->
            if (connectedVertexIndex >= onlookerBeesCount - 1) return
            colorVertex(connectedVertex)
        }
    }

    private fun colorVertex(vertex: Int) {
        val availableColors = usedColors.toList().toMutableList() //copy array
        var isColoredSuccessfully = false
        while (!isColoredSuccessfully) {
            if (availableColors.size == 0) {
                val newColor = getNextColor()
                usedColors.plusAssign(newColor)
                graph.tryToColorAndCheckIsValid(vertex, newColor)
                isColoredSuccessfully = true
                break
            }
            val randomAvailableColorIndex = (0 until availableColors.size).random()
            val color = availableColors[randomAvailableColorIndex]
            availableColors.removeAt(randomAvailableColorIndex)
            isColoredSuccessfully = graph.tryToColorAndCheckIsValid(vertex, color)
        }
    }

    private fun getNextColor(): Int {
        return palette[usedColors.size]
    }
}