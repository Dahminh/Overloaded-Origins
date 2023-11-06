package dahminh.overloadedorigins.sound;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class OOSounds {
    public static final SoundEvent DARK_ELF_DASH = registerSoundEvent("dark_elf_dash");
    public static final SoundEvent DARK_ELF_DODGE = registerSoundEvent("dark_elf_dodge");
    public static final SoundEvent DARK_ELF_VANISHES = registerSoundEvent("dark_elf_vanishes");
    public static final SoundEvent DARK_ELF_APPEARS = registerSoundEvent("dark_elf_appears");
    public static final SoundEvent DARK_ELF_AMBIENT = registerSoundEvent("dark_elf_ambient");



    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(OverloadedOrigins.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        OverloadedOrigins.LOGGER.info("Registering Sounds for " + OverloadedOrigins.MOD_ID);
    }

}
