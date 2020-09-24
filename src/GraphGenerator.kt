import kotlin.math.min

const val VERTEX_COUNT: Int = 200
const val MIN_VERTEX_DEGREE: Int = 1
const val MAX_VERTEX_DEGREE: Int = 20

class GraphGenerator {
    fun generateGraph(): Graph {
        val adjMatrix = Array(VERTEX_COUNT) { IntArray(VERTEX_COUNT) }
        for (vertex in (0 until VERTEX_COUNT - 1)) {
            val vertexConnections = adjMatrix[vertex]
            val currentVertexDegree = vertexConnections.count { adjMatrixElement -> adjMatrixElement == 1 }
            val finalVertexDegree = min(
                (MIN_VERTEX_DEGREE..MAX_VERTEX_DEGREE).random() - currentVertexDegree,
                VERTEX_COUNT - vertex - 1
            )
            for (newConnection in (0 until finalVertexDegree)) {
                var isConnectedAlready = true
                var tryCount = 0
                var newConnectionVertex: Int
                while (isConnectedAlready && tryCount < VERTEX_COUNT) {
                    newConnectionVertex = (vertex + 1 until VERTEX_COUNT).random()
                    tryCount ++
                    val newConnectionVertexDegree = adjMatrix[newConnectionVertex]
                        .count { adjMatrixElement -> adjMatrixElement == 1 }
                    if (
                        vertexConnections[newConnectionVertex] == 0 &&
                        newConnectionVertexDegree < MAX_VERTEX_DEGREE
                    ) {
                        isConnectedAlready = false
                        adjMatrix[vertex][newConnectionVertex] = 1
                        adjMatrix[newConnectionVertex][vertex] = 1
                    }
                }
            }
        }
        return Graph(adjMatrix)
    }
}