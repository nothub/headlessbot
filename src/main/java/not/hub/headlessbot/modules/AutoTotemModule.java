package not.hub.headlessbot.modules;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Cooldowns;

import static not.hub.headlessbot.util.PlayerStuff.swapSlots;

public class AutoTotemModule extends Module {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (Cooldowns.INVENTORY.isValid()) return;
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) return;
        int slot = totemSlot(Items.TOTEM_OF_UNDYING);
        if (slot == -1) return;
        swapSlots(slot, 45);
    }

    public int totemSlot(Item item) {
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i < 9 ? i + 36 : i;
        }
        return -1;
    }

}
