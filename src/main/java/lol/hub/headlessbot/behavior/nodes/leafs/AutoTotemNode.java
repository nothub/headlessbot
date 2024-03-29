package lol.hub.headlessbot.behavior.nodes.leafs;

import net.minecraft.item.Items;

import static lol.hub.headlessbot.behavior.State.SUCCESS;

public class AutoTotemNode extends McNode {
    public AutoTotemNode() {
        super(mc -> {
            if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING))
                return SUCCESS;
            // TODO: global inventory cooldown

            var totemSlot = -1;
            for (int i = 0; i < 45; i++) {
                if (mc.player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                    totemSlot = i < 9 ? i + 36 : i;
                    break;
                }
            }

            if (totemSlot >= 0) {
                // TODO: swapSlots(slot, 45);
            }

            return SUCCESS;
        });
    }
}
