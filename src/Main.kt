fun main(args: Array<String>) {
    val generator = GraphGenerator()
    val graph = generator.generateGraph()
    val algorithm = ABCAlgorithm(graph)
    val algorithmTester = ABCAlgorithmTester(algorithm)
    algorithmTester.test()
}
