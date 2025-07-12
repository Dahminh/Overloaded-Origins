package dahminh.overloadedorigins.stat;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import static net.minecraft.stat.Stats.CUSTOM;

public class OOStats {

    public static final Identifier DAMAGE_DEALT_TOTAL = new Identifier(OverloadedOrigins.MOD_ID, "damage_dealt_total");

    private static void registerStat(String name, Identifier identifier, StatFormatter statFormatter) {
        Registry.register(Registries.CUSTOM_STAT, name, identifier);
        CUSTOM.getOrCreateStat(identifier, statFormatter);
    }

    public static void registerStats() {
        registerStat(DAMAGE_DEALT_TOTAL.getPath(), DAMAGE_DEALT_TOTAL, StatFormatter.DIVIDE_BY_TEN);
        OverloadedOrigins.LOGGER.info("Registering Stats for " + OverloadedOrigins.MOD_ID);
    }
}
