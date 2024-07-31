package com.Infinity.Nexus.Generators.block.common;

import com.Infinity.Nexus.Core.items.custom.ComponentItem;
import com.Infinity.Nexus.Core.items.custom.UpgradeItem;
import com.Infinity.Nexus.Generators.block.entity.RefineryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;

public class CommonUpgrades {

    public static void setUpgrades(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        ItemStack stack = pPlayer.getMainHandItem().copy();

        //Tipos de upgrades
        boolean component = stack.getItem() instanceof ComponentItem;
        boolean upgrade = stack.getItem() instanceof UpgradeItem;
        if(pPlayer instanceof ServerPlayer ){
            //Oque fazer com cada tipo
            if (component) {
                if (Objects.requireNonNull(entity) instanceof RefineryBlockEntity be) {
                    be.setMachineLevel(stack, pPlayer);
                }
            } else if (upgrade) {
                if (Objects.requireNonNull(entity) instanceof RefineryBlockEntity be) {
                    be.setUpgradeLevel(stack, pPlayer);
                }
            }else{
                try{
                    NetworkHooks.openScreen(((ServerPlayer) pPlayer), (MenuProvider) entity, pPos);
                }catch (Exception e){
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
        }
    }
}
