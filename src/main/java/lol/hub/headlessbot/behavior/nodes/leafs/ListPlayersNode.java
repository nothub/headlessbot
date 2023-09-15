package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.Mod;

import java.util.stream.Collectors;

import static lol.hub.headlessbot.behavior.State.SUCCESS;

public class ListPlayersNode extends McNode {
    public ListPlayersNode() {
        super(mc -> {
            if (Mod.ticksOnline % (20 * 10) == 0) {
                var players =
                    mc.world.getPlayers().stream()
                        .filter(p -> !p.getUuid().equals(MC.player().getUuid()))
                        .collect(Collectors.toSet());
                if (!players.isEmpty()) {
                    Log.info("players: %s", players.stream()
                        .map(p -> String.format("%s (%s)",
                            p.getGameProfile().getName(),
                            p.getUuid().toString()))
                        .collect(Collectors.joining(", ")));
                }
            }
            return SUCCESS;
        });
    }
}
