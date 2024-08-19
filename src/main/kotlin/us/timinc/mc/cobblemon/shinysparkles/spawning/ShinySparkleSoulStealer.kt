package us.timinc.mc.cobblemon.shinysparkles.spawning

import com.cobblemon.mod.common.api.events.entity.SpawnEvent
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawner
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles.config
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles.debug
import us.timinc.mc.cobblemon.shinysparkles.blocks.ShinySparklesBlocks
import us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities.ShinySparkleBlockEntity
import us.timinc.mc.cobblemon.shinysparkles.store.player.SparklesData

object ShinySparkleSoulStealer {
    fun possiblyStealSoul(spawnEvent: SpawnEvent<PokemonEntity>) {
        if (spawnEvent.ctx.world.isClient) return

        val pokemon = spawnEvent.entity.pokemon
        if (!pokemon.shiny) return

        if (spawnEvent.ctx.spawner !is PlayerSpawner) return
        val playerUuid = (spawnEvent.ctx.spawner as PlayerSpawner).uuid
        val player = spawnEvent.ctx.world.getPlayerByUuid(playerUuid) ?: return

        debug("Shiny spawned on ${player.name.string}")

        val prevSparklesData = SparklesData.getFromPlayer(player)
        if (prevSparklesData.pos != null && spawnEvent.ctx.world.getBlockState(prevSparklesData.pos).block == ShinySparklesBlocks.SHINY_SPARKLE) {
            if (config.cancelMultiples) {
                debug("Cancelling spawn as this player already has a shiny sparkle")
                spawnEvent.cancel()
            } else {
                debug("Cancelling shiny sparkle as this player already has a shiny sparkle")
            }
            return
        }

        val world = spawnEvent.ctx.world
        val pos = spawnEvent.ctx.position

        val targetPos: BlockPos.Mutable = pos.mutableCopy()
        var searchAllowance: Int = config.searchAllowance
        var currentOffset = 0
        while (world.getBlockState(targetPos).block != Blocks.AIR && searchAllowance > 0) {
            if (world.getBlockState(targetPos).block != Blocks.WATER) {
                searchAllowance--
            }
            currentOffset++
            targetPos.move(Direction.UP, 1)
        }
        if (world.getBlockState(targetPos).block != Blocks.AIR) {
            debug("Cancelling shiny sparkle, searched too far")
            return
        }

        world.setBlockState(targetPos, ShinySparklesBlocks.SHINY_SPARKLE.defaultState)

        val entity = world.getBlockEntity(targetPos) as ShinySparkleBlockEntity
        entity.player = playerUuid
        entity.pokemon = pokemon.createPokemonProperties(PokemonPropertyExtractor.ALL)
        entity.life = config.sparkleLifespan

        SparklesData.modifyForPlayer(player) { it.pos = targetPos }

        println(targetPos)
        spawnEvent.cancel()
    }
}