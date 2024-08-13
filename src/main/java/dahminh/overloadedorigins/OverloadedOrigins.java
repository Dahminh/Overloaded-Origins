package dahminh.overloadedorigins;

import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.entity.OOEntities;
import dahminh.overloadedorigins.entity.custom.ShadowDecoyEntity;
import dahminh.overloadedorigins.entity.custom.SpectreEntity;
import dahminh.overloadedorigins.item.Items;
import dahminh.overloadedorigins.registry.factory.OverloadedOriginsEntityActions;
import dahminh.overloadedorigins.sound.OOSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverloadedOrigins implements ModInitializer {
	public static final String MOD_ID = "overloadedorigins";
	public static final Logger LOGGER = LoggerFactory.getLogger("overloadedorigins");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		OOEffects.registerEffects();
		Items.registerItems();
		OverloadedOriginsEntityActions.register();
		OOSounds.registerSounds();
		FabricDefaultAttributeRegistry.register(OOEntities.SHADOW_DECOY, ShadowDecoyEntity.createShadowDecoyAttributes());
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}