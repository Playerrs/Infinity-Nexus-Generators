package com.Infinity.Nexus.Generators.block.custom;


import com.Infinity.Nexus.Generators.block.entity.FractionatingTankBlockEntity;
import com.Infinity.Nexus.Generators.block.entity.IndustrialBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FractionatingTank extends BaseEntityBlock {

    public FractionatingTank(Properties pProperties) {
        super(pProperties);
    }

    // BLOCK ENTITY

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof FractionatingTankBlockEntity) {
                ((FractionatingTankBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //BlockEntity entity = pLevel.getBlockEntity(pPos);
        //ItemStack stack = pPlayer.getMainHandItem();
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        FluidStack stack = FluidStack.loadFluidStackFromNBT(pStack.getTagElement("Fluid"));
        FractionatingTankBlockEntity entity =  (FractionatingTankBlockEntity)pLevel.getBlockEntity(pPos);
        if(entity != null && !pLevel.isClientSide()) {
            entity.fillFluidFromNBT(stack);
        }
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FractionatingTankBlockEntity(blockPos, blockState);
    }
}
