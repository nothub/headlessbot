package not.hub.headlessbot.fsm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class FSM {

    private final Map<State, Supplier<State>> transitions = new HashMap<>();
    private State current;

    public FSM(State start) {
        this.current = start;
    }

    public final void add(State state, Supplier<State> generator) {
        transitions.put(state, generator);
    }

    public final State current() {
        return current;
    }

    public final State next() {
        current = transitions.get(current).get();
        return current;
    }

}
