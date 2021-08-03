package not.hub.headlessbot.modules;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTotemModule extends Module {

    public AutoTotemModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) return;
        int slot = findItem(Items.TOTEM_OF_UNDYING);
        if (slot == -1) return;
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            slot,
            0,
            ClickType.PICKUP,
            mc.player
        );
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            45,
            0,
            ClickType.PICKUP,
            mc.player
        );
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            slot,
            0,
            ClickType.PICKUP,
            mc.player
        );
    }


    public int findItem(Item item) {
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i < 9 ? i + 36 : i;
        }
        return -1;
    }

}
