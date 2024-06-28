package dev.andante.teamtextresolve.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeamCommand;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TeamCommand.class)
public abstract class TeamCommandMixin {
    @Shadow
    private static int executeModifyPrefix(ServerCommandSource source, Team team, Text prefix) {
        throw new AssertionError();
    }

    @Shadow
    private static int executeModifySuffix(ServerCommandSource source, Team team, Text suffix) {
        throw new AssertionError();
    }

    @Redirect(
            method = "method_13717",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/command/TeamCommand;executeModifyPrefix(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/scoreboard/Team;Lnet/minecraft/text/Text;)I"
            )
    )
	private static int modifyPrefix(ServerCommandSource source, Team team, Text prefix) {
        return executeModifyPrefix(source, team, parseLine(prefix, source));
    }

	@Redirect(
            method = "method_13712",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/command/TeamCommand;executeModifySuffix(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/scoreboard/Team;Lnet/minecraft/text/Text;)I"
            )
    )
	private static int modifySuffix(ServerCommandSource source, Team team, Text suffix) {
        return executeModifySuffix(source, team, parseLine(suffix, source));
    }

    @Unique
    private static Text parseLine(Text text, ServerCommandSource source) {
        try {
            return Texts.parse(source, text, source.getEntity(), 0);
        } catch (CommandSyntaxException ignored) {
        }

        return text;
    }
}
