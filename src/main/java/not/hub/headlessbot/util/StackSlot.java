package not.hub.headlessbot.util;

import net.minecraft.item.ItemStack;

public class StackSlot {

    public final int slot;
    public final ItemStack stack;

    public StackSlot(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = stack;
    }

    public static StackSlot of(int slot, ItemStack stack) {
        return new StackSlot(slot, stack);
    }

}
