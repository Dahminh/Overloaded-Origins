package dahminh.overloadedorigins.entity.client;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class OOModelLayers {
    public static final EntityModelLayer SPECTRE =
            new EntityModelLayer(new Identifier(OverloadedOrigins.MOD_ID, "spectre"), "main");

    public static final EntityModelLayer SHADOW_DECOY =
            new EntityModelLayer(new Identifier(OverloadedOrigins.MOD_ID, "shadow_decoy"), "main");
}
