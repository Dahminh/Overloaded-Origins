package dahminh.overloadedorigins.registry.factory;

import dahminh.overloadedorigins.action.entity.*;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;


public class OverloadedOriginsEntityActions {
    public static void register() {

        register(RandomTeleport.getFactory());
        register(GiveAction.getFactory());
    }

    public static ActionFactory<Entity> register(ActionFactory<Entity> actionFactory) {
        return Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
