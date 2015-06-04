package com.reverie

import grails.transaction.Transactional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.Duration

@Transactional
class SchedulerService {
    def utilityService
    /**
     * The main scheduling algorithm
     * @param datePointer
     * @param owner
     */
    def reDraw(LocalDateTime datePointer, User owner){
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
                taskList.remove(0)
            }
            if(owner.completionTimeConstant>owner.deadlineConstant){
                utilityService.computeWeights(Task.findAllByOwner(owner), owner.deadlineConstant, owner.completionTimeConstant, owner)
            }
            def tempList = Task.findAllByOwner(owner, [sort: "weight"])
            taskList.clear()
            for(Task t : tempList){
                if(completionArray.get(t.id)>0){
                    taskList.add(t)
                }
            }
        }
    }
    /**
     * refreshes the tasks list and removes subtasks that are already done.
     * @param owner
     */
    def refresh(User owner){
        def now = LocalDateTime.now()
        Task[] taskList = Task.findAllByOwnerAndDone(owner, false)
        for(Task t : taskList){
            SubTask[] stList = SubTask.findAllByMotherTask(t)
            for(SubTask st : stList){
                if(now.isAfter(st.subTaskStart)){
                    st.delete(flush: true)
                    t.completionTime--
                    t.save(failOnError: true)
                    if(t.completionTime<=0){
                        t.delete(flush: true)
                    }
                }
            }
        }
    }
}
