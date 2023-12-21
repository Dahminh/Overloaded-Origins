package dahminh.overloadedorigins.item;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Items {
    public static final Item ELDER_DARK_ELF_DAGGER = registerItem("elder_dark_elf_dagger", new Item(new FabricItemSettings()));
    public static final Item HIGH_DARK_ELF_DAGGER = registerItem("high_dark_elf_dagger", new Item(new FabricItemSettings()));
    public static final Item DARK_ELF_DAGGER = registerItem("dark_elf_dagger", new Item(new FabricItemSettings()));
    public static final Item SHULK_MONOLITH_SHELL = registerItem("shulk_monolith_shell", new ShulkShellItems(new FabricItemSettings()
            .rarity(Rarity.EPIC), 10));
    public static final Item SHULK_IRONCLAD_SHELL = registerItem("shulk_ironclad_shell", new ShulkShellItems(new FabricItemSettings()
            .rarity(Rarity.RARE), 10));
    public static final Item SHULK_SHELL = registerItem("shulk_shell", new ShulkShellItems(new FabricItemSettings(), 10));
    public static final Item JOKE_SHELL = registerItem("joke_shell", new ShulkShellItems(new FabricItemSettings()
            .rarity(Rarity.EPIC), 100));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(OverloadedOrigins.MOD_ID, name), item);
    }
    public static void registerItems() {
        OverloadedOrigins.LOGGER.info("Registering Mod Items for " + OverloadedOrigins.MOD_ID);
    }
}
