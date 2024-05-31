package us.timinc.mc.cobblemon.shinysparkles.spawning

import com.cobblemon.mod.common.api.events.entity.SpawnEvent
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawner
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import us.timinc.mc.cobblemon.shinysparkles.blocks.ShinySparklesBlocks
import us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities.ShinySparkleBlockEntity
import us.timinc.mc.cobblemon.shinysparkles.store.player.ShinyPos

object ShinySparkleSoulStealer {
    fun possiblyStealSoul(spawnEvent: SpawnEvent<PokemonEntity>) {
        if (spawnEvent.ctx.spawner !is PlayerSpawner) return
        val playerUuid = (spawnEvent.ctx.spawner as PlayerSpawner).uuid
        val player = spawnEvent.ctx.world.getPlayerByUuid(playerUuid) ?: return

        val prevShinyPos = ShinyPos.getFromPlayer(player)
        if (prevShinyPos.pos != null) {
            println("Greedy")
            spawnEvent.cancel()
            return
        }

        val pokemon = spawnEvent.entity.pokemon
        if (!pokemon.shiny) return

        val world = spawnEvent.ctx.world
        val pos = spawnEvent.ctx.position

        val targetPos: BlockPos = if (world.getBlockState(pos).block == Blocks.WATER) {
            pos
        } else if (world.getBlockState(pos.up()).block == Blocks.AIR) {
            pos.up()
        } else if (world.getBlockState(pos.up(2)).block == Blocks.AIR) {
            pos.up(2)
        } else {
            return
        }

        world.setBlockState(targetPos, ShinySparklesBlocks.SHINY_SPARKLE.defaultState)

        val entity = world.getBlockEntity(targetPos) as ShinySparkleBlockEntity
        entity.player = playerUuid
        entity.pokemon = pokemon.createPokemonProperties(PokemonPropertyExtractor.ALL)
        prevShinyPos.pos = targetPos
        println(targetPos)
        spawnEvent.cancel()
    }
}