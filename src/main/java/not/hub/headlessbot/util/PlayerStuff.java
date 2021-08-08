package not.hub.headlessbot.util;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import not.hub.headlessbot.Cooldowns;

public class PlayerStuff implements MC {

    public static boolean hasBlocks() {
        return mc
            .player
            .inventory
            .mainInventory
            .stream()
            .filter(itemStack -> !itemStack.isEmpty())
            .map(ItemStack::getItem)
            .anyMatch(item -> item instanceof ItemBlock);
    }

    public static boolean hasPickaxe() {
        return mc
            .player
            .inventory
            .mainInventory
            .stream()
            .filter(itemStack -> !itemStack.isEmpty())
            .map(ItemStack::getItem)
            .anyMatch(item -> item instanceof ItemPickaxe);
    }

    public static boolean hasArmor() {
        return mc
            .player
            .inventory
            .mainInventory
            .stream()
            .filter(itemStack -> !itemStack.isEmpty())
            .map(ItemStack::getItem)
            .anyMatch(item -> item instanceof ItemArmor);
    }

    public static void swapSlots(int source, int target) {
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            source,
            0,
            ClickType.PICKUP,
            mc.player
        );
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            target,
            0,
            ClickType.PICKUP,
            mc.player
        );
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            source,
            0,
            ClickType.PICKUP,
            mc.player
        );
        Cooldowns.INVENTORY.reset();
    }

    public static void quickMoveSlot(int slot) {
        mc.playerController.windowClick(
            mc.player.inventoryContainer.windowId,
            slot < 9 ? slot + 36 : slot,
            0,
            ClickType.QUICK_MOVE,
            mc.player
        );
        Cooldowns.INVENTORY.reset();
    }

}
