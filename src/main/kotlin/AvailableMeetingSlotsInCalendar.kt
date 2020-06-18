import java.time.LocalTime

val person1_meetings = listOf("09:00-09:30", "13:00-14:00", "17:20-18:00") // Scheduled meetings for Person1
val person2_meetings = listOf( "13:30-14:30", "17:00-17:30") // Scheduled meetings for Person2
val restrictions_person1 = "08:00,19:00" // Person1 cannot accept meetings before 8:00 & after 19:00
val restrictions_person2 = "08:30,18:30" // Person2 cannot accept meetings before 8:00 & after 19:00
val findMeetingSlot : Long = 40 // minutes
val step : Long = 10 // minutes


/**
 * Find all possibilities for a meeting with the duration of $findMeetingSlot minutes between Person1 & Person2
 * This meeting should not overlap with already existing meetings or specified restrictions.
 */
fun main() {
    val lstResults = mutableListOf<String>()
    val allMeetings = parseStringListsToLocalTime(person1_meetings).plus(parseStringListsToLocalTime(person2_meetings))
    var (begin, end) = determineLowerUpperRestrictions(restrictions_person1,restrictions_person2)

    while (begin.plusMinutes(findMeetingSlot).isBefore(end)){
        if (allMeetings.none { isOverLap(it.first,it.second,begin, begin.plusMinutes(findMeetingSlot))}){
            lstResults.add("$begin - ${begin.plusMinutes(findMeetingSlot)}")
        }
        begin = begin.plusMinutes(step)
    }

    println(lstResults)
}

/**
 * returns true when startTime1-endTime1 is overlaps with startTime2-endTime2
 */
fun isOverLap(start1 : LocalTime, end1 : LocalTime, start2 : LocalTime, end2 : LocalTime) : Boolean{
    if((start1.isAfter(start2) && start1.isBefore(end2)) || (end1.isAfter(start2) && end1.isBefore(end2)) ||
            start1.isBefore(start2) && end1.isAfter(end2) || start2.isBefore(start1) && end2.isAfter(end1)){
        return true
    }
    return false
}

fun parseStringListsToLocalTime(list : List<String>) : List<Pair<LocalTime,LocalTime>> {
    return list.map { val (left,right) = it.split("-")
        Pair(LocalTime.parse(left), LocalTime.parse(right))}.toList()
}

fun determineLowerUpperRestrictions(restrictions_person1 : String, restrictions_person2 : String) : Pair<LocalTime,LocalTime>{
    val (left1,right1) = restrictions_person1.split(",")
    val (left2,right2) = restrictions_person2.split(",")
    val lowerBound = when{
        LocalTime.parse(left1).isBefore(LocalTime.parse(left2)) -> LocalTime.parse(left2)
        else -> LocalTime.parse(left1)
    }

    val upperBound = when {
        LocalTime.parse(right1).isBefore(LocalTime.parse(right2)) -> LocalTime.parse(right1)
        else -> LocalTime.parse(right2)
    }
    return Pair(lowerBound,upperBound)
}