package us.timinc.mc.cobblemon.shinysparkles.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles.MOD_ID

abstract class PlayerCommandExecutor(private val path: List<String>) : CommandExecutor() {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val reversedPath = path.reversed()
        var lastLink = literal(reversedPath.first())
            .then(argument("player", EntityArgumentType.player()).executes(::withPlayer))
            .executes(::withoutPlayer)

        for (name in reversedPath.drop(1)) {
            val nextLink = literal(name)
            nextLink.then(lastLink)
            lastLink = nextLink
        }

        dispatcher.register(literal(MOD_ID).then(lastLink))
    }

    fun withPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return check(
            ctx, EntityArgumentType.getPlayer(ctx, "player")
        )
    }

    fun withoutPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return ctx.source.player?.let { player ->
            check(
                ctx, player
            )
        } ?: 0
    }

    abstract fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int
}