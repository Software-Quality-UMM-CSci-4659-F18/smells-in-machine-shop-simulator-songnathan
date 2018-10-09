/** machine shop simulation */

package applications;

public class MachineShopSimulator {

    // data members of MachineShopSimulator
    private static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    private static EventList eList; // pointer to event list
    private static Machine[] machine; // array of machines
    private static int largeTime; // all machines finish before this

    // methods


    private static void setMachineChangeOverTimes(SimulationSpecification specification) {
        for (int i = 1; i<=specification.getNumMachines(); ++i) {
            machine[i].setChangeTime(specification.getChangeOverTimes(i));
        }
    }

    private static void setUpJobs(SimulationSpecification specification) {
        // input the jobs
        Job theJob;
        for (int i = 1; i <= specification.getNumJobs(); i++) {
            int tasks = specification.getJobSpecifications(i).getNumTasks();
            int firstMachine = 0; // machine for first task

            // create the job
            theJob = new Job(i);
            for (int j = 1; j <= tasks; j++) {
                int theMachine = specification.getJobSpecifications(i).getSpecificationsForTasks()[2*(j-1)+1];
                int theTaskTime = specification.getJobSpecifications(i).getSpecificationsForTasks()[2*(j-1)+2];
                if (j == 1)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(machine[theMachine], theTaskTime); // add to
            } // task queue
            machine[firstMachine].putJobOnQ(theJob);
        }
    }

    private static void createEventAndMachineQueues(SimulationSpecification specification) {
        // create event and machine queues
        eList = new EventList(specification.getNumMachines(), largeTime);
        machine = new Machine[specification.getNumMachines() + 1];

        Machine.eList = eList;
        for (int i = 1; i <= specification.getNumMachines(); i++)
            machine[i] = new Machine(i);
    }

    /** load first jobs onto each machine
     * @param specification*/
    static void startShop(SimulationSpecification specification) {
        // Move this to startShop when ready
        MachineShopSimulator.numMachines = specification.getNumMachines();
        MachineShopSimulator.numJobs = specification.getNumJobs();
        createEventAndMachineQueues(specification);

        // Move this to startShop when ready
        setMachineChangeOverTimes(specification);

        // Move this to startShop when ready
        setUpJobs(specification);

        for (int p = 1; p <= numMachines; p++)
            machine[p].changeState(timeNow);
    }

    /** process all jobs to completion
     * @param simulationResults*/
    static void simulate(SimulationResults simulationResults) {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = machine[nextToFinish].changeState(timeNow);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !theJob.moveToNextMachine(simulationResults, timeNow, eList))
                numJobs--;
        }
    }

    /** output wait times at machines
     * @param simulationResults*/
    static void outputStatistics(SimulationResults simulationResults) {
        simulationResults.setFinishTime(timeNow);
        simulationResults.setNumMachines(numMachines);
        setNumTasksPerMachine(simulationResults);
        simulationResults.setTotalWaitTimePerMachine(machine);
    }

    private static void setNumTasksPerMachine(SimulationResults simulationResults) {
        int[] numTasksPerMachine = new int[numMachines+1];
        for (int i=1; i<=numMachines; ++i) {
            numTasksPerMachine[i] = machine[i].getNumTasks();
        }
        simulationResults.setNumTasksPerMachine(numTasksPerMachine);
    }

    public static SimulationResults runSimulation(SimulationSpecification specification) {
        largeTime = Integer.MAX_VALUE;
        timeNow = 0;
        startShop(specification); // initial machine loading
        SimulationResults simulationResults = new SimulationResults(numJobs);
        simulate(simulationResults); // run all jobs through shop
        outputStatistics(simulationResults);
        return simulationResults;
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        final SpecificationReader specificationReader = new SpecificationReader();
        SimulationSpecification specification = specificationReader.readSpecification();
        SimulationResults simulationResults = runSimulation(specification);
        simulationResults.print();
    }
}
