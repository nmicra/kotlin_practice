import java.util.*


val flights = listOf("11-12","9-19","12-16", "7-10", "11-18","15-19", "16-20", "12-15", "14-17")

/**
 * Find max flights in the air for the schedule list (flights)
 */
fun main() {

    var maxFligths = 0
    var currentFlights = 0

    val map1 = flights.map { it.split("-") }.map { Pair(it[0].toInt(), it[1].toInt()) }.associate { it.first to it.second }

    val queueDepartures = PriorityQueue<Int>()
    queueDepartures.addAll(map1.keys)

    val queueArrivals = PriorityQueue<Int>()
    queueArrivals.addAll(map1.values)


    while (queueDepartures.isNotEmpty()){
        val dep = queueDepartures.peek()
        val arr = queueArrivals.peek()

        when {
            dep < arr -> {
                currentFlights++
                if (currentFlights > maxFligths) maxFligths = currentFlights
                queueDepartures.remove()
            }
            dep == arr -> {
                queueDepartures.remove()
                queueArrivals.remove()
            }
            else -> {
                currentFlights--
                queueArrivals.remove()
            }
        }
    }
    println(maxFligths)
}