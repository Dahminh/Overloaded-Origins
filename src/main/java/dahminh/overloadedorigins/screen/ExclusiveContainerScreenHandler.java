package dahminh.overloadedorigins.screen;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.power.InventoryPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.awt.*;
import java.util.function.Predicate;


public class ExclusiveContainerScreenHandler extends ScreenHandler {

    private final Predicate<Pair<World, ItemStack>> inventoryFilter;
    private static final int NUM_COLUMNS = 9;
    private final Inventory inventory;
    private final int rows;
    private LivingEntity livingEntity;
    private InventoryPower.ContainerType containerType;

    public ExclusiveContainerScreenHandler(
            ScreenHandlerType<?> type,
            int syncId,
            PlayerInventory playerInventory,
            Inventory inventory,
            int rows,
            Predicate<Pair<World, ItemStack>> inventoryFilter,
            LivingEntity livingEntity,
            InventoryPower.ContainerType containerType
    ) {
        super(type, syncId);
        int expectedSize = switch (containerType) {
            case DOUBLE_CHEST -> 54;
            case CHEST -> 27;
            case HOPPER -> 5;
            default -> 9;
        };
        this.inventoryFilter = inventoryFilter;
        this.livingEntity = livingEntity;
        this.containerType = containerType;
        int k;
        int j;
        GenericContainerScreenHandler.checkSize(inventory, expectedSize);
        this.inventory = inventory;
        this.rows = rows;
        inventory.onOpen(playerInventory.player);
        fillSlots(rows, playerInventory);
    }

    private void fillSlots(int rows, PlayerInventory playerInventory) {
        int i;
        int j;
        int k;
        switch(this.containerType) {
            case DROPPER, DISPENSER -> {
                for (i = 0; i < 3; ++i) {
                    for (j = 0; j < 3; ++j) {
                        this.addSlot(new ExclusiveSlot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
                    }
                }
                for (i = 0; i < 3; ++i) {
                    for (j = 0; j < 9; ++j) {
                        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                    }
                }
                for (i = 0; i < 9; ++i) {
                    this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
                }
            }
            case HOPPER -> {
                for (j = 0; j < 5; ++j) {
                    this.addSlot(new ExclusiveSlot(inventory, j, 44 + j * 18, 20));
                }
                for (j = 0; j < 3; ++j) {
                    for (k = 0; k < 9; ++k) {
                        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
                    }
                }
                for (j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
                }
            }
            default -> {
                i = (this.rows - 4) * 18;
                for (j = 0; j < this.rows; ++j) {
                    for (k = 0; k < 9; ++k) {
                        this.addSlot(new ExclusiveSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
                    }
                }
                for (j = 0; j < 3; ++j) {
                    for (k = 0; k < 9; ++k) {
                        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
                    }
                }
                for (j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
                }
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.inventory.size() ?
                    !this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true) :
                    !this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }
        return itemStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return inventoryFilter.test(new Pair<>(livingEntity.getWorld(), stack));
    }

    private class ExclusiveSlot extends Slot {
        public ExclusiveSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return inventoryFilter.test(new Pair<>(livingEntity.getWorld(), stack));
        }
    }
}
