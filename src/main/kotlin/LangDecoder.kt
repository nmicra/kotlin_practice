/**
 * Given map is how to encode any character in alphabet.
 * Write a decode function, that will return all possible variant that can be encoded for a given string
 * Example: for a given string "12", possible values [ab,l]
 * for "1213" --> [abac, abm, auc, lac, lm]
 */

val map = mapOf(
    "a" to "1", "b" to "2", "c" to "3", "d" to "4", "e" to "5",
    "f" to "6", "g" to "7", "h" to "8", "i" to "9", "j" to "10",
    "k" to "11", "l" to "12", "m" to "13", "n" to "14", "o" to "15",
    "p" to "16", "q" to "17", "r" to "18", "s" to "19", "t" to "20",
    "u" to "21", "v" to "22", "w" to "23", "x" to "24", "y" to "25", "z" to "26"
)

fun main() {
    println(decode("1213"))
}

val reversedMap = map.entries.associateBy({ it.value }) { it.key }
val abcMaxLength = map["z"]?.length ?: error("Assumption that z is the last character of the alphabet")

fun splitString(str : String,n : Int) : Pair<String,String> = Pair(str.toCharArray().dropLast(str.length-n).joinToString(""),
                                                                str.toCharArray().drop(n).joinToString(""))


tailrec fun decode(str: String) : Set<String> {
    fun aggregate(splitedPair : Pair<String,String>) : Set<String> {
        val set = (1..splitedPair.first.length)
                .map { splitString(splitedPair.first,it) }
                .map { reversedMap[it.first].orEmpty() + reversedMap[it.second].orEmpty() }
                .toSet()
        val set2 = decode(splitedPair.second)
        return set.map {el1 -> set2.map { el1 +it }.toSet() }.flatten().toSet()
    }
    return when {
        str.length <= abcMaxLength -> (1..str.length)
                .map { splitString(str,it) }
                .map { reversedMap[it.first].orEmpty() + reversedMap[it.second].orEmpty() }
                .toSet()
        else -> (1..abcMaxLength).asSequence()
                .map { splitString(str, it) }
                .map { aggregate(it) }
                .toSet().flatten().toSet()
    }
}