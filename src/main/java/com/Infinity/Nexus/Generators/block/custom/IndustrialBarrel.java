package com.Infinity.Nexus.Generators.block.custom;

import com.Infinity.Nexus.Core.items.custom.ComponentItem;
import com.Infinity.Nexus.Core.items.custom.UpgradeItem;
import com.Infinity.Nexus.Generators.block.common.CommonUpgrades;
import com.Infinity.Nexus.Generators.block.entity.ModBlockEntities;
import com.Infinity.Nexus.Generators.block.entity.IndustrialBarrelBlockEntity;
import com.Infinity.Nexus.Generators.block.entity.RefineryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IndustrialBarrel extends BaseEntityBlock {
    public static IntegerProperty LIT = IntegerProperty.create("lit", 0, 17);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;


    public IndustrialBarrel(Properties pProperties) {super(pProperties);}

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
        pBuilder.add(FACING, LIT);
    }


    // BLOCK ENTITY


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof IndustrialBarrelBlockEntity) {
                ((IndustrialBarrelBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        ItemStack stack = pPlayer.getMainHandItem().copy();

        if (!(entity instanceof IndustrialBarrelBlockEntity)) {
            return InteractionResult.PASS;
        }

        boolean bucket = stack.getItem() instanceof BucketItem;

        if (!(pPlayer instanceof ServerPlayer)) {
            if (pPlayer.getMainHandItem().isEmpty()) {
                IndustrialBarrelBlockEntity.sendTankLevel((IndustrialBarrelBlockEntity) entity, pPlayer);
            }
        } else {
            if (bucket) {
                IndustrialBarrelBlockEntity.modifyFluid(stack, pPlayer, (IndustrialBarrelBlockEntity) entity);
            }

        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IndustrialBarrelBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.INDUSTRIAL_BARREL_BLOCK_ENTITY.get(),
                (level, blockPos, blockState, industrialBarrelBlockEntity) -> industrialBarrelBlockEntity.tick(level, blockPos, blockState));
    }
}
