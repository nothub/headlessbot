package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.*;

/**
 * WHILE N>0
 * BREAK ON SUCCESS
 */
public final class RetryNode extends DecoratorNode {
    private final int runs;
    private int step;

    public RetryNode(Node child, int runs) {
        super(child);
        this.runs = runs;
        this.step = runs;
    }

    @Override
    public State tick() {
        if (child().tick() == SUCCESS) {
            step = runs;
            return SUCCESS;
        }
        if (--step <= 0) {
            step = runs;
            return FAILURE;
        }
        return RUNNING;
    }
}
