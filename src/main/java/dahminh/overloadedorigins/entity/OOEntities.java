package dahminh.overloadedorigins.entity;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.entity.custom.ShadowDecoyEntity;
import dahminh.overloadedorigins.entity.custom.SpectreEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OOEntities {
    public static final EntityType<SpectreEntity> SPECTRE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(OverloadedOrigins.MOD_ID, "spectre"),
            FabricEntityTypeBuilder.<SpectreEntity>create(SpawnGroup.MISC, SpectreEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 1.15f)).build());

    public static final EntityType<ShadowDecoyEntity> SHADOW_DECOY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(OverloadedOrigins.MOD_ID, "shadow_decoy"),
            FabricEntityTypeBuilder.<ShadowDecoyEntity>create(SpawnGroup.CREATURE, ShadowDecoyEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 1.8f)).build());
}
