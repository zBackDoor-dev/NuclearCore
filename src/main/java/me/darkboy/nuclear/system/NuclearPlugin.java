package me.darkboy.nuclear.system;

import com.google.common.collect.Lists;
import me.darkboy.nuclear.system.inventory.InventoryManager;
import me.darkboy.nuclear.system.register.Registrable;
import me.darkboy.nuclear.system.resource.DefaultResourceProvider;
import me.darkboy.nuclear.system.resource.ResourceProvider;
import me.darkboy.nuclear.system.resource.yml.YamlResourceHandler;
import me.darkboy.nuclear.system.util.logging.ConsoleLog;
import me.darkboy.nuclear.system.util.logging.StaticLog;
import me.darkboy.nuclear.system.util.reflect.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * The {@link NuclearPlugin} is responsible for loading and enabling components
 * within the library.
 *
 * @author NotStxnks_
 */
public class NuclearPlugin extends JavaPlugin {

    private final List<Registrable> registers = Lists.newArrayList();

    private static ResourceProvider resourceProvider;
    protected final ConsoleLog console = new ConsoleLog();
    private static InventoryManager invManager;

    // Semi abstract

    /**
     * Called post {@link JavaPlugin#onLoad()}. Resource loading
     * should be done here.
     */
    public void load() {}

    /**
     * Called after {@link JavaPlugin#onEnable()}. Registrables
     * should be registered here.
     *
     * @see Registrable#register()
     * @see Registrable#onRegister()
     */
    public void enable() {}

    /**
     * Called after {@link JavaPlugin#onDisable()} and after all
     * registrables have been unregistered.
     * <p>
     * May not execute if an {@link Exception} is thrown while
     * unregistering registered {@link Registrable}s.
     *
     * @see Registrable#onUnregister()
     */
    public void disable() {}

    @Override
    public final void onLoad() {
        // Pre-load SystemInfo

        PluginDescriptionFile description = getDescription();

        resourceProvider = new DefaultResourceProvider(this);
        resourceProvider.addResourceHandler(new YamlResourceHandler());

        console.setFormat("[&3" + (description.getPrefix() != null ? description.getPrefix() : description.getName()) + "&r] [{nuclearcore_log_level}]: {nuclearcore_log_message}");

        execute(new StateExecutor(StateType.LOAD) {

            @Override
            public void execute() {
                load();
            }
        });
    }

    @Override
    public final void onEnable() {
        invManager = new InventoryManager(this);
        invManager.init();

        execute(new StateExecutor(StateType.ENABLE) {

            @Override
            public void execute() {
                enable();
            }
        });
    }

    @Override
    public final void onDisable() {
        execute(new StateExecutor(StateType.DISABLE) {

            @Override
            public void execute() {
                // Unregister registrables before disabling.
                registers.forEach(Registrable::onUnregister);

                disable();
            }
        });
    }

    /**
     * Registers the provided {@link Registrable} to this
     * plugin. Registers are first registered internally then
     * through implementation. Internal {@link Exception}s are
     * caught.
     * <p>
     * Injects the "plugin" field with an instance of this plugin.
     * <p>
     * Registrables are stored a list and can be retrieved with
     * unregistered with {@link #unregister(Registrable)}. Note
     * that all registers are automatically unregistered before
     * {@link #disable()} is called.
     *
     * @param registrable the registrable object to register.
     */
    public void register(Registrable registrable) {
        if (registrable == null) {
            console.error("Failed to register registrable object: &cnull&r.");
            return;
        }

        // Inject the plugin.
        if (Reflection.hasField(registrable.getClass(), "plugin")) {
            Reflection.setFieldValue("plugin", registrable, this);
        }

        try {
            registrable.register();
        } catch (Exception e) {
            console.error(String.format("Failed to register registrable object: &c%s&r.", getLoggableName(registrable)));
            console.exception(e);
            return;
        }

        // We assume that the register has successfully registered as
        // no exception was thrown.
        registers.add(registrable);

        // Finally, call the overridable onRegister method.
        registrable.onRegister();
    }

    /**
     * @param clazz the registrable class to register.
     */
    public void register(Class<? extends Registrable> clazz) {
        Registrable registrable;

        if (Reflection.isSingleton(clazz)) {
            registrable = Reflection.getSingleton(clazz);
        } else {
            registrable = Reflection.newInstance(clazz);
        }

        if (registrable != null) {
            register(registrable);
        } else {
            console.error("Failed to register registrable class: &ccould not create an instance&r.");
        }
    }

    /**
     * The provided object can either be an implementation
     * of {@link Registrable} or a class. In the case of it
     * being a class, a new instance will be created unless
     * a singleton.
     *
     * @param object the object to register.
     *
     * @see Reflection#isSingleton(Class)
     */
    @SuppressWarnings("unchecked")
    public void register(Object object) {
        if (object instanceof Registrable) {
            register((Registrable) object);
        } else if (object instanceof Class<?>) {
            Class<?> clazz = (Class<?>) object;

            if (Registrable.class.isAssignableFrom(clazz)) {
                register((Class<? extends Registrable>) clazz);
            } else {
                console.error(String.format("Failed to register &c%s &ras it does not implement &eRegistrable&r.", getLoggableName(clazz)));
            }
        } else {
            console.error(String.format("Failed to register &c%s&r: unknown object.", getLoggableName(object)));
        }
    }

    /**
     * @param objects iterable of objects to register.
     */
    public void register(Iterable<Object> objects) {
        objects.forEach(this::register);
    }

    /**
     * @param objects array of objects to register.
     */
    public void register(Object... objects) {
        Stream.of(objects).forEach(this::register);
    }

    /**
     * @param registrable the register to unregister.
     *
     * @throws IllegalArgumentException if the register is not
     * registered.
     */
    public void unregister(Registrable registrable) {
        if (isRegistered(registrable)) {
            throw new IllegalArgumentException("registrable is not registered.");
        }

        registrable.onUnregister();
    }

    /**
     * @return an unmodifiable set containing all of the registered registers.
     */
    public List<Registrable> getRegisters() {
        return Collections.unmodifiableList(registers);
    }

    /**
     * @param registrable the registrable to check.
     * @return {@code true} if the registrable is registered with
     *  this plugin.
     */
    public boolean isRegistered(Registrable registrable) {
        return registers.contains(registrable);
    }

    /**
     * @return the plugin's console logger.
     */
    public ConsoleLog getConsole() {
        return console;
    }

    // Util

    public String getLoggableName(Class<?> clazz) {
        String name = clazz.getSimpleName();
        return name.substring(0, Math.min(name.length(), 30));
    }

    public String getLoggableName(Object obj) {
        if (obj == null) {
            return "unknown";
        }

        return getLoggableName(obj.getClass());
    }

    /**
     * @return the plugin's resource provider.
     */
    public static ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    // State Execution

    private void execute(StateExecutor executor) {
        try {
            executor.execute();
        } catch (Exception e) {
            StaticLog.error("Failed to execute plugin in state &c" + executor.type.name() + "&r, exception was thrown:");
            StaticLog.exception(e);

            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private enum StateType {
        LOAD,
        ENABLE,
        DISABLE
    }

    public static InventoryManager manager() { return invManager; }

    private abstract static class StateExecutor {

        private final StateType type;

        StateExecutor(StateType type) {
            this.type = type;
        }

        /**
         * Executes code while catching any exceptions.
         *
         * @throws Exception if an exception is thrown during
         *                   execution.
         */
         abstract void execute() throws Exception;

    }
}