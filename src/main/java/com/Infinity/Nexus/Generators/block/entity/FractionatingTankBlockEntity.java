package com.Infinity.Nexus.Generators.block.entity;

import com.Infinity.Nexus.Generators.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class FractionatingTankBlockEntity extends BlockEntity {

    //Liquids
    private static final int FLUID_STORAGE_CAPACITY = 10000;

    private final FluidTank FLUID_STORAGE = new FluidTank(FLUID_STORAGE_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!getLevel().isClientSide) {
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return super.isFluidValid(stack);
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public FractionatingTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FRACTIONATING_TANK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    //TODO TESTA AI PLAYER
    public void drops() {
        if(!FLUID_STORAGE.getFluid().isEmpty()) {
            ItemStack itemStack = new ItemStack(this.getBlockState().getBlock().asItem());
            itemStack.addTagElement("Fluid", FLUID_STORAGE.getFluid().writeToNBT(new CompoundTag()));
            ItemEntity itemTank = new ItemEntity(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), itemStack);
            this.level.addFreshEntity(itemTank);
        }else{
            ItemEntity itemTank = new ItemEntity(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), new ItemStack(this.getBlockState().getBlock().asItem()));
            this.level.addFreshEntity(itemTank);
        }
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

    public FluidTank getTank() {
        return this.FLUID_STORAGE;
    }

    public void fillFluidFromNBT(FluidStack stack) {
        if(!stack.isEmpty()) {
            FLUID_STORAGE.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        }
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
