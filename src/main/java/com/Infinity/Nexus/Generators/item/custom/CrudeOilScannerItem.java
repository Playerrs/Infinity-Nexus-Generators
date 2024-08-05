package com.Infinity.Nexus.Generators.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Random;

public class CrudeOilScannerItem extends Item {
    public CrudeOilScannerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStackInHand = pPlayer.getItemInHand(pUsedHand);

        if(!pLevel.isClientSide()) {
            int chance = new Random().nextInt(100);

            ChunkAccess chunk = pLevel.getChunkAt(pPlayer.blockPosition());
            //pPlayer.sendSystemMessage(Component.literal(String.valueOf(chunk.getStatus().getIndex()))); // pq da sempre 11?

            //Função que pega o purity do chunk
            int purity = getChunkPurity(pPlayer, pLevel, chunk); // Ta funcionando mas vai até 12 kkkkk
            pPlayer.sendSystemMessage(Component.literal(purity + " of purity"));
        }

        pPlayer.getCooldowns().addCooldown(this, 20);// Adiciona cooldown //TODO AUMENTAR COOLDOWN para 100
        itemStackInHand.hurtAndBreak(1, pPlayer, pPlayer1 -> pPlayer1.broadcastBreakEvent(pUsedHand)); // Tira durabilidade


        return InteractionResultHolder.sidedSuccess(itemStackInHand, pLevel.isClientSide());
    }


    private int getChunkPurity(Player pPlayer, Level pLevel, ChunkAccess pChunk) {
        Long chunkPurity = pPlayer.getServer().overworld().getSeed(); //Seed
        int seed = (int) pPlayer.getServer().overworld().getSeed(); //Valor otimizado de seed // 510337443
        int[] chunkPos =  {pChunk.getPos().x, pChunk.getPos().z}; //Posicao do chunk

        int seedLength = String.valueOf(seed).length();
        int lastSeedNumber = Integer.parseInt(String.valueOf(seed).substring(seedLength - 1));

        int lastPosXNumber = Integer.parseInt(String.valueOf(chunkPos[0]).substring(String.valueOf(chunkPos[0]).length() - 1));
        int lastPosZNumber = Integer.parseInt(String.valueOf(chunkPos[1]).substring(String.valueOf(chunkPos[1]).length() - 1));

        //int XPurity = (lastSeedNumber * lastPosXNumber);
        //int ZPurity = (lastSeedNumber * lastPosZNumber);
        //pPlayer.sendSystemMessage(Component.literal(lastPosXNumber + " " + lastPosZNumber));


        return (lastPosZNumber * 5) + (lastPosXNumber * 5);
    }

}
