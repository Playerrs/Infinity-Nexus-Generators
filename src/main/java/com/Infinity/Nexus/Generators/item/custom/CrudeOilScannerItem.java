package com.Infinity.Nexus.Generators.item.custom;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.math.BigInteger;
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
            getChunkPurity(pPlayer, pLevel, chunk);
            pPlayer.sendSystemMessage(Component.literal(chance + "% chance"));
        }

        pPlayer.getCooldowns().addCooldown(this, 20);                                // Adiciona cooldown
        itemStackInHand.hurtAndBreak(1, pPlayer, pPlayer1 -> pPlayer1.broadcastBreakEvent(pUsedHand)); // Tira durabilidade

        return InteractionResultHolder.sidedSuccess(itemStackInHand, pLevel.isClientSide());
    }
    private int getChunkPurity(Player pPlayer, Level pLevel, ChunkAccess pChunk) {
        Long chunkPurity = pPlayer.getServer().overworld().getSeed(); //Seed
        int seed = (int) pPlayer.getServer().overworld().getSeed(); //Valor optimizado de seed
        int[] chunkPos =  {pChunk.getPos().x, pChunk.getPos().z}; //Posicao do chunk

        pPlayer.sendSystemMessage(Component.literal("Seed: " + seed + " Chunk Pos: " + chunkPos[0] + "," + chunkPos[1]));

        return 0;
    }
}
