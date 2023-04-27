package core;

public abstract class BaseSimulation {
    public interface Action{}

    public abstract void processAction(Action action);
}
