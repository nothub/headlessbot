package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

public class RepeatNode extends DecoratorNode {
    private final boolean exitOnFail;
    private int count;

    public RepeatNode(Node child, int count, boolean exitOnFail) {
        super(child);
        this.count = count;
        this.exitOnFail = exitOnFail;
    }

    @Override
    public State run() {
        do {
            var result = child.run();
            if (result == State.FAILURE && exitOnFail) {
                return State.FAILURE;
            }
        } while (count-- > 0);
        return State.SUCCESS;
    }
}
