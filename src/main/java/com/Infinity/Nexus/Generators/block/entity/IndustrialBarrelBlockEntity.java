package com.Infinity.Nexus.Generators.block.entity;

import com.Infinity.Nexus.Core.block.entity.common.SetMachineLevel;
import com.Infinity.Nexus.Core.block.entity.common.SetUpgradeLevel;
import com.Infinity.Nexus.Core.utils.ModEnergyStorage;
import com.Infinity.Nexus.Core.utils.ModUtils;
import com.Infinity.Nexus.Generators.block.custom.Refinery;
import com.Infinity.Nexus.Generators.config.Config;
import com.Infinity.Nexus.Generators.recipe.RefineryRecipes;
import com.Infinity.Nexus.Generators.screen.refinery.RefineryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IndustrialBarrelBlockEntity extends BlockEntity {

    //Liquids
    private static final int FLUID_STORAGE_CAPACITY = Config.industrial_barrel_capacity;
    private final FluidTank FLUID_STORAGE = createFluidStorage();

    private FluidTank createFluidStorage() {
        return new FluidTank(FLUID_STORAGE_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public IndustrialBarrelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INDUSTRIAL_BARREL_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void drops() {

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> FLUID_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        pTag = FLUID_STORAGE.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        FLUID_STORAGE.readFromNBT(pTag);
    }

    public FluidStack getTank() {
        return this.FLUID_STORAGE.getFluid();
    }
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }


    }

    public static void modifyFluid(ItemStack itemStack, Player player, IndustrialBarrelBlockEntity blockEntity) {

        FluidStack tank = blockEntity.getTank();

        itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {


            if(iFluidHandlerItem.getContainer().getItem() instanceof BucketItem) {
                if(blockEntity.FLUID_STORAGE.getSpace() >= iFluidHandlerItem.getFluidInTank(0).getAmount()){

                    FluidStack fluidStack = iFluidHandlerItem.drain(iFluidHandlerItem.getFluidInTank(0).getAmount(), IFluidHandler.FluidAction.SIMULATE);
                    iFluidHandlerItem.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    //TODO FAZER COM QUE TIRE DO BALDE O LIQUIDO

                    blockEntity.FLUID_STORAGE.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                }
            } else if(blockEntity.FLUID_STORAGE.getSpace() >= 10 || blockEntity.FLUID_STORAGE.getSpace() >= iFluidHandlerItem.getContainer().getCount()) {

                int drainAmount = Math.min(blockEntity.FLUID_STORAGE.getSpace(), 10);
                FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                iFluidHandlerItem.drain(stack, IFluidHandler.FluidAction.EXECUTE);
                blockEntity.FLUID_STORAGE.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
            }


//
//            if (itemStack.isEmpty() && !tank.isEmpty()) {
//
//                itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
//                    if (blockEntity.FLUID_STORAGE.getFluidAmount() >= 1000) {
//                        int drainAmount = 1000;
//                        //FluidStack fluidInBucket = iFluidHandlerItem.getFluidInTank(0);
//
//                        FluidStack stack = blockEntity.FLUID_STORAGE.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
//                        stack = blockEntity.FLUID_STORAGE.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
//
//                        iFluidHandlerItem.fill(new FluidStack(stack.getFluid(), drainAmount), IFluidHandler.FluidAction.EXECUTE);
//                    }
//                });
//
//            } else if (itemStack.is(tank.getFluid().getBucket())) {
//
//                itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
//                    int drainAmount = Math.min(blockEntity.FLUID_STORAGE.getSpace(), 1000);
//                    FluidStack fluidInBucket = iFluidHandlerItem.getFluidInTank(0);
//
//                    FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
//                    stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
//
//                    blockEntity.FLUID_STORAGE.fill(new FluidStack(fluidInBucket.getFluid(), blockEntity.FLUID_STORAGE.getFluidAmount() + drainAmount), IFluidHandler.FluidAction.EXECUTE);
//                });
//
////            FluidStack fluidStack = tank.copy();
////            fluidStack.shrink(1);
////            blockEntity.FLUID_STORAGE.setFluid(fluidStack);
////
////            if (!player.isCreative()) {
////                player.getMainHandItem().shrink(1);
////            }
//            } else if (tank.isEmpty()) {
//
//                itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
//                    int drainAmount = Math.min(blockEntity.FLUID_STORAGE.getSpace(), 1000);
//                    FluidStack fluidInBucket = iFluidHandlerItem.getFluidInTank(0);
//
//                    FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
//                    stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
//
//                    blockEntity.FLUID_STORAGE.fill(new FluidStack(fluidInBucket.getFluid(), drainAmount), IFluidHandler.FluidAction.EXECUTE);
//                });
//            }
        });


    }

    public static void sendTankLevel(IndustrialBarrelBlockEntity entity, Player player) {
        player.sendSystemMessage(Component.translatable(entity.getTank().getTranslationKey()).append(Component.literal(": " + entity.getTank().getAmount() + "/" + FLUID_STORAGE_CAPACITY)));
    }

    private boolean hasRecipeFluidInInputTank(FluidStack fluid) {
         return this.FLUID_STORAGE.getFluid().getFluid() == fluid.getFluid() && this.FLUID_STORAGE.getFluid().getAmount() >= fluid.getAmount();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

}
