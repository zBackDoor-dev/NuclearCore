package me.darkboy.nuclear.system.register.command;

import com.google.common.collect.Lists;
import me.darkboy.nuclear.system.register.Registrable;
import me.darkboy.nuclear.system.NuclearPlugin;
import me.darkboy.nuclear.system.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author NotStxnks_
 */
public abstract class NuclearCommand implements Registrable, NuclearCommandExecutor, NuclearCommandTabCompleter {

    protected NuclearPlugin plugin;

    private String name;
    private String description;
    private String usage;
    private String permission;
    private List<String> permissionDenyMessage;
    private boolean allowConsole;
    private List<String> aliases = Lists.newArrayList();
    private List<NuclearCommand> children = Lists.newArrayList();

    private boolean sync = true;
    private NuclearCommandExecutor executor = this;
    private NuclearCommandTabCompleter tabCompleter = this;
    private NuclearCommand parent;

    @Override
    public final void register() {
        // Update children which were set in the constructor, as
        // the plugin is injected after the constructor.
        children.forEach(child -> {
            child.plugin = plugin;
            child.parent = this;

            child.register();
            child.onRegister();
        });

        if (isRoot()) {
            NuclearCommandBukkit.getCommandMap().register(plugin.getName(), new NuclearCommandBukkit(this));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }

    final void executeCalled(CommandSender sender, String[] args) {
        if (!children.isEmpty() && args.length > 0) {
            for (NuclearCommand child : children) {
                if (child.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(args[0]))) {
                    List<String> list = Lists.newArrayList(args);
                    list.remove(0);

                    child.executeCalled(sender, list.toArray(new String[0]));
                    return;
                }
            }
        }

        if (sender instanceof ConsoleCommandSender && !isAllowConsole()) {
            plugin.getConsole().error("&c" + getName() + " &rdoes not have console support.");
            return;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            if (permissionDenyMessage != null && !permissionDenyMessage.isEmpty() && (sender instanceof Player)) {
                Players.sendMessage(Players.getPlayer(sender), permissionDenyMessage);
            }

            return;
        }

        // All tests past, execute.
        if (isSync()) {
            executor.execute(sender, args);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> executor.execute(sender, args));
        }
    }

    final List<String> tabCalled(CommandSender sender, String alias, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            return null;
        }

        if (!children.isEmpty() && args.length > 0) {
            for (NuclearCommand child : children) {
                if (child.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(args[0]))) {
                    List<String> list = Lists.newArrayList(args);
                    list.remove(0);

                    return child.tabCalled(sender, alias, list.toArray(new String[0]));
                }
            }
        }

        // No children, invoke this class' tab completer
        return tabComplete(sender, alias, args);
    }

    /**
     * If the {@link NuclearCommand#name} is not present, the first alias will be returned, or
     * {@code null} if no aliases are set.
     *
     * @return command's name.
     */
    public String getName() {
        return name != null ? name : aliases.size() > 0 ? aliases.get(0) : null;
    }

    /**
     * The commands description describes what the command does, not how to use it.
     *
     * @return command's description.
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    /**
     * @return command's usage.
     */
    public String getUsage() {
        return usage != null ? usage : "/" + name;
    }

    /**
     * @return command's permission or {@code null} if none.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @return message to send to the sender if they do not have permission to
     *         execute the command.
     */
    public List<String> getPermissionDenyMessage() {
        return permissionDenyMessage;
    }

    /**
     * @return if the console is allowed to execeute the command.
     */
    public boolean isAllowConsole() {
        return allowConsole;
    }

    /**
     * @return a list of aliases, including the command's "main" executor.
     */
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    /**
     * @return list of child commands, can be empty.
     */
    public List<NuclearCommand> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * @return {@code true} if the command is ran on the main thread.
     */
    public boolean isSync() {
        return sync;
    }

    /**
     * @return the command's executor.
     */
    public NuclearCommandExecutor getExecutor() {
        return executor;
    }

    /**
     * @return the command's tab completer.
     */
    public NuclearCommandTabCompleter getTabCompleter() {
        return tabCompleter;
    }

    /**
     * @return the command's parent or {@code null} if the object is a
     *         root command.
     */
    public NuclearCommand getParent() {
        return parent;
    }

    /**
     * @return the command's root.
     */
    public NuclearCommand getRoot() {
        NuclearCommand command = this;

        while (!command.isRoot()) {
            command = command.getParent();
        }

        return command;
    }

    /**
     * @return {@code true} if the command has no parent.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * @param parent the command's parent.
     */
    public void setParent(NuclearCommand parent) {
        this.parent = parent;
    }

    /**
     * @param name command's name, usually main or common executor.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The description should not contain how to use the command.
     *
     * @param description command's description, describing what the command does.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The usage should not contain what the command does.
     *
     * @param usage command's usage, how to use it.
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * @param permission the permission required to execute the command, or null
     *                   if no permission is required.
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * @param permissionDenyMessage the messages to send to the sender if they do not have permission.
     */
    public void setPermissionDenyMessage(List<String> permissionDenyMessage) {
        this.permissionDenyMessage = permissionDenyMessage;
    }

    /**
     * @param allowConsole if the console is allowed to execute the command.
     */
    public void setAllowConsole(boolean allowConsole) {
        this.allowConsole = allowConsole;
    }

    /**
     * @param aliases new list of aliases.
     */
    public void setAliases(List<String> aliases) {
        this.aliases.clear();
        aliases.forEach(this::addAlias);
    }

    /**
     * @param aliases new list of aliases.
     */
    public void setAliases(String... aliases) {
        setAliases(Arrays.asList(aliases));
    }

    /**
     * Checks to see whether the provided alias is already present in the list,
     * if so it will not add it - this is to avoid duplicate and redundant aliases.
     *
     * @param alias the alias to add.
     */
    public void addAlias(String alias) {
        if (!aliases.contains(alias)) {
            aliases.add(alias);
        }
    }

    /**
     * @param children new list of children.
     */
    public void setChildren(List<NuclearCommand> children) {
        this.children.clear();
        children.forEach(this::addChild);
    }

    /**
     * @param children new list of children.
     */
    public void setChildren(NuclearCommand... children) {
        setChildren(Arrays.asList(children));
    }

    /**
     * @param child the child command to add.
     */
    public void addChild(NuclearCommand child) {
        if (!children.contains(child)) {
            child.plugin = plugin;
            child.parent = parent;
            children.add(child);
        }
    }

    /**
     * @param sync if the command is to be run on the main thread.
     */
    public void setSync(boolean sync) {
        this.sync = sync;
    }

    /**
     * @param executor the command's executor.
     */
    public void setExecutor(NuclearCommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * @param tabCompleter the command's tab completer.
     */
    public void setTabCompleter(NuclearCommandTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }
}