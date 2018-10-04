package applications;

// top-level nested classes
class Task {
    // data members
    private Machine machine;
    private int time;

    // constructor
    Task(Machine machine, int time){
        this.machine = machine;
        this.time = time;
    }

    public Machine getMachine() {
        return machine;
    }

    public int getTime() {
        return time;
    }
}
