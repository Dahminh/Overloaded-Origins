package dahminh.overloadedorigins.action.entity;

import dahminh.overloadedorigins.OverloadedOrigins;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;

public class RandomTeleport {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (entity instanceof LivingEntity living) {
            double radius = data.get("radius");
            boolean particles = data.get("particles");
            int i = 0;
            boolean tpSuccess = false;
            while (i < 10 && !tpSuccess) {
                double x = living.getX() + (living.getRandom().nextDouble() - 0.5) * radius;
                double y = living.getY() + (living.getRandom().nextInt((int) radius) - (radius / 2));
                double z = living.getZ() + (living.getRandom().nextDouble() - 0.5) * radius;
                tpSuccess = living.teleport(x,y,z, particles);
                i++;
            }
            if (tpSuccess) {
                data.<Consumer<Entity>>ifPresent("success_action", entityAction -> entityAction.accept(living));
                return;
            }
            data.<Consumer<Entity>>ifPresent("fail_action", entityAction -> entityAction.accept(living));
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            OverloadedOrigins.identifier("random_teleport"),
            new SerializableData()
                .add("radius", SerializableDataTypes.DOUBLE, 32D)
                .add("particles", SerializableDataTypes.BOOLEAN, false)
                .add("success_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("fail_action", ApoliDataTypes.ENTITY_ACTION, null),
            RandomTeleport::action
        );
    }
}
