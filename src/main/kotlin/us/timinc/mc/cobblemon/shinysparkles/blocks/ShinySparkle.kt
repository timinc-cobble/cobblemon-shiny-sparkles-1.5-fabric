package us.timinc.mc.cobblemon.shinysparkles.blocks

import net.minecraft.block.*
import net.minecraft.particle.DustParticleEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.random.Random.Default.nextFloat


class ShinySparkle : Block(
    Settings.copy(Blocks.TORCH).dropsNothing().ticksRandomly().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque()
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

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        view: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.cuboid(0.15, 0.15, 0.15, 0.85, 0.85, 0.85)
    }

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
}