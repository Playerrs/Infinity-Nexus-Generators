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


public class BarrelBlockEntity extends BlockEntity {

    //Liquids
    private static int FLUID_STORAGE_CAPACITY = Config.barrel_capacity;

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

    public BarrelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BARREL_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

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
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }


    }

    public void modifyFluid(ItemStack itemStack, Player player, InteractionHand hand) {
        itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
            FluidStack tank = FLUID_STORAGE.getFluid();
            int amount = Math.min(iFluidHandlerItem.getFluidInTank(0).getAmount(), 1000);
            FluidStack fluidStack = iFluidHandlerItem.drain(iFluidHandlerItem.getFluidInTank(0).getAmount(), IFluidHandler.FluidAction.SIMULATE);
            if(tank.isEmpty() || (fluidStack.getFluid().isSame(tank.getFluid())) && tank.getAmount()+amount <= FLUID_STORAGE.getCapacity()) {
                //player.sendSystemMessage(Component.literal("Filling tank..."));
                emptyBucket(this.getBlockState(), this.getLevel(), this.getBlockPos(), player, hand, tank.getFluid().getBucket().getDefaultInstance());
                FLUID_STORAGE.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            }else if(FLUID_STORAGE.getFluid().getAmount() >= 1000 && iFluidHandlerItem.getFluidInTank(0).isEmpty()){
                //player.sendSystemMessage(Component.literal("Filling bucket..."));
                fillBucket(this.getBlockState(), this.getLevel(), this.getBlockPos(), player, hand, itemStack, tank.getFluid().getBucket().getDefaultInstance());
                FLUID_STORAGE.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    private void fillBucket(BlockState pBlockState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack pEmptyStack, ItemStack pFilledStack) {
        if (!pLevel.isClientSide) {
            Item $$9 = pEmptyStack.getItem();
            if (!pPlayer.isCreative()) {
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pEmptyStack, pPlayer, pFilledStack));
            }
            pLevel.playSound(null, pPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, pPos);
        }
    }

    private void emptyBucket(BlockState pBlockState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack pFilledStack) {
        if (!pLevel.isClientSide) {
            Item $$7 = pFilledStack.getItem();
            if (!pPlayer.isCreative()) {
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pFilledStack, pPlayer, new ItemStack(Items.BUCKET)));
            }
            pLevel.playSound((Player)null, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent((Entity)null, GameEvent.FLUID_PLACE, pPos);
        }
    }

    public static void sendTankLevel(BarrelBlockEntity entity, Player player) {
        player.sendSystemMessage(Component.translatable(entity.getTank().getFluid().getTranslationKey())
                .append(Component.literal(": " + entity.getTank().getFluid().getAmount() + "/" + FLUID_STORAGE_CAPACITY)));
    }

    public void fillFluidFromNBT(FluidStack stack) {
        if(!stack.isEmpty()) {
            FLUID_STORAGE.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        }
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
