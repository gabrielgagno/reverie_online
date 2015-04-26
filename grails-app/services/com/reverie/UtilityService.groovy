package com.reverie

import grails.transaction.Transactional
import org.joda.time.Instant
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class UtilityService {
    def addTask(User owner, String jobName, String jobNotes, String deadline, float completionTimeHour, int completionTimeMinute, float minOperationDurationHour, int minOperationDurationMinute) {
        //process competionTime and minOperationDuration
        LocalTime completionLocalTime
        LocalTime minOpDurationLocalTime
        completionLocalTime = new LocalTime((int) completionTimeHour, completionTimeMinute)
        minOpDurationLocalTime = new LocalTime((int) minOperationDurationHour, minOperationDurationMinute)
        if(completionTimeMinute==30){
            completionTimeHour+=0.5
        }
        if(minOperationDurationMinute==30){
            minOperationDurationHour+=0.5
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        def t = new Task()
        t.owner = owner
        t.jobName = jobName
        t.jobNotes = jobNotes
        t.deadline = fmt.parseLocalDateTime(deadline)
        t.completionTime = completionTimeHour
        t.completionLocalTime = completionLocalTime
        t.minOperationDuration = minOperationDurationHour
        t.minOpDurationLocalTime = minOpDurationLocalTime
        println(t.owner)
        t.save()
    }

    def addHabit(User owner, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY/MM/dd")
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm")
        LocalDate rs = dateFmt.parseLocalDate(rangeStart)
        LocalDate re = dateFmt.parseLocalDate(rangeEnd)
        LocalTime sh = timeFmt.parseLocalTime(startHour)
        LocalTime eh = timeFmt.parseLocalTime(endHour)
        Habit h = new Habit()
        h.owner = owner
        h.jobName = jobName
        h.jobNotes = jobNotes
        h.rangeStart = rs
        h.rangeEnd = re
        h.start = sh
        h.end = eh
        h.frequency = frequency
        h.save()
        LocalDate tempStart = rs
        while(!tempStart.isAfter(re)){
            addSubTask(h, tempStart.toLocalDateTime(sh), tempStart.toLocalDateTime(eh))
            if(frequency.equals("ONCE")){
                break;
            }
            else if(frequency.equals("DAILY")){
                tempStart = tempStart.plusDays(1)
            }
            else if(frequency.equals("WEEKLY")){
                tempStart = tempStart.plusWeeks(1)
            }
            else if(frequency.equals("MONTHLY")){
                tempStart = tempStart.plusMonths(1)
            }
            else if(frequency.equals("ANNUALLY")){
                tempStart = tempStart.plusYears(1)
            }
        }
    }

    def addSubTask(Job motherTask, LocalDateTime subTaskStart, LocalDateTime subTaskEnd){
        SubTask st = new SubTask()
        st.motherTask = motherTask
        st.subTaskStart = subTaskStart
        st.subTaskEnd = subTaskEnd
        st.save()
    }

    def computeWeights(tasks, int wx, int wy){
        for(Task t : tasks){
            t.weight = weight(wx, wy, t.deadline.toDateTime().getMillis(), t.completionLocalTime.toDateTimeToday().getMillis())
            t.save()
        }
    }

    private static double weight(int wx, int wy, long x, long y){
        return (2*wx*x) + (wy*y)
    }

    def createDatePointer(){
        return LocalDateTime.now().plusHours(1).withMinuteOfHour(0)
    }

    def habitSameDay(SubTask[] subHabitList, LocalDateTime tStart){
        for(SubTask x : subHabitList){
            if(tStart.dayOfWeek() == x.subTaskStart.dayOfWeek()){
                return true;
            }
        }
        return false;
    }

    def floatToHoursMins(float x){
        int[] arr
        arr = new int[2]
        float temp = x*60
        arr[0] = (int) temp/60
        arr[1] = (int) temp%60
        return arr
    }

    def findNextHabit(Habit[] habits, LocalDateTime datePointer){
        def query = SubTask.where {
            inList("motherTask", habits)
            order('subTaskEnd', 'asc')
        }
        SubTask[] subTasks = query.findAll()
        for(SubTask st : subTasks){
            if(st.subTaskEnd.isAfter(datePointer)){
                return st.subTaskEnd
            }
        }

    }
}
