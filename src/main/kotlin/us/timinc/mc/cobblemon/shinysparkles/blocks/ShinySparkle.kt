package us.timinc.mc.cobblemon.shinysparkles.blocks

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.DustParticleEffect
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities.ShinySparkleBlockEntity
import us.timinc.mc.cobblemon.shinysparkles.store.player.SparklesData
import kotlin.random.Random.Default.nextFloat


class ShinySparkle : BlockWithEntity(
    Settings.copy(Blocks.BEDROCK).dropsNothing().ticksRandomly().noBlockBreakParticles()
) {
    companion object {
        val PARTICLE = DustParticleEffect(Vec3d.unpackRgb(0xDEDEDE).toVector3f(), 1.0F)
    }

    override fun randomDisplayTick(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
        super.randomDisplayTick(blockState, world, blockPos, random)
        spawnParticles(world, blockPos)
    }

    override fun hasRandomTicks(blockState: BlockState): Boolean {
        return true
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        view: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.cuboid(0.15, 0.15, 0.15, 0.85, 0.85, 0.85)
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.INVISIBLE
    }

    private fun spawnParticles(world: World, blockPos: BlockPos) {
        val directions = Direction.values()
        val offset = 0.5625
        val middle = 0.5

        directions.forEach { direction ->
            val offsetPos = blockPos.offset(direction)
            if (!world.getBlockState(offsetPos).isOpaqueFullCube(world, offsetPos)) {
                val axis = direction.axis
                val x = if (axis == Direction.Axis.X) middle + offset * direction.offsetX else nextFloat().toDouble()
                val y = if (axis == Direction.Axis.Y) middle + offset * direction.offsetY else nextFloat().toDouble()
                val z = if (axis == Direction.Axis.Z) middle + offset * direction.offsetZ else nextFloat().toDouble()

                world.addParticle(
                    PARTICLE, blockPos.x + x, blockPos.y + y, blockPos.z + z, 0.0, 0.0, 0.0
                )
            }
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ShinySparkleBlockEntity(pos, state)
    }

    override fun onUse(
        blockState: BlockState,
        world: World,
        blockPos: BlockPos,
        playerEntity: PlayerEntity,
        hand: Hand,
        blockHitResult: BlockHitResult
    ): ActionResult {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS
        val player = (playerEntity.uuid)
        val blockEntity = (world.getBlockEntity(blockPos) as ShinySparkleBlockEntity)
        val blockPlayer = (blockEntity.player)
        if (player != blockPlayer) {
            if (!world.isClient) {
                playerEntity.sendMessage(Text.literal("That's not yours."))
            }
            return ActionResult.FAIL
        }

        val properties = blockEntity.pokemon ?: return ActionResult.PASS

        val prevSparklesData = SparklesData.getFromPlayer(playerEntity)
        prevSparklesData.pos = null

        if (!world.isClient) {
            val spawned = PokemonEntity(world, properties.create())
            spawned.setPosition(blockPos.toCenterPos())
            world.spawnEntity(spawned)
            println(spawned.pos)
        }

        world.setBlockState(blockPos, Blocks.AIR.defaultState)

        return ActionResult.SUCCESS
    }
}