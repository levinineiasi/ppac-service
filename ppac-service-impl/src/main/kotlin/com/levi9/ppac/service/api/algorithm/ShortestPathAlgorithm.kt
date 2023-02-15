package com.levi9.ppac.service.api.algorithm

data class Edge<T>(
    val node1: T,
    val node2: T,
    val distance: Int
)

class ShortestPathResult<T>(
    val prev: Map<T, T?>,
    val dist: Map<T, Int>,
    val source: T,
    val target: T
) {

    fun shortestPath(from: T = source, to: T = target, list: List<T> = emptyList()): List<T> {
        val last = prev[to] ?: return if (from == to) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[target]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}

/**
 * See https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 */
fun <T> findShortestPath(edges: List<Edge<T>>, source: T, target: T): ShortestPathResult<T> {

    // Note: this implementation uses similar variable names as the algorithm given do.
    // We found it more important to align with the algorithm than to use possibly more sensible naming.

    val dist = mutableMapOf<T, Int>()
    val prev = mutableMapOf<T, T?>()
    val q = findDistinctNodes(edges)

    q.forEach { v ->
        dist[v] = Integer.MAX_VALUE
        prev[v] = null
    }
    dist[source] = 0

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it] ?: 0 }
        q.remove(u)

        if (u == target) {
            break // Found the shortest path to target
        }
        edges
            .filter { it.node1 == u }
            .forEach { edge ->
                val v = edge.node2
                val alt = (dist[u] ?: 0) + edge.distance
                if (alt < (dist[v] ?: 0)) {
                    dist[v] = alt
                    prev[v] = u
                }
            }
    }

    return ShortestPathResult(prev, dist, source, target)
}

private fun <T> findDistinctNodes(edges: List<Edge<T>>): MutableSet<T> {
    val nodes = mutableSetOf<T>()
    edges.forEach {
        nodes.add(it.node1)
        nodes.add(it.node2)
    }
    return nodes
}
