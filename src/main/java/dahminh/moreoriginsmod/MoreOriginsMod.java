package dahminh.moreoriginsmod;

import dahminh.moreoriginsmod.effect.CustomEffects;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreOriginsMod implements ModInitializer {
	public static final String MOD_ID = "moreoriginsmod";
	public static final Logger LOGGER = LoggerFactory.getLogger("moreoriginsmod");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		CustomEffects.registerEffects();
	}
}