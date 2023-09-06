package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

// WHILE N>0
public class RepeatNode extends DecoratorNode {
    private final boolean exitOnFail;
    private int runs;

    public RepeatNode(Node child, int runs, boolean exitOnFail) {
        super(child);
        this.runs = runs;
        this.exitOnFail = exitOnFail;
    }

    @Override
    public State run() {
        do {
            var result = child().run();
            if (result == State.FAILURE && exitOnFail) {
                return State.FAILURE;
            }
        } while (--runs > 0);
        return State.SUCCESS;
    }
}
