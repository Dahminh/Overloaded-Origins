package dahminh.overloadedorigins.registry.factory;

import dahminh.overloadedorigins.power.InventoryPower;
import dahminh.overloadedorigins.power.ParticlePower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class OverloadedOriginsPowerFactories {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(ParticlePower::createFactory);
        register(InventoryPower::createFactory);
    }

    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                powerFactory.getSerializerId(),
                powerFactory
        );
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }

}
