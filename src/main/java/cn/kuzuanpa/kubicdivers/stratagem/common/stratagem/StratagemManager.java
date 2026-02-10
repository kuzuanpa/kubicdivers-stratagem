package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.EmptyStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage.*;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital.*;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class StratagemManager {
    private static final Map<ResourceLocation, StratagemEntry> REGISTRY = new ConcurrentHashMap<>();
    private static final Map<String, ResourceLocation> ID_TO_KEY_MAP = new ConcurrentHashMap<>();
    public static final EmptyStratagem EMPTY = new EmptyStratagem();

    private static final Map<ResourceLocation, IStratagem> INSTANCES = new ConcurrentHashMap<>();

    private static class StratagemEntry {
        private final ResourceLocation key;
        private final Supplier<? extends IStratagem> factory;
        private final String id;
        private final String name;

        public StratagemEntry(ResourceLocation key, Supplier<? extends IStratagem> factory, String id, String name) {
            this.key = key;
            this.factory = factory;
            this.id = id;
            this.name = name;
        }
    }

    public static void init() {

        registerOrbitalStratagems();

        registerHellpodStratagems();

        REGISTRY.forEach((key, entry) -> {
            IStratagem instance = entry.factory.get();
            INSTANCES.put(key, instance);
            ID_TO_KEY_MAP.put(entry.id, key);
        });

        KubicdiversStratagemMod.LOGGER.info("Registered {} stratagems", REGISTRY.size());
    }

    private static void registerOrbitalStratagems() {
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "orbital_strike"),
                OrbitalStrikeStratagem::new
        );
        //barrages
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "airburst_barrage"),
                AirburstBarrageStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "120_barrage"),
                Barrage120mmStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "380_barrage"),
                Barrage380mmStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "fire_barrage"),
                FireBarrageStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "gatling_barrage"),
                GatlingBarrageStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "walking_barrage"),
                WalkingBarrageStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "gas_strike"),
                GasStrikeStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "auto_aim_strike"),
                AutoAimStrikeStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "ems_strike"),
                EMSStrikeStratagem::new
        );
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "smoke_strike"),
                SmokeStrikeStratagem::new
        );

    }

    private static void registerHellpodStratagems() {
        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "machine_gun_turret"),
                MachineGunTurretStratagem::new
        );

        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "ap_mine"),
                AntiPersonnelMineStratagem::new
        );

        register(
                ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "supply"),
                () -> new WeaponHellpodStratagem("supply", "Supply", new ItemStack(Items.IRON_INGOT, 64))
        );

    }
    public static synchronized void register(
            ResourceLocation key,
            Supplier<? extends IStratagem> factory) {

        Validate.notNull(key, "Key cannot be null");
        Validate.notNull(factory, "Factory cannot be null");
        String id = factory.get().getId();
        String name = factory.get().getName();

        if (REGISTRY.containsKey(key)) {
            throw new IllegalArgumentException("Stratagem already registered: " + key);
        }

        if (ID_TO_KEY_MAP.containsKey(id)) {
            throw new IllegalArgumentException("Stratagem ID already in use: " + id);
        }

        REGISTRY.put(key, new StratagemEntry(key, factory, id, name));

        KubicdiversStratagemMod.LOGGER.debug("Registered stratagem: {} -> {}", key, id);
    }

    @Nullable
    public static IStratagem getStratagem(ResourceLocation key) {
        return INSTANCES.get(key);
    }

    @Nullable
    public static IStratagem getStratagem(String id) {
        ResourceLocation key = ID_TO_KEY_MAP.get(id);
        return key != null ? INSTANCES.get(key) : null;
    }

    public static Collection<IStratagem> getAllStratagems() {
        return Collections.unmodifiableCollection(INSTANCES.values());
    }

    public static Set<ResourceLocation> getAllKeys() {
        return Collections.unmodifiableSet(REGISTRY.keySet());
    }

    @Nullable
    public static String getStratagemId(ResourceLocation key) {
        StratagemEntry entry = REGISTRY.get(key);
        return entry != null ? entry.id : null;
    }

    @Nullable
    public static String getStratagemName(ResourceLocation key) {
        StratagemEntry entry = REGISTRY.get(key);
        return entry != null ? entry.name : null;
    }

    public static boolean isValidSequence(List<IStratagem.Direction> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return false;
        }

        for (IStratagem stratagem : INSTANCES.values()) {
            if (stratagem.getSequence().equals(sequence)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public static IStratagem findStratagemBySequence(List<IStratagem.Direction> sequence) {
        for (IStratagem stratagem : INSTANCES.values()) {
            if (stratagem.getSequence().equals(sequence)) {
                return stratagem;
            }
        }
        return null;
    }

    public static synchronized void clear() {
        REGISTRY.clear();
        INSTANCES.clear();
        ID_TO_KEY_MAP.clear();
    }

    public static synchronized void reload() {
        clear();
        init();
    }
}