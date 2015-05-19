package com.reverie

import grails.transaction.Transactional
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.Hours
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Minutes
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.tz.FixedDateTimeZone

@Transactional
class UtilityService {
    def addTask(User owner, String jobName, String jobNotes, String deadline, float completionTimeHour) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        def t = new Task()
        t.owner = owner
        t.jobName = jobName
        t.jobNotes = jobNotes
        t.deadline = fmt.parseLocalDateTime(deadline)
        t.completionTime = completionTimeHour
        //t.completionLocalTime = completionLocalTime
        //t.minOperationDuration = minOperationDurationHour
        //t.minOpDurationLocalTime = minOpDurationLocalTime
        t.save(failOnError: true)
    }

    def addHabit(User owner, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency){
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd")
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm")
        LocalDate rs = dateFmt.parseLocalDate(rangeStart)
        LocalDate re = dateFmt.parseLocalDate(rangeEnd)
        LocalTime sh = timeFmt.parseLocalTime(startHour)
        LocalTime eh = timeFmt.parseLocalTime(endHour)
        println(sh.toString())
        println(eh.toString())
        Habit h = new Habit()
        h.owner = owner
        h.jobName = jobName
        h.jobNotes = jobNotes
        h.rangeStart = rs
        h.rangeEnd = re
        h.start = sh
        h.end = eh
        h.frequency = frequency
        h.save(failOnError: true)
        LocalDate tempStart = rs
        LocalTime tempSh = sh
        LocalTime tempEh = eh
        /*
        if(sh.getHourOfDay()>12){
            println("SH")
            tempSh = tempSh.minusHours(12)
        }
        println(tempSh.toString())
        if(eh.getHourOfDay()>12){
            println("EH")
            tempEh = tempEh.minusHours(12)
        }*/
        int minutes
        println(eh.toString())
        println(eh.plusMinutes(1440).toString())
        if(sh.isBefore(eh)){
            minutes = Math.abs(Minutes.minutesBetween(eh, sh).getMinutes())
        }
        else{
            minutes = Math.abs(Minutes.minutesBetween(eh.toDateTimeToday().toLocalDateTime().plusDays(1), sh.toDateTimeToday().toLocalDateTime()).getMinutes())
        }
        println(minutes)
        while(!tempStart.isAfter(re)){
            addSubTask(h, tempStart.toLocalDateTime(sh), tempStart.toLocalDateTime(sh).plusMinutes(minutes))
            if(frequency.equals("ONCE")){
                break
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
            def arr = floatToHoursMins(t.completionTime)
            def allMinutes = arr[1] + (arr[0]*60000)
            t.weight = weight(wx, wy, t.deadline.toDateTime().getMillis(), allMinutes)
            t.save()
        }
    }

    private static double weight(int wx, int wy, long x, long y){
        return (2*wx*x) + (wy*y)
    }

    def createDatePointer(){
        //return LocalDateTime.now().plusHours(13).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0) //only for testing in PH purposes
        return LocalDateTime.now().plusHours(1).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0) //real deal should be fixed
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
        println(arr[0])
        println(arr[1])
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

    def findResetIndex(HashMap<String, Float> completionList, Task[] taskList){
        for(int i=0;i<taskList.size();i++){
            if(completionList.get(taskList[i].id)>0){
                return i
            }
        }
        return -1
    }

    def findNextIndex(HashMap<String, Float> completionList, Task[] taskList, int index){
        for(int i=index;i<taskList.size();i++){
            if(completionList.get(taskList[i].id)>0){
                return i
            }
        }
        return -1
    }

    def getAllSubHabits(User subHabitOwner){
        SubTask[] emptyArr
        if(Habit.findAllByOwner(subHabitOwner).size()==0){
            return emptyArr
        }
        else {
            def query = SubTask.where {
                inList("motherTask", Habit.findAllByOwner(subHabitOwner))
            }
            SubTask[] res = query.list(sort: "subTaskStart")
            for (SubTask r : res) {
                println(r.subTaskStart.toString())
            }
            return res
        }
    }

    def overlapFinder(LocalDateTime datePointer, SubTask subTask) {
        if (datePointer.compareTo(subTask.subTaskStart) == 0) {
            return true
        }
        return datePointer.isAfter(subTask.subTaskStart) && datePointer.isBefore(subTask.subTaskEnd)
    }

    def editTask(String id, String jobName, String jobNotes, String deadline, int completionTimeHour){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")
        def task = Task.findById(id)
        task.jobName = jobName
        task.jobNotes = jobNotes
        task.deadline = fmt.parseLocalDateTime(deadline)
        task.completionTime = completionTimeHour
        //task.completionLocalTime = completionLocalTime
        //task.minOpDurationLocalTime = minOpDurationLocalTime
        task.save(failOnError: true)
    }

    def editHabit(String id, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency) {
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd")
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm")
        def habit = Habit.findById(id)
        habit.jobName = jobName
        habit.jobNotes = jobNotes
        habit.rangeStart = dateFmt.parseLocalDate(rangeStart)
        habit.rangeEnd = dateFmt.parseLocalDate(rangeEnd)
        habit.start = timeFormatter.parseLocalTime(startHour)
        habit.end = timeFormatter.parseLocalTime(endHour)
        habit.frequency = frequency
        habit.save(failOnError: true)
        LocalDate tempStart = habit.rangeStart
        int minutes
        println(habit.end.toString())
        println(habit.end.plusMinutes(1440).toString())
        if(habit.start.isBefore(habit.end)){
            minutes = Math.abs(Minutes.minutesBetween(habit.end, habit.start).getMinutes())
        }
        else{
            minutes = Math.abs(Minutes.minutesBetween(habit.end.toDateTimeToday().toLocalDateTime().plusDays(1), habit.start.toDateTimeToday().toLocalDateTime()).getMinutes())
        }
        println(minutes)
        while (!tempStart.isAfter(habit.rangeEnd)) {
            addSubTask(habit, tempStart.toLocalDateTime(habit.start), tempStart.toLocalDateTime(habit.start).plusMinutes(minutes))
            if (frequency.equals("ONCE")) {
                break
            } else if (frequency.equals("DAILY")) {
                tempStart = tempStart.plusDays(1)
            } else if (frequency.equals("WEEKLY")) {
                tempStart = tempStart.plusWeeks(1)
            } else if (frequency.equals("MONTHLY")) {
                tempStart = tempStart.plusMonths(1)
            } else if (frequency.equals("ANNUALLY")) {
                tempStart = tempStart.plusYears(1)
            }
        }
    }

    def findFreeTimes(int timeBeforeDeadline, LocalDateTime datePointer, SubTask[] subTasks){
        ArrayList<LocalDateTime> arrayList = new ArrayList<LocalDateTime>()
        for(int i=0;i<timeBeforeDeadline;i++){
            def tempPointer = datePointer.plusHours(i)
            boolean isFree = true
            int j=0
            for(j=0;j<subTasks.length;j++){
                if(subTasks[j].subTaskStart.isAfter(tempPointer)){
                    break;
                }
                if(tempPointer.equals(subTasks[j].subTaskStart)){
                    //i+=duration
                    isFree = false
                    int dur = new Duration(subTasks[j].subTaskStart.toDateTime(DateTimeZone.UTC), subTasks[j].subTaskEnd.toDateTime(DateTimeZone.UTC)).getStandardHours()-1
                    println(dur)
                    i+= dur
                    break
                }
            }
            if(isFree){
                arrayList.add(tempPointer)
            }
        }
        for(LocalDateTime ldt : arrayList){
            println(ldt.toString())
        }
        return arrayList
    }
}
