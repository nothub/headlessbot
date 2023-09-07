package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

// WHILE N>0
public class RepeatNode extends DecoratorNode {
    private final boolean exitOnFail;
    private final int runs;
    private int step;

    public RepeatNode(Node child, int runs, boolean exitOnFail) {
        super(child);
        this.runs = runs;
        this.step = runs;
        this.exitOnFail = exitOnFail;
    }

    @Override
    public State tick() {
        if (child().tick() == FAILURE && exitOnFail) {
            step = runs;
            return FAILURE;
        }
        if (--step == 0) {
            step = runs;
            return SUCCESS;
        }
        return RUNNING;
    }
}
