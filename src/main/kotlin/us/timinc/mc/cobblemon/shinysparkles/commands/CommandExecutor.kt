package us.timinc.mc.cobblemon.shinysparkles.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource

abstract class CommandExecutor {
    abstract fun register(dispatcher: CommandDispatcher<ServerCommandSource>)
}