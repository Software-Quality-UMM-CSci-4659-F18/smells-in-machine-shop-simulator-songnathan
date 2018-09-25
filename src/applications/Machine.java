package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine
    private int machineNum; // to-do: make machines know their number
    public static EventList eList;

    Machine(int machineNum){
        this.machineNum = machineNum;
        jobQ = new LinkedQueue();
    }

    /**
     * change the state of theMachine
     *
     * @return last job run on this machine
     */
    public Job changeState(int timeNow) {// Task on theMachine has finished,

        // schedule next one.
        Job lastJob = getActiveJob();
        setActiveJob(timeNow);

        return lastJob;
    }

    public void putJobOnQ(Job job){
        jobQ.put(job);
    }

    public boolean hasNoJobs(){
        return jobQ.isEmpty();
    }

    /*
    If activeJob==null: sets activeJob to the next job on the Queue, unless the jobQ is empty then we change the "state"
                        of the machine to be idling
    Else: set the active job to be null, signifying that the machine is in its "change over state"

    Also updates all relevant numbers/fields
     */
    private void setActiveJob(int timeNow){

        //If the current active job is null, we try to give it one
        if(activeJob == null) {
            activeJob = (Job) jobQ.remove();

            //If the active job is still null, there are no jobs waiting for this machine and we tell eList that it is
            //idle by setting the finish time to MAX_VALUE
            if (activeJob == null){
                eList.setFinishTime(machineNum, Integer.MAX_VALUE);
            } else {
                totalWait += timeNow - activeJob.getArrivalTime();
                numTasks++;
                int finishTaskTime = activeJob.removeNextTask();
                eList.setFinishTime(machineNum, finishTaskTime + timeNow);
            }
        }
        //If the active job is not null then we are finished with the task and we make the active job null
        else {
            activeJob = null;
            eList.setFinishTime(machineNum, changeTime + timeNow);
        }
    }

    public boolean isIdle(){
        return jobQ.isEmpty() && activeJob==null;
    }


    public int getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(int totalWait) {
        this.totalWait = totalWait;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }

    public Job getActiveJob() {
        return activeJob;
    }


}
