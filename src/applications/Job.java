package applications;

import dataStructures.LinkedQueue;

class Job {
    // data members
    private LinkedQueue taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier

    // constructor
    Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    /**
     * move theJob to machine for its next task
     *
     * @return false iff no next task
     */
    public boolean moveToNextMachine(SimulationResults simulationResults, int timeNow, EventList eList) {
        if (hasNoTasks()) {// no next task
            simulationResults.setJobCompletionData(getId(), timeNow, timeNow - getLength());
            return false;
        } else {// theJob has a next task
                // get machine for next task
            Machine machine = getNextTask().getMachine();
            // put on machine's wait queue
            machine.putJobOnQ(this);
            setArrivalTime(timeNow);
            // if machine is idle, schedule immediately
            if (eList.nextEventTime(machine.getMachineNum()) == Integer.MAX_VALUE) {// machine is idle
                machine.changeState(timeNow);
            }
            return true;
        }
    }

    // other methods
    public void addTask(Machine theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        length = getLength() + theTime;
        return theTime;
    }

//    public LinkedQueue getTaskQ() {
//        return taskQ;
//    }

    public int getLength() {
        return length;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public Task getNextTask(){
        return (Task) taskQ.getFrontElement();
    }

    public boolean hasNoTasks(){
        return taskQ.isEmpty();
    }
}
