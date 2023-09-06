package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.RootNode;
import lol.hub.headlessbot.behaviour.nodes.decorators.RepeatNode;
import lol.hub.headlessbot.behaviour.nodes.leafs.TaskNode;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTests {
    @Test
    void simple() {
        var tree = new BehaviourTree(new RootNode(new TaskNode() {
            @Override
            public State run() {
                return State.SUCCESS;
            }
        }));

        assertEquals(tree.run(), State.SUCCESS);
    }

    @Test
    void repeat() {
        AtomicInteger i = new AtomicInteger();
        var tree = new BehaviourTree(new RootNode(new RepeatNode(new TaskNode() {
            @Override
            public State run() {
                i.incrementAndGet();
                return State.SUCCESS;
            }
        }, 3, false)));
        tree.run();

        assertEquals(i.get(), 3);
    }
}
