package lesson05.homework

import lesson05.homework.Days.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ScheduleEntity(val lesson: String, val startTime: LocalTime, val endTime: LocalTime)

enum class Days {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

class Schedule {

    private val scheduleOfWeek = mutableMapOf<Days, MutableList<ScheduleEntity>>()
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private var currentDay: Days? = null

    fun addSchedule(day: Days, scheduleEntity: ScheduleEntity) {
        scheduleOfWeek.getOrPut(day) { mutableListOf() }.add(scheduleEntity)
    }

    override fun toString(): String {
        return scheduleOfWeek.toSortedMap()
            .map { (day, list) ->
                list.sortedBy { it.startTime }
                    .joinToString("\n") {
                        "%-15s${it.startTime.format(timeFormatter)} - ${
                            it.endTime.format(
                                timeFormatter
                            )
                        }".format("\t${it.lesson}:")
                    }.let {
                        "${day.name.lowercase().replaceFirstChar { day.name[0].uppercase() }}:\n$it"
                    }
            }.joinToString("\n\n")
    }

    operator fun invoke(f: Schedule.() -> Unit) {
        f(this)
    }

    fun monday(f: Schedule.() -> Unit) {
        currentDay = MONDAY
        f(this)
        currentDay = null
    }

    fun tuesday(f: Schedule.() -> Unit) {
        currentDay = TUESDAY
        f(this)
        currentDay = null
    }

    fun wednesday(f: Schedule.() -> Unit) {
        currentDay = WEDNESDAY
        f(this)
        currentDay = null
    }

    fun thursday(f: Schedule.() -> Unit) {
        currentDay = THURSDAY
        f(this)
        currentDay = null
    }

    fun friday(f: Schedule.() -> Unit) {
        currentDay = FRIDAY
        f(this)
        currentDay = null
    }

    fun saturday(f: Schedule.() -> Unit) {
        currentDay = SATURDAY
        f(this)
        currentDay = null
    }

    fun sunday(f: Schedule.() -> Unit) {
        currentDay = SUNDAY
        f(this)
        currentDay = null
    }

    infix fun ClosedRange<String>.schedule(lesson: String) {
        addSchedule(
            currentDay ?: throw IllegalStateException(),
            ScheduleEntity(lesson, start.toLocalTime(), endInclusive.toLocalTime())
        )
    }

    private fun String.toLocalTime() = LocalTime.parse(this, timeFormatter)
}

fun main() {

    val schedule = Schedule()

    // Так добавляется расписание без DSL
//    schedule.addSchedule(MONDAY, ScheduleEntity("Biology", LocalTime.of(10, 30), LocalTime.of(11, 10)))
//    schedule.addSchedule(MONDAY, ScheduleEntity("Chemistry", LocalTime.of(11, 15), LocalTime.of(11, 55)))

    // Так добавляется расписание с использованием DSL
    schedule {
        monday {
            "10:30".."11:10" schedule "Biology"
            "11:15".."11:55" schedule "Chemistry"
            "09:00".."09:40" schedule "Mathematics"
            "09:45".."10:25" schedule "History"
        }

        tuesday {
            "09:00".."09:40" schedule "English"
            "09:45".."10:25" schedule "Geography"
            "11:15".."11:55" schedule "Art"
            "10:30".."11:10" schedule "Physics"
        }

        wednesday {
            "11:15".."11:55" schedule "Biology"
            "09:00".."09:40" schedule "Literature"
            "10:30".."11:10" schedule "History"
            "09:45".."10:25" schedule "Mathematics"
        }

        thursday {
            "11:15".."11:55" schedule "Physics"
            "10:30".."11:10" schedule "Geography"
            "09:00".."09:40" schedule "Chemistry"
            "09:45".."10:25" schedule "English"
        }

        friday {
            "09:45".."10:25" schedule "Literature"
            "11:15".."11:55" schedule "History"
            "09:00".."09:40" schedule "Art"
            "10:30".."11:10" schedule "Mathematics"
        }
    }

    println(schedule.toString())
}
