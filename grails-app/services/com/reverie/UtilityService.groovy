package com.reverie

import grails.transaction.Transactional
import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import java.awt.*

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
        t.taskColor = colorRandomizer()
        t.save(failOnError: true)
    }

    def addHabit(User owner, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency, wkFreq) {
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd")
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
        h.wkDays = new ArrayList<Integer>()
        if(frequency.equals("WEEKLY")){
            for(String x : wkFreq){
                h.wkDays.add(Integer.parseInt(x))
            }
        }
        if (re.isBefore(rs)) {
            return false
        }
        h.start = sh
        h.end = eh
        h.frequency = frequency
        h.save(failOnError: true)
        LocalDate tempStart = rs
        if(frequency.equals("WEEKLY")){
            tempStart = tempStart.dayOfWeek().withMinimumValue().minusDays(1)
            println("WEEKLY: " + tempStart)
        }
        LocalTime tempSh = sh
        LocalTime tempEh = eh
        int minutes
        if (sh.isBefore(eh)) {
            minutes = Math.abs(Minutes.minutesBetween(eh, sh).getMinutes())
        } else {
            minutes = Math.abs(Minutes.minutesBetween(eh.toDateTimeToday().toLocalDateTime().plusDays(1), sh.toDateTimeToday().toLocalDateTime()).getMinutes())
        }
        while (!tempStart.isAfter(re)) {
            println("ITO BA?")
            if(!frequency.equals("WEEKLY")){
                addSubTask(h, tempStart.toLocalDateTime(sh), tempStart.toLocalDateTime(sh).plusMinutes(minutes))
            }
            if (frequency.equals("ONCE")) {
                break
            } else if (frequency.equals("DAILY")) {
                tempStart = tempStart.plusDays(1)
            } else if (frequency.equals("WEEKLY")) {
                for(int x : h.wkDays){
                    println("EH ITO?")
                    if(x==7){
                        //tempStart = tempStart.minusWeeks(1).withDayOfWeek(7)
                        addSubTask(h, tempStart.minusWeeks(1).withDayOfWeek(7).toLocalDateTime(sh), tempStart.minusWeeks(1).withDayOfWeek(7).toLocalDateTime(sh).plusMinutes(minutes))
                    }
                    else{
                        //tempStart = tempStart.withDayOfWeek(x)
                        addSubTask(h, tempStart.withDayOfWeek(x).toLocalDateTime(sh), tempStart.withDayOfWeek(x).toLocalDateTime(sh).plusMinutes(minutes))
                    }
                    println(tempStart.toString())
                    //addSubTask(h, tempStart.toLocalDateTime(sh), tempStart.toLocalDateTime(sh).plusMinutes(minutes))
                }
                tempStart = tempStart.plusWeeks(1).withDayOfWeek(1)
                println("FINALE: " + tempStart.toString())
            } else if (frequency.equals("MONTHLY")) {
                tempStart = tempStart.plusMonths(1)
            } else if (frequency.equals("ANNUALLY")) {
                tempStart = tempStart.plusYears(1)
            }
        }
        return true
    }

    def addSubTask(Job motherTask, LocalDateTime subTaskStart, LocalDateTime subTaskEnd) {
        SubTask st = new SubTask()
        st.motherTask = motherTask
        st.subTaskStart = subTaskStart
        st.subTaskEnd = subTaskEnd
        st.save()
    }

    def computeWeights(tasks, int wx, int wy, User owner) {
        if (wy > wx) {
            for (Task t : tasks) {
                def count = SubTask.countByMotherTask(t)
                def arr = floatToHoursMins(t.completionTime)
                def allMinutes = arr[1] + (arr[0] * 60000)
                t.weight = weight(wx, wy, new Duration(DateTime.now().getMillis(), t.deadline.toDateTime().getMillis()).getStandardMinutes(), arr[0], (float) count / t.completionTime)
                //t.weight2 = weight(wy, wx, new Duration(DateTime.now().getMillis(), t.deadline.toDateTime().getMillis()).getStandardMinutes(), arr[0], (float) count/t.completionTime)
                t.save(flush: true)
            }
        } else {
            ArrayList<Integer> freeList = new ArrayList<Integer>()
            def sts = SubTask.findAllByMotherTaskInList(Task.findAllByOwnerAndDone(owner, false))
            SubTask.deleteAll(sts)
            SubTask[] subTasks = SubTask.findAllByMotherTaskInList(Job.findAllByOwner(owner), [sort: "subTaskStart"])
            for (Task t : tasks) {
                freeList.add(findFreeTimes((int) new Duration(createDatePointer().toDateTime(DateTimeZone.UTC), t.deadline.toDateTime(DateTimeZone.UTC)).getStandardHours(), createDatePointer(), subTasks).size())

            }
            def taskListSize = freeList.size()
            ArrayList<Task> solution = new ArrayList<Task>()
            for (int i = taskListSize; i > 0; i--) {
                if (solution.size() > i) {
                    break
                }
                findnCr(tasks, freeList, taskListSize, i, solution)
            }
            def weightCtr = 0
            for (Task t : solution) {
                t.weight = weightCtr++
                t.save(flush: true)
            }
            for (Task t : tasks) {
                if (!solution.contains(t)) {
                    t.weight = weightCtr++
                    t.save(flush: true)
                }
            }
        }
    }

    private static double weight(int wx, int wy, long x, long y, float age) {
        if (wx > wy) {
            return (wx * (y / x))
        } else {
            (wy * age)
        }
        //return (wx*(1-(y/x))) + (wy*age)
    }

    def createDatePointer() {
        //return LocalDateTime.now().plusHours(13).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0) //only for testing in PH purposes
        return LocalDateTime.now().plusHours(1).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
        //real deal should be fixed
    }

    def habitSameDay(SubTask[] subHabitList, LocalDateTime tStart) {
        for (SubTask x : subHabitList) {
            if (tStart.dayOfWeek() == x.subTaskStart.dayOfWeek()) {
                return true;
            }
        }
        return false;
    }

    def floatToHoursMins(float x) {
        int[] arr
        arr = new int[2]
        float temp = x * 60
        arr[0] = (int) temp / 60
        arr[1] = (int) temp % 60
        return arr
    }

    def findNextHabit(Habit[] habits, LocalDateTime datePointer) {
        def query = SubTask.where {
            inList("motherTask", habits)
            order('subTaskEnd', 'asc')
        }
        SubTask[] subTasks = query.findAll()
        for (SubTask st : subTasks) {
            if (st.subTaskEnd.isAfter(datePointer)) {
                return st.subTaskEnd
            }
        }

    }

    def findResetIndex(HashMap<String, Float> completionList, Task[] taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            if (completionList.get(taskList[i].id) > 0) {
                return i
            }
        }
        return -1
    }

    def findNextIndex(HashMap<String, Float> completionList, Task[] taskList, int index) {
        for (int i = index; i < taskList.size(); i++) {
            if (completionList.get(taskList[i].id) > 0) {
                return i
            }
        }
        return -1
    }

    def getAllSubHabits(User subHabitOwner) {
        SubTask[] emptyArr
        if (Habit.findAllByOwner(subHabitOwner).size() == 0) {
            return emptyArr
        } else {
            def query = SubTask.where {
                inList("motherTask", Habit.findAllByOwner(subHabitOwner))
            }
            SubTask[] res = query.list(sort: "subTaskStart")
            return res
        }
    }

    def overlapFinder(LocalDateTime datePointer, SubTask subTask) {
        if (datePointer.compareTo(subTask.subTaskStart) == 0) {
            return true
        }
        return datePointer.isAfter(subTask.subTaskStart) && datePointer.isBefore(subTask.subTaskEnd)
    }

    def editTask(String id, String jobName, String jobNotes, String deadline, int completionTimeHour) {
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

    def editHabit(String id, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency, wkFreq) {
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
        habit.wkDays = new ArrayList<Integer>()
        for(String x : wkFreq){
            habit.wkDays.add(Integer.parseInt(x))
        }
        habit.save(failOnError: true)
        LocalDate tempStart = habit.rangeStart
        if(frequency.equals("WEEKLY")){
            tempStart = tempStart.withDayOfWeek(1)
        }
        int minutes
        if (habit.start.isBefore(habit.end)) {
            minutes = Math.abs(Minutes.minutesBetween(habit.end, habit.start).getMinutes())
        } else {
            minutes = Math.abs(Minutes.minutesBetween(habit.end.toDateTimeToday().toLocalDateTime().plusDays(1), habit.start.toDateTimeToday().toLocalDateTime()).getMinutes())
        }
        while (!tempStart.isAfter(habit.rangeEnd)) {
            if(!frequency.equals("WEEKLY")) {
                addSubTask(habit, tempStart.toLocalDateTime(habit.start), tempStart.toLocalDateTime(habit.start).plusMinutes(minutes))
            }
            if (frequency.equals("ONCE")) {
                break
            } else if (frequency.equals("DAILY")) {
                tempStart = tempStart.plusDays(1)
            } else if (frequency.equals("WEEKLY")) {
                for(int x : habit.wkDays){
                    if(x==7){
                        //tempStart = tempStart.minusWeeks(1).withDayOfWeek(7)
                        addSubTask(habit, tempStart.minusWeeks(1).withDayOfWeek(7).toLocalDateTime(habit.start), tempStart.minusWeeks(1).withDayOfWeek(7).toLocalDateTime(habit.start).plusMinutes(minutes))
                    }
                    else{
                        //tempStart = tempStart.withDayOfWeek(x)
                        addSubTask(habit, tempStart.withDayOfWeek(x).toLocalDateTime(habit.start), tempStart.withDayOfWeek(x).toLocalDateTime(habit.start).plusMinutes(minutes))
                    }
                }
                tempStart = tempStart.plusWeeks(1).withDayOfWeek(1)
            } else if (frequency.equals("MONTHLY")) {
                tempStart = tempStart.plusMonths(1)
            } else if (frequency.equals("ANNUALLY")) {
                tempStart = tempStart.plusYears(1)
            }
        }
    }

    def findFreeTimes(int timeBeforeDeadline, LocalDateTime datePointer, SubTask[] subTasks) {
        int counter = 0
        ArrayList<LocalDateTime> arrayList = new ArrayList<LocalDateTime>()
        for (int i = 0; i < timeBeforeDeadline * 2; i++) {
            def tempPointer = datePointer.plusMinutes(i * 30)
            boolean isFree = true
            int j = 0
            for (j = 0; j < subTasks.length; j++) {
                if (subTasks[j].subTaskStart.isAfter(tempPointer)) {
                    break;
                }
                if (tempPointer.equals(subTasks[j].subTaskStart)) {
                    //i+=duration
                    isFree = false
                    float ceil = new Duration(subTasks[j].subTaskStart.toDateTime(DateTimeZone.UTC), subTasks[j].subTaskEnd.toDateTime(DateTimeZone.UTC)).getStandardMinutes() / 30
                    int dur = ceil - 1
                    i += dur
                    counter = 0
                    break
                }
            }
            if (isFree) {
                if (counter == 1) {
                    arrayList.add(tempPointer.minusMinutes(30))
                    counter = 0
                } else {
                    counter++
                }
            }
        }
        return arrayList
    }

    def habitConflictCheck(User owner, String id, String jobName, String jobNotes, String rangeStart, String rangeEnd, String startHour, String endHour, String frequency) {
        //create habits and subhabits but don't persist yet
        Habit habit
        if (!id.equals(null)) {
            habit = Habit.findById(id)
        } else {
            habit = new Habit()
        }
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd")
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm")
        LocalDate rs = dateFmt.parseLocalDate(rangeStart)
        LocalDate re = dateFmt.parseLocalDate(rangeEnd)
        LocalTime sh = timeFmt.parseLocalTime(startHour)
        LocalTime eh = timeFmt.parseLocalTime(endHour)

        habit.jobName = jobName
        habit.jobNotes = jobNotes
        habit.owner = owner
        habit.rangeStart = rs
        habit.rangeEnd = re
        habit.start = sh
        habit.end = eh
        habit.frequency = frequency
        LocalDate tempStart = rs
        def tempList = []
        int minutes
        if (sh.isBefore(eh)) {
            minutes = Math.abs(Minutes.minutesBetween(eh, sh).getMinutes())
        } else {
            minutes = Math.abs(Minutes.minutesBetween(eh.toDateTimeToday().toLocalDateTime().plusDays(1), sh.toDateTimeToday().toLocalDateTime()).getMinutes())
        }
        while (!tempStart.isAfter(re)) {
            SubTask t = new SubTask()
            t.motherTask = habit
            t.subTaskStart = tempStart.toLocalDateTime(sh)
            t.subTaskEnd = tempStart.toLocalDateTime(sh).plusMinutes(minutes)
            tempList << t
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
        //real algorithm
        SubTask[] inputList = tempList
        Task[] tasks = Task.findAllByOwner(owner)
        for (Task t : tasks) {
            SubTask[] stList = SubTask.findAllByMotherTask(t)
            for (SubTask st2 : stList) {
                for (SubTask st1 : inputList) {
                    if (overlapChecker(st1, st2)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    def overlapChecker(SubTask st1, SubTask st2) {
        if (st1.subTaskStart.compareTo(st2.subTaskStart) == 0 || st1.subTaskEnd.compareTo(st2.subTaskEnd) == 0) {
            return true
        } else if (st1.subTaskStart.isAfter(st2.subTaskStart) && st1.subTaskStart.isBefore(st2.subTaskEnd)) {
            return true
        } else if (st1.subTaskEnd.isAfter(st2.subTaskStart) && st1.subTaskEnd.isBefore(st2.subTaskEnd)) {
            return true
        } else if (st1.subTaskStart.isBefore(st2.subTaskStart) && st1.subTaskEnd.isAfter(st2.subTaskEnd)) {
            return true
        } else if (st1.subTaskStart.isAfter(st2.subTaskStart) && st1.subTaskEnd.isBefore(st2.subTaskEnd)) {
            return true
        } else if (st1.subTaskStart.isBefore(st2.subTaskStart) && st1.subTaskEnd.isAfter(st2.subTaskEnd)) {
            return true
        }
        return false
    }

    def findnCr(tasks, ArrayList<Integer> freeList, int n, int r, ArrayList<Task> solution) {
        Task[] tasksList = tasks
        int[] res = new int[r]
        for (int i = 0; i < res.length; i++) {
            res[i] = i + 1
        }
        boolean done = false;
        while (!done) {
            ArrayList<Task> tList = new ArrayList<Task>()
            ArrayList<Integer> fList = new ArrayList<Integer>()
            for (int i = 0; i < res.length; i++) {
                tList.add(tasksList[res[i] - 1])
                fList.add(freeList.get(res[i] - 1))
            }
            def ctr = fitChecker(tList, fList, (n - solution.size()), (n - r))
            if (ctr < (n - solution.size())) {
                solution.clear()
                for (Task t : tList) {
                    solution.add(t)
                }
            }
            done = getNext(res, n, r)
        }
    }

    def fitChecker(ArrayList<Task> tList, ArrayList<Integer> fList, int x, int init) {
        def ctr = init
        def saveVariable = 0
        for (int i = 0; i < tList.size(); i++) {
            saveVariable += tList.get(i).completionTime
            if (saveVariable > fList.get(i)) {
                tList.remove(i)
                fList.remove(i)
                i--
                ctr++
                if (ctr > x) {
                    break
                }
            }
        }
        return ctr

    }

    boolean getNext(int[] num, int n, int r) {
        int target = r - 1
        num[target]++
        if (num[target] > ((n - (r - target)) + 1)) {
            // Carry the One
            while (num[target] > ((n - (r - target)))) {
                target--
                if (target < 0) {
                    break
                }
            }
            if (target < 0) {
                return true
            }
            num[target]++
            for (int i = target + 1; i < num.length; i++) {
                num[i] = num[i - 1] + 1
            }
        }
        return false;
    }

    def colorRandomizer() {
        Color mix = new Color(100, 50, 0)
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (red + mix.getRed()) / 2;
            green = (green + mix.getGreen()) / 2;
            blue = (blue + mix.getBlue()) / 2;
        }

        Color color = new Color(red, green, blue);
        String res = "#" + Integer.toHexString(color.getRGB()).substring(2)
//        Random random = new Random()
//        for(int i=0;i<6;i++){
//            if(i==0){
//                res = res + Integer.toHexString(random.nextInt(5)+7)
//            }else if(i==2){
//                res = res + Integer.toHexString(random.nextInt(6))
//            }
//            else{
//                res = res + Integer.toHexString(random.nextInt(16))
//            }
//        }
        return res
    }

    def reporter(User owner){
        String message = null
        String smallMsg = ""
        def ctr = 0
        Task[] tasks = Task.findAllByOwner(owner)
        for(Task t : tasks){
            SubTask[] subTasks = SubTask.findAllByMotherTask(t, [sort: "subTaskStart"])
            def stLength = subTasks.length
            def stBeforeDeadline = 0
            for(SubTask st : subTasks){
                if(st.subTaskStart.isBefore(t.deadline)){
                    stBeforeDeadline++
                }
                else{
                    ctr++
                    break
                }
            }
            float percentage = (float) stBeforeDeadline/(float) t.completionTime
            smallMsg = smallMsg + "\n" + t.jobName + " " + percentage + "% done"
        }
        message = "WARNING: " + ctr + " task/s overshot their deadline/s."
        if(!smallMsg.equals("")){
            message = message
        }
        [message, smallMsg]
    }
}