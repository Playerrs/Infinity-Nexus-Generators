package com.Infinity.Nexus.Generators.block.custom;


import com.Infinity.Nexus.Generators.block.entity.FractionatingTankBlockEntity;
import com.Infinity.Nexus.Generators.block.entity.IndustrialBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class FractionatingTank extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public FractionatingTank(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape();
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.125, 0.875, 0.9375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.0625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.03125, 0.0625, 0.84375, 0.09375, 0.9375, 0.90625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.03125, 0.03125, 0.46875, 0.09375, 0.53125, 0.53125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.03125, 0.03125, 0.28125, 0.09375, 0.34375, 0.34375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.09375, 0.46875, 0.46875, 0.15625, 0.53125, 0.53125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.09375, 0.28125, 0.28125, 0.15625, 0.34375, 0.34375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.03125, 0.0625, 0.09375, 0.09375, 0.9375, 0.15625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.90625, 0.0625, 0.09375, 0.96875, 0.9375, 0.15625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.90625, 0.0625, 0.84375, 0.96875, 0.9375, 0.90625), BooleanOp.OR);

        return shape;
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
