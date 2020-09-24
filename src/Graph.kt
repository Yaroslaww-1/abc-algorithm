class Graph(
    private val adjMatrix: Array<IntArray>
) {
    private val colors: Array<Int> = Array(adjMatrix.size) { -1 }

    init {
        this.validateAdjMatrix()
    }

    fun getAdjMatrix() = adjMatrix

    private fun validateAdjMatrix() {
        adjMatrix.forEachIndexed { index, row ->
            if (row[index] == 1) println("ERROR")
            val degree = row.count { el -> el == 1 }
            if (degree > MAX_VERTEX_DEGREE) {
                println("degree is bigger than MAX_VERTEX_DEGREE")
            }
        }
    }

    fun printDegrees() {
        adjMatrix.forEachIndexed { index, row ->
            val degree = row.count { el -> el == 1 }
            println(degree)
        }
    }

    fun printAdjMatrix() {
        adjMatrix.forEach { arr ->
            arr.forEach { el -> print("$el ") }
            println()
        }
    }

    fun printColors() {
        println("colors ${colors.joinToString(" ")}")
    }

    fun getVertexArray(): Array<Int> {
        return Array(VERTEX_COUNT){ i -> i }
    }

    fun getVertexDegree(vertex: Int): Int {
        return this.adjMatrix[vertex].count { el -> el == 1 }
    }

    fun getConnectedVertexes(vertex: Int): Array<Int> {
        return this.adjMatrix[vertex]
            .mapIndexed { index, el ->
                if (el == 1) index
                else null
            }
            .filterNotNull()
            .toTypedArray()
    }

    private fun getVertexColor(vertex: Int): Int {
        return this.colors[vertex]
    }

    fun isAllVerticesValidColored(): Boolean {
        val uncoloredVerticesCount = this.colors.count { color -> color == -1 }
        return uncoloredVerticesCount == 0 && this.isColoringValid()
    }

    fun tryToColorAndCheckIsValid(vertex: Int, newColor: Int): Boolean {
        val oldColor = this.colors[vertex]
        this.colors[vertex] = newColor
        val isValid = this.isColoringValid()
        if (!isValid) {
            this.colors[vertex] = oldColor
        }
        return isValid
    }

    private fun isColoringValid(): Boolean {
        adjMatrix.forEachIndexed { vertex1, row ->
            row.forEachIndexed { vertex2, el ->
                if (el == 1 &&
                    this.getVertexColor(vertex1) != -1 &&
                    this.getVertexColor(vertex2) != -1 &&
                    this.getVertexColor(vertex1) == this.getVertexColor(vertex2)
                ) {
                    return false
                }
            }
        }
        return true
    }
}