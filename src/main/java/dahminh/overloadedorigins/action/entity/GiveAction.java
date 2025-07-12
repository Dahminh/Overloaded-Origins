package dahminh.overloadedorigins.action.entity;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.power.InventoryPower;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Consumer;

import static io.github.apace100.apoli.util.InventoryUtil.modifyInventory;

public class GiveAction {
    public static void action(SerializableData.Instance data, Entity entity) {

        if (entity.getWorld().isClient) {
            return;
        }

        ItemStack stack = data.<ItemStack>get("stack").copy();
        if (stack.isEmpty()) {
            return;
        }

        StackReference stackReference = InventoryUtil.createStackReference(stack);
        if (data.isPresent("item_action")) {
            Consumer<Pair<World, StackReference>> itemAction = data.get("item_action");
            itemAction.accept(new Pair<>(entity.getWorld(), stackReference));
        }

        stack = stackReference.get();


        InventoryUtil.InventoryType inventoryType = data.get("inventory_type");
        switch(inventoryType) {
            case INVENTORY: {
                tryPreferredSlot:
                if (data.isPresent("preferred_slot") && entity instanceof LivingEntity livingEntity) {

                    EquipmentSlot preferredSlot = data.get("preferred_slot");
                    ItemStack stackInSlot = livingEntity.getEquippedStack(preferredSlot);

                    if (stackInSlot.isEmpty()) {
                        livingEntity.equipStack(preferredSlot, stack);
                        return;
                    }

                    if (!ItemStack.canCombine(stackInSlot, stack) || stackInSlot.getCount() >= stackInSlot.getMaxCount()) {
                        break tryPreferredSlot;
                    }

                    int itemsToGive = Math.min(stackInSlot.getMaxCount() - stackInSlot.getCount(), stack.getCount());

                    stackInSlot.increment(itemsToGive);
                    stack.decrement(itemsToGive);

                    if (stack.isEmpty()) {
                        return;
                    }

                }
            }
            case POWER: {
                if (!data.isPresent("power") || !(entity instanceof LivingEntity livingEntity)) return;

                PowerType<?> targetPowerType = data.get("power");
                Power targetPower = PowerHolderComponent.KEY.get(livingEntity).getPower(targetPowerType);

                if (!(targetPower instanceof InventoryPower inventoryPower)) return;
                if (entity instanceof PlayerEntity playerEntity) {
                    inventoryPower.offer(stack);
                    return;
                }
                break;
            }
        }

        if (entity instanceof PlayerEntity playerEntity) {
            playerEntity.getInventory().offerOrDrop(stack);
        } else {
            InventoryUtil.throwItem(entity, stack, false, false);
        }

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
                OverloadedOrigins.identifier("give"),
                new SerializableData()
                        .add("inventory_type", ApoliDataTypes.INVENTORY_TYPE, InventoryUtil.InventoryType.INVENTORY)
                        .add("power", ApoliDataTypes.POWER_TYPE, null)
                        .add("stack", SerializableDataTypes.ITEM_STACK)
                        .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                        .add("preferred_slot", SerializableDataTypes.EQUIPMENT_SLOT, null),
                GiveAction::action
        );
    }
}
