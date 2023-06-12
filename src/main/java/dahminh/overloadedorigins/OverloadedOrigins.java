package dahminh.overloadedorigins;

import dahminh.overloadedorigins.effect.CustomEffects;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverloadedOrigins implements ModInitializer {
	public static final String MOD_ID = "overloadedorigins";
	public static final Logger LOGGER = LoggerFactory.getLogger("overloadedorigins");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		CustomEffects.registerEffects();
	}
}