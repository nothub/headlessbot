package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.*;

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
