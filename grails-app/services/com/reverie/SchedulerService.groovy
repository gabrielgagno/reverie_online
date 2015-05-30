package com.reverie

import grails.transaction.Transactional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.Duration

@Transactional
class SchedulerService {
    def utilityService
    def reDraw(LocalDateTime datePointer, User owner){
        println("DATE PINT: " + datePointer.toString())
        Random random = new Random()
        int index = 0
        int complTime
        int timeBeforeDeadline
        float upperRandCeil
        //move datepointer to the end of first habit encountered
        SubTask[] subHabitsTemp = utilityService.getAllSubHabits(owner)
        for(SubTask sTemp : subHabitsTemp){
            if(utilityService.overlapFinder(datePointer, sTemp)){
                datePointer = sTemp.subTaskEnd
            }
        }
        ArrayList<Task> taskList = Task.findAllByOwner(owner, [sort: "weight"])
        def o = owner

        //clear all subTasks of task
        if(Task.findAllByOwnerAndDone(owner, false).size()!=0) {
            def sts = SubTask.findAllByMotherTaskInList(Task.findAllByOwnerAndDone(owner, false))
            SubTask.deleteAll(sts)
        }
        int scheduled = 0
        int totalNum = taskList.size()
        HashMap<String, Float> completionArray = new HashMap<String, Float>()
        //initialize hashMaps
        for(Task t : taskList){
            completionArray.put(t.id, t.completionTime)
            println("COMPLETION ARRAY ELEMENT: " + t.id + " " + t.completionTime)
        }
        float currDuration
        while(scheduled!=totalNum){
            int[] timeArray
            if(completionArray.get(taskList.get(0).id)<1){
                // time remaining in completionArray is <1
                currDuration = completionArray.get(taskList.get(0).id)
                timeArray = utilityService.floatToHoursMins(completionArray.get(taskList.get(0).id))
            }
            else{
                //opposite
                currDuration = 1.0f
                timeArray = utilityService.floatToHoursMins(1.0f)
            }
            complTime = completionArray.get(taskList.get(0).id)
            timeBeforeDeadline = new Duration(datePointer.toDateTime(DateTimeZone.UTC), taskList.get(0).deadline.toDateTime(DateTimeZone.UTC)).getStandardHours()
            SubTask[] subTasks = SubTask.findAllByMotherTaskInList(Job.findAllByOwner(owner), [sort: "subTaskStart"])
            ArrayList<LocalDateTime> freeTimes = utilityService.findFreeTimes(timeBeforeDeadline, datePointer, subTasks)
            println("FREETIMES " + freeTimes.size() + " " + completionArray.get(taskList.get(0).id))
            if(freeTimes.size()<completionArray.get(taskList.get(0).id)){
                if(freeTimes.size()!=0){
                    completionArray.put(taskList.get(0).id, (float) completionArray.get(taskList.get(0).id)-currDuration)
                    SubTask st = new SubTask()
                    st.motherTask = taskList.get(0)
                    st.subTaskStart = freeTimes.get(0)
                    st.subTaskEnd = freeTimes.get(0).plusHours(timeArray[0]).plusMinutes(timeArray[1])
                    st.save(failOnError: true)
                }
                else{
                    int counter = 0
                    for(int i=0; ;i++){
                        def tempPointer = datePointer.plusMinutes(i*30)
                        boolean isFree = true
                        int j=0
                        for(j=0;j<subTasks.length;j++){
                            if(subTasks[j].subTaskStart.isAfter(tempPointer)){
                                break;
                            }
                            if(tempPointer.equals(subTasks[j].subTaskStart)){
                                //i+=duration
                                isFree = false
                                float ceil = new Duration(subTasks[j].subTaskStart.toDateTime(DateTimeZone.UTC), subTasks[j].subTaskEnd.toDateTime(DateTimeZone.UTC)).getStandardMinutes()/30
                                int dur = ceil-1
                                i+= dur
                                counter = 0
                                break
                            }
                        }
                        if(isFree){
                            if(counter == 1){
                                completionArray.put(taskList.get(0).id, (float) completionArray.get(taskList.get(0).id)-currDuration)
                                SubTask st = new SubTask()
                                st.motherTask = taskList.get(0)
                                st.subTaskStart = tempPointer.minusMinutes(30)
                                st.subTaskEnd = tempPointer.minusMinutes(30).plusHours(timeArray[0]).plusMinutes(timeArray[1])
                                st.save(failOnError: true)
                                break
                            }
                            else{
                                counter++
                            }
                        }
                    }
                }
            }
            else{
                upperRandCeil = (complTime + freeTimes.size())/2
                int x = random.nextInt((int) upperRandCeil)
                def randomizedPointer = freeTimes.get(x)
                completionArray.put(taskList.get(0).id, (float) completionArray.get(taskList.get(0).id)-currDuration)
                SubTask st = new SubTask()
                st.motherTask = taskList.get(0)
                st.subTaskStart = randomizedPointer
                st.subTaskEnd = randomizedPointer.plusHours(timeArray[0]).plusMinutes(timeArray[1])
                st.save(failOnError: true)
            }
            if(completionArray.get(taskList.get(0).id)<=0){
                scheduled++
                println("SCHEDULED ONE! " + scheduled)
                taskList.remove(0)
            }
            if(owner.completionTimeConstant>owner.deadlineConstant){
                utilityService.computeWeights(Task.findAllByOwner(owner), owner.deadlineConstant, owner.completionTimeConstant, owner)
            }
            def tempList = Task.findAllByOwner(owner, [sort: "weight"])
            /*
            def d = Task.createCriteria()
            def tempList = d.list {
                eq("owner", owner)
                and{
                    order('weight')
                }
            }*/
            taskList.clear()
            for(Task t : tempList){
                if(completionArray.get(t.id)>0){
                    taskList.add(t)
                }
            }
        }
    }

