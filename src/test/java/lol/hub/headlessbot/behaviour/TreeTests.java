package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.RootNode;
import lol.hub.headlessbot.behaviour.nodes.leaf.LeafNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTests {
    @Test
    void run() {
        var tree = new BehaviourTree(new RootNode(new LeafNode() {
            @Override
            public State run() {
                return State.SUCCESS;
            }
        }));

        assertEquals(tree.run(), State.SUCCESS);
    }
}
