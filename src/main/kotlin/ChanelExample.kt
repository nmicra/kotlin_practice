/**
 * Write a code that produces 100 instances of
 * Employee(val id : String, val count : Int)
 * Those instances have to be consume in async-way by coroutines.
 * but the Employee with the same ID have to be consumed sequentially.
 *
 * Example, for employees: Employee(id=empId0, count=60), Employee(id=empId7, count=57), Employee(id=empId5, count=55), Employee(id=empId0, count=70)
 * The Employee(id=empId0, count=70) MUST BE HANDLED AFTER Employee(id=empId0, count=60), because they have same ID.
 * Other 2 employees (Employee(id=empId7, count=57), Employee(id=empId5, count=55)) have to be handled in async way
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.channels.Channel
import java.math.BigInteger

const val NUM_OF_EXECUTORS = 10
data class Employee(val id : String, val count : Int)
val workLoad : MutableList<Channel<Employee>> = MutableList(NUM_OF_EXECUTORS) { Channel() }


fun main(){

    // Initialize consumers
    for (i in 0 until NUM_OF_EXECUTORS) {
        GlobalScope.launch(Dispatchers.IO) { consumer(i,workLoad[i] ) }
    }

    for (i in 1..100){
        GlobalScope.launch(Dispatchers.IO) {
            val empl  = Employee("empId${ i.rem(NUM_OF_EXECUTORS)}", i)
//            val chnlNr = BigInteger(empl.id.toByteArray()).mod(BigInteger.valueOf(NUM_OF_EXECUTORS.toLong())).toInt()
            val chnlNr = asciiSum(empl.id).mod(NUM_OF_EXECUTORS)
            workLoad[chnlNr].send(empl)
        }
    }
    runBlocking { delay(10000) /* hold on for 10 seconds before closing */}
    coroutineContext.cancelChildren()
}


fun asciiSum(str : String) : Int = str.toCharArray().sumOf { it.code }

suspend fun consumer(consumerId: Int, channel: Channel<Employee>) {
    for (wrkr in channel) {
        println("[$consumerId] $wrkr")
        delay(300) // wait a bit
    }
}