    boolean fit(User owner, LocalDateTime tStart, int[] timeArray){
        def jobs = Job.findAllByOwner(owner)
        SubTask[] subTasks = SubTask.findAllByMotherTaskInList(jobs, [sort: "subTaskStart"])
        println("SUBTASKS")
        for(SubTask st : subTasks){
            println(st.subTaskStart.toString() + " " + st.subTaskEnd.toString())
        }
        int i
        for(i=0;i<subTasks.length;i++){
            if(tStart.compareTo(subTasks[i].subTaskStart)<=0){
                break;
            }
        }
        def tEnd = tStart.plusHours(timeArray[0]).plusMinutes(timeArray[1])
        println("TIME: " + tStart + " " + tEnd)
        println("I: " + i)
        if(i<subTasks.length){
            if(tEnd.compareTo(subTasks[i].subTaskStart)>0){
                println("HERE DIDNT FIT")
                return false
            }
        }
        println("FITSKY")
        return true
    }

    def refresh(User owner){
        def now = LocalDateTime.now()
        Task[] taskList = Task.findAllByOwnerAndDone(owner, false)
        def tempTList = []
        for(Task t : taskList){
            println("TASK NAME: " + t.jobName + " " + t.id)
            SubTask[] stList = SubTask.findAllByMotherTask(t)
            println("TLIST: " + taskList.length + " " + "STLIST: " + stList.length)
            //def tempStList = []
            for(SubTask st : stList){
                println(st.subTaskStart.toString())
                if(now.isAfter(st.subTaskStart)){
                    println("OO!")
                    //tempStList << st
                    st.delete(flush: true)
                    t.completionTime--
                    t.save(failOnError: true)
                    println("MAY ERROR BA?")
                    if(t.completionTime<=0){
                        //tempTList << t
                        t.delete(flush: true)
                    }
                }
            }
            /*
            if(tempStList.size()>0){
                SubTask.deleteAll(tempStList)
            }
            if(tempTList.size()>0){
                Task.deleteAll(tempTList)
            }*/
        }
    }
}
