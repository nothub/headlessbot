package not.hub.headlessbot.fsm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// TODO: expose a way to externally invoke transition execution
public abstract class Fsm<T> {

    private final Map<FsmState, Consumer<T>> transitions = new HashMap<>();
    FsmState current;

    public Fsm(FsmState start) {
        this.current = start;
    }

    public final void add(FsmState fsmState, Consumer<T> func) {
        transitions.put(fsmState, func);
    }


    /**
     * @return current state
     */
    public FsmState current() {
        return current;
    }

    /**
     * Transition to next state
     *
     * @param result result of current state
     */
    public void transition(T result) {
        transitions.get(current).accept(result);
    }

}
