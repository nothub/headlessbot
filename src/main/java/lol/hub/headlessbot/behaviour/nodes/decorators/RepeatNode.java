package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * WHILE N>0
 * BREAK ON FAILURE (OPTIONAL)
 */
public final class RepeatNode extends DecoratorNode {
    private final int runs;
    private int step;
    private final boolean breakOnFail;

    public RepeatNode(Node child, int runs, boolean breakOnFail) {
        super(child);
        this.runs = runs;
        this.step = runs;
        this.breakOnFail = breakOnFail;
    }

    public RepeatNode(Node child, int runs) {
        this(child, runs, false);
    }

    @Override
    public State tick() {
        if (child().tick() == FAILURE && breakOnFail) {
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
