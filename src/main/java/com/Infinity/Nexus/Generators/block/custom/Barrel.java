package com.Infinity.Nexus.Generators.block.custom;

import com.Infinity.Nexus.Generators.block.entity.BarrelBlockEntity;
import com.Infinity.Nexus.Generators.block.entity.ModBlockEntities;
import com.Infinity.Nexus.Generators.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Barrel extends IndustrialBarrel{

    public Barrel(Properties pProperties) {
        super(pProperties);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof BarrelBlockEntity) {
                ((BarrelBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        ItemStack stack = pPlayer.getMainHandItem();

        if (!(entity instanceof BarrelBlockEntity be)) {
            return InteractionResult.PASS;
        }

        boolean bucket = stack.getItem() instanceof BucketItem;

        if (!(pPlayer instanceof ServerPlayer)) {
            if (pPlayer.getMainHandItem().isEmpty()) {
                be.sendTankLevel((BarrelBlockEntity) entity, pPlayer);
            }
        } else {
            if (bucket) {
                be.modifyFluid(stack, pPlayer, pHand);
            }

        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

//    @Override
//    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
//        FluidStack stack = FluidStack.loadFluidStackFromNBT(pStack.getTagElement("Fluid"));
//        BarrelBlockEntity entity =  (BarrelBlockEntity) pLevel.getBlockEntity(pPos);
//        if(entity != null && !pLevel.isClientSide()) {
//            entity.fillFluidFromNBT(stack);
//        }
//        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
//    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BarrelBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.BARREL_BLOCK_ENTITY.get(),
                (level, blockPos, blockState, barrelBlockEntity) -> barrelBlockEntity.tick(level, blockPos, blockState));
    }
}
