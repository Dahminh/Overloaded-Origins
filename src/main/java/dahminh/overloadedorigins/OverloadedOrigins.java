package dahminh.overloadedorigins;

import dahminh.overloadedorigins.effect.CustomEffects;
import dahminh.overloadedorigins.item.Items;
import dahminh.overloadedorigins.registry.factory.OverloadedOriginsEntityActions;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverloadedOrigins implements ModInitializer {
	public static final String MOD_ID = "overloadedorigins";
	public static final Logger LOGGER = LoggerFactory.getLogger("overloadedorigins");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		CustomEffects.registerEffects();
		Items.registerItems();
		OverloadedOriginsEntityActions.register();
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}