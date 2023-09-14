package lol.hub.headlessbot.behavior;

import lol.hub.headlessbot.behavior.nodes.decorators.RepeatNode;
import lol.hub.headlessbot.behavior.nodes.leafs.LeafNode;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static lol.hub.headlessbot.behavior.State.RUNNING;
import static lol.hub.headlessbot.behavior.State.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTests {
    @Test
    void simple() {
        var tree = new BehaviorTree(new LeafNode() {
            @Override
            public State tick() {
                return SUCCESS;
            }
        });

        assertEquals(tree.tick(), SUCCESS);
    }

    @Test
    void repeat() {
        AtomicInteger i = new AtomicInteger();
        var tree = new BehaviorTree(new RepeatNode(new LeafNode() {
            @Override
            public State tick() {
                i.incrementAndGet();
                return SUCCESS;
            }
        }, 3));

        assertEquals(RUNNING, tree.tick());
        assertEquals(1, i.get());

        assertEquals(RUNNING, tree.tick());
        assertEquals(2, i.get());

        assertEquals(SUCCESS, tree.tick());
        assertEquals(3, i.get());
    }
}
