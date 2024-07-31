package com.Infinity.Nexus.Generators.block.entity;

import com.Infinity.Nexus.Core.block.entity.common.SetMachineLevel;
import com.Infinity.Nexus.Core.block.entity.common.SetUpgradeLevel;
import com.Infinity.Nexus.Core.items.custom.ComponentItem;
import com.Infinity.Nexus.Core.utils.ModUtils;
import com.Infinity.Nexus.Generators.block.custom.Refinery;
import com.Infinity.Nexus.Generators.screen.refinery.RefineryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RefineryBlockEntity extends BlockEntity implements MenuProvider {

    private static final int COMPONENT_SLOT = 0;
    private static final int[] UPGRADE_SLOTS = {1, 2, 3, 4};
    private static final int OUTPUT_SLOT = 5;

    private final ItemStackHandler itemHandler = new ItemStackHandler(6){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case COMPONENT_SLOT -> ModUtils.isComponent(stack);
                case 1,2,3,4 -> ModUtils.isUpgrade(stack);
                case OUTPUT_SLOT -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;


    public RefineryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.REFINERY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> RefineryBlockEntity.this.progress;
                    case 1 -> RefineryBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> RefineryBlockEntity.this.progress = i1;
                    case 1 -> RefineryBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.infinity_nexus_generators.refinery").append(" LV "+ getMachineLevel());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RefineryMenu(i, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }
        //Sim
        int machineLevel = getMachineLevel()-1 <= 0 ? 0 : getMachineLevel()-1;
        //Verification for machine working
        if (isRedstonePowered(blockPos, level) || !(itemHandler.getStackInSlot(COMPONENT_SLOT).isEmpty())) {
            if(blockState.getValue(Refinery.LIT) != machineLevel){
                level.setBlock(blockPos, blockState.setValue(Refinery.LIT, machineLevel), 3);
            }
            return;
        }else if(blockState.getValue(Refinery.LIT) != machineLevel+9){
            level.setBlock(blockPos, blockState.setValue(Refinery.LIT, machineLevel+9), 3);
        }
        //Machine Logic

        //increaseCraftingProgress(machineLevel);
        //setChanged(pLevel, pPos, pState);
        //if (hasProgressFinished()) {
        //    craftItem();
        //    resetProgress();
        //}
    }
    private int getMachineLevel(){
        return ModUtils.getComponentLevel(this.itemHandler.getStackInSlot(COMPONENT_SLOT));
    }
    private boolean isRedstonePowered(BlockPos pPos, Level level) {
        return level.hasNeighborSignal(pPos);
    }

//
//        if (isOutputSlotEmptyOrReceivable() && hasRecipe()) {
//            increaseCraftingProcess();
//            setChanged(level, blockPos, blockState);
//
//            if (hasProgressFinished()) {
//                craftItem();
//                resetProgress();
//            }
//        } else {
//            resetProgress();
//        }
//    }
//
//    private void craftItem() {
//        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
//
//        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(ModItems.ALEXANDRITE.get(),
//                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + 1));
//    }
//
//    private void resetProgress() {
//        this.progress = 0;
//    }
//
//    private boolean hasProgressFinished() {
//        return this.progress >= this.maxProgress;
//    }
//
//    private void increaseCraftingProcess() {
//        this.progress++;
//    }
//
//    private boolean hasRecipe() {
//        return canInsertAmountIntoOutputSlot(1) && canInsertItemIntoOutputSlot(ModItems.ALEXANDRITE.get())
//                && hasRecipeItemInInputSlot();
//    }
//
//    private boolean hasRecipeItemInInputSlot() {
//        return this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == ModItems.RAW_ALEXANDRITE.get();
//    }
//
//    private boolean canInsertItemIntoOutputSlot(Item item) {
//        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
//    }
//
//    private boolean canInsertAmountIntoOutputSlot(int count) {
//        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >=
//                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
//    }
//
//    private boolean isOutputSlotEmptyOrReceivable() {
//        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
//                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
//    }

    public void setMachineLevel(ItemStack itemStack, Player player) {
        SetMachineLevel.setMachineLevel(itemStack, player, this, COMPONENT_SLOT, this.itemHandler);
    }
    public void setUpgradeLevel(ItemStack itemStack, Player player) {
        SetUpgradeLevel.setUpgradeLevel(itemStack, player, this, UPGRADE_SLOTS, this.itemHandler);
    }
}
