package dahminh.overloadedorigins.util;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Tags {
    public static class Items {
        public static final TagKey<Item> SHULK_SHELLS =
                createTag("shulk_shells");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(OverloadedOrigins.MOD_ID, name));
        }
    }
}
