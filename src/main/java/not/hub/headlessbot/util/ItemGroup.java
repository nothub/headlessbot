package not.hub.headlessbot.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public enum ItemGroup {

    /*
        public static final Block WHITE_SHULKER_BOX;
    public static final Block ORANGE_SHULKER_BOX;
    public static final Block MAGENTA_SHULKER_BOX;
    public static final Block LIGHT_BLUE_SHULKER_BOX;
    public static final Block YELLOW_SHULKER_BOX;
    public static final Block LIME_SHULKER_BOX;
    public static final Block PINK_SHULKER_BOX;
    public static final Block GRAY_SHULKER_BOX;
    public static final Block SILVER_SHULKER_BOX;
    public static final Block CYAN_SHULKER_BOX;
    public static final Block PURPLE_SHULKER_BOX;
    public static final Block BLUE_SHULKER_BOX;
    public static final Block BROWN_SHULKER_BOX;
    public static final Block GREEN_SHULKER_BOX;
    public static final Block RED_SHULKER_BOX;
    public static final Block BLACK_SHULKER_BOX;
    public static final Block WHITE_GLAZED_TERRACOTTA;
    public static final Block ORANGE_GLAZED_TERRACOTTA;
    public static final Block MAGENTA_GLAZED_TERRACOTTA;
    public static final Block LIGHT_BLUE_GLAZED_TERRACOTTA;
    public static final Block YELLOW_GLAZED_TERRACOTTA;
    public static final Block LIME_GLAZED_TERRACOTTA;
    public static final Block PINK_GLAZED_TERRACOTTA;
    public static final Block GRAY_GLAZED_TERRACOTTA;
    public static final Block SILVER_GLAZED_TERRACOTTA;
    public static final Block CYAN_GLAZED_TERRACOTTA;
    public static final Block PURPLE_GLAZED_TERRACOTTA;
    public static final Block BLUE_GLAZED_TERRACOTTA;
    public static final Block BROWN_GLAZED_TERRACOTTA;
    public static final Block GREEN_GLAZED_TERRACOTTA;
    public static final Block RED_GLAZED_TERRACOTTA;
    public static final Block BLACK_GLAZED_TERRACOTTA;
     */

    MINEABLE_HAND(
        Blocks.GRASS,
        Blocks.DIRT,
        Blocks.PLANKS,
        Blocks.LOG,
        Blocks.LOG2,
        Blocks.SPONGE,
        Blocks.SLIME_BLOCK,
        Blocks.TNT,
        Blocks.NOTEBLOCK,
        Blocks.STICKY_PISTON,
        Blocks.PISTON,
        Blocks.WOOL,
        Blocks.CRAFTING_TABLE,
        Blocks.JUKEBOX,
        Blocks.SOUL_SAND,
        Blocks.MYCELIUM,
        Blocks.HAY_BLOCK,
        Blocks.NETHER_WART_BLOCK
    );

    public final Set<ResourceLocation> names;
    public final Set<Block> blocks;
    public final Set<Item> items;

    ItemGroup(Block... blocks) {
        this.names = Collections.unmodifiableSet(Arrays.stream(blocks).map(IForgeRegistryEntry.Impl::getRegistryName).collect(Collectors.toSet()));
        this.blocks = Collections.unmodifiableSet(names.stream().map(Block.REGISTRY::getObject).collect(Collectors.toSet()));
        this.items = Collections.unmodifiableSet(names.stream().map(Item.REGISTRY::getObject).collect(Collectors.toSet()));
    }

}
