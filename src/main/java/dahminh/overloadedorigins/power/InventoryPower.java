package dahminh.overloadedorigins.power;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.screen.ExclusiveContainerScreenHandler;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class InventoryPower extends io.github.apace100.apoli.power.InventoryPower {

    private final ScreenHandlerFactory containerScreen;
    private final int containerSize;

    public InventoryPower(
            PowerType<?> type,
            LivingEntity entity,
            String containerTitle,
            ContainerType containerType,
            boolean shouldDropOnDeath,
            Predicate<Pair<World, ItemStack>> dropOnDeathFilter,
            Predicate<Pair<World, ItemStack>> inventoryFilter,
            boolean recoverable
    ) {
        super(type, entity, containerTitle, containerType, shouldDropOnDeath, dropOnDeathFilter, recoverable);
        switch (containerType) {
            case DOUBLE_CHEST:
                containerSize = 54;
                this.containerScreen = (i, playerInventory, playerEntity) -> new ExclusiveContainerScreenHandler(
                        ScreenHandlerType.GENERIC_9X6,
                        i,
                        playerInventory,
                        this,
                        6,
                        inventoryFilter,
                        entity,
                        containerType);
                break;
            case CHEST:
                containerSize = 27;
                this.containerScreen = (i, playerInventory, playerEntity) -> new ExclusiveContainerScreenHandler(
                        ScreenHandlerType.GENERIC_9X3,
                        i,
                        playerInventory,
                        this,
                        3,
                        inventoryFilter,
                        entity,
                        containerType);
                break;
            case HOPPER:
                containerSize = 5;
                this.containerScreen = (i, playerInventory, playerEntity) -> new ExclusiveContainerScreenHandler(
                        ScreenHandlerType.HOPPER,
                        i,
                        playerInventory,
                        this,
                        1,
                        inventoryFilter,
                        entity,
                        containerType);
                break;
            case DROPPER, DISPENSER:
            default:
                containerSize = 9;
                this.containerScreen = (i, playerInventory, playerEntity) -> new ExclusiveContainerScreenHandler(
                        ScreenHandlerType.GENERIC_3X3,
                        i,
                        playerInventory,
                        this,
                        3,
                        inventoryFilter,
                        entity,
                        containerType);
                break;
        }
    }

    @Override
    public void onUse() {

        if (this.isActive() && entity instanceof PlayerEntity player) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(containerScreen, super.getContainerTitle()));
        }

    }

    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        return !existingStack.isEmpty() &&
                ItemStack.canCombine(existingStack, stack) &&
                existingStack.isStackable() &&
                existingStack.getCount() < existingStack.getMaxCount() &&
                existingStack.getCount() < this.getMaxCountPerStack();
    }

    public int getEmptySlot() {
        for (int i = 0; i < super.getContainer().size(); ++i) {
            if (!super.getContainer().get(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    public int getOccupiedSlotWithRoomForStack(ItemStack stack) {
        for (int i = 0; i < super.getContainer().size(); ++i) {
            if (!this.canStackAddMore(super.getContainer().get(i), stack)) continue;
            return i;
        }
        return -1;
    }

    private int addStack(ItemStack stack) {
        int i = this.getOccupiedSlotWithRoomForStack(stack);
        if (i == -1) {
            i = this.getEmptySlot();
        }
        if (i == -1) {
            return stack.getCount();
        }
        return this.addStack(i, stack);
    }

    private int addStack(int slot, ItemStack stack) {
        int j;
        Item item = stack.getItem();
        int i = stack.getCount();
        ItemStack itemStack = this.getStack(slot);
        if (itemStack.isEmpty()) {
            itemStack = new ItemStack(item, 0);
            if (stack.hasNbt()) {
                itemStack.setNbt(stack.getNbt().copy());
            }
            this.setStack(slot, itemStack);
        }
        if ((j = i) > itemStack.getMaxCount() - itemStack.getCount()) {
            j = itemStack.getMaxCount() - itemStack.getCount();
        }
        if (j > this.getMaxCountPerStack() - itemStack.getCount()) {
            j = this.getMaxCountPerStack() - itemStack.getCount();
        }
        if (j == 0) {
            return i;
        }
        itemStack.increment(j);
        itemStack.setBobbingAnimationTime(5);
        return i -= j;
    }

    public boolean insertStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        try {
            if (!stack.isDamaged()) {
                int i;
                do {
                    i = stack.getCount();
                    if (slot == -1) {
                        stack.setCount(this.addStack(stack));
                        continue;
                    }
                    stack.setCount(this.addStack(slot, stack));
                } while (!stack.isEmpty() && stack.getCount() < i);
                return stack.getCount() < i;
            }
            if (slot == -1) {
                slot = this.getEmptySlot();
            }
            if (slot >= 0) {
                super.getContainer().set(slot, stack.copyAndEmpty());
                super.getContainer().get(slot).setBobbingAnimationTime(5);
                return true;
            }
            return false;
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Adding item to inventory");
            CrashReportSection crashReportSection = crashReport.addElement("Item being added");
            crashReportSection.add("Item ID", Item.getRawId(stack.getItem()));
            crashReportSection.add("Item data", stack.getDamage());
            crashReportSection.add("Item name", () -> stack.getName().getString());
            throw new CrashException(crashReport);
        }
    }

    public void offer(ItemStack stack) {
        if (!(entity instanceof PlayerEntity player)) return;
        while (!stack.isEmpty()) {
            int i = this.getOccupiedSlotWithRoomForStack(stack);
            if (i == -1) {
                i = this.getEmptySlot();
            }
            if (i == -1) {
                break;
            }
            int j = stack.getMaxCount() - this.getStack(i).getCount();
            if (!this.insertStack(i, stack.split(j)) || !(player instanceof ServerPlayerEntity));
        }
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(OverloadedOrigins.identifier("inventory"),
                new SerializableData()
                        .add("title", SerializableDataTypes.STRING, "container.inventory")
                        .add("container_type", SerializableDataType.enumValue(ContainerType.class), ContainerType.DROPPER)
                        .add("drop_on_death", SerializableDataTypes.BOOLEAN, false)
                        .add("drop_on_death_filter", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("inventory_filter", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key())
                        .add("recoverable", SerializableDataTypes.BOOLEAN, true),
                data ->
                        (powerType, livingEntity) -> {
                            InventoryPower inventoryPower = new InventoryPower(
                                    powerType,
                                    livingEntity,
                                    data.getString("title"),
                                    data.get("container_type"),
                                    data.getBoolean("drop_on_death"),
                                    data.isPresent("drop_on_death_filter") ? data.get("drop_on_death_filter") : itemStack -> true,
                                    data.isPresent("inventory_filter") ? data.get("inventory_filter") : itemStack -> true,
                                    data.getBoolean("recoverable")
                            );
                            inventoryPower.setKey(data.get("key"));
                            return inventoryPower;
                        })
                .allowCondition();
    }
}
