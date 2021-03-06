package applications;

import java.util.Arrays;

public class SimulationResults {
    private int finishTime;
    private int numMachines;
    private int[] numTasksPerMachine;
    private int[] totalWaitTimePerMachine;
    private JobCompletionData[] jobCompletions;
    private int nextJob = 0;

    public SimulationResults(int numJobs) {
        jobCompletions = new JobCompletionData[numJobs];
    }

    /** output wait times at machines
     */
    public void outputStatistics(int timeNow, Machine[] machine) {
        setFinishTime(timeNow);
        setNumMachines(machine.length - 1);
        setNumTasksPerMachine(machine);
        setTotalWaitTimePerMachine(machine);
    }

    private void setNumTasksPerMachine(Machine[] machine) {
        this.numTasksPerMachine = new int[machine.length+1];
        for (int i = 1; i<machine.length; ++i) {
            numTasksPerMachine[i] = machine[i].getNumTasks();
        }
    }

    private void setTotalWaitTimePerMachine(Machine[] machines) {
        totalWaitTimePerMachine = new int[machines.length+1];
        for (int i = 1; i< machines.length; ++i) {
            totalWaitTimePerMachine[i] = machines[i].getTotalWait();
        }
    }

    public void print() {
        for (JobCompletionData data : jobCompletions) {
            System.out.println("Job " + data.getJobNumber() + " has completed at "
                    + data.getCompletionTime() + " Total wait was " + data.getTotalWaitTime());
        }

        System.out.println("Finish time = " + finishTime);
        for (int p = 1; p <= numMachines; p++) {
            System.out.println("Machine " + p + " completed "
                    + numTasksPerMachine[p] + " tasks");
            System.out.println("The total wait time was "
                    + totalWaitTimePerMachine[p]);
            System.out.println();
        }
    }

    public int getFinishTime() {
        return finishTime;
    }

    private void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    private void setNumMachines(int numMachines) {
        this.numMachines = numMachines;
    }

    public int[] getNumTasksPerMachine() {
        return Arrays.copyOf(numTasksPerMachine, numTasksPerMachine.length);
    }



    public int[] getTotalWaitTimePerMachine() {
        return Arrays.copyOf(totalWaitTimePerMachine, totalWaitTimePerMachine.length);
    }

    public JobCompletionData[] getJobCompletionData() {
        return jobCompletions;
    }

    public void setJobCompletionData(int jobNumber, int completionTime, int totalWaitTime) {
        JobCompletionData jobCompletionData = new JobCompletionData(jobNumber, completionTime, totalWaitTime);
        jobCompletions[nextJob] = jobCompletionData;
        nextJob++;
    }
}
