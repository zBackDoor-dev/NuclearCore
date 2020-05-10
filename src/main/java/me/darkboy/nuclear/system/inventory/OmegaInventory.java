package me.darkboy.nuclear.system.inventory;

import me.darkboy.nuclear.system.NuclearPlugin;
import me.darkboy.nuclear.system.inventory.content.InventoryContents;
import me.darkboy.nuclear.system.inventory.content.InventoryProvider;
import me.darkboy.nuclear.system.inventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class OmegaInventory {

    private String id;
    private String title;
    private InventoryType type;
    private int rows, columns;
    private boolean closeable;
    private boolean clickable;

    private InventoryProvider provider;
    private OmegaInventory parent;

    private List<InventoryListener<? extends Event>> listeners;
    private InventoryManager manager;

    private OmegaInventory(InventoryManager manager) {
        this.manager = manager;
    }

    public Inventory open(Player player) { return open(player, 0); }
    public Inventory open(Player player, int page) {
        Optional<OmegaInventory> oldInv = this.manager.getInventory(player);

        oldInv.ifPresent(inv -> {
            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                            .accept(new InventoryCloseEvent(player.getOpenInventory())));

            this.manager.setInventory(player, null);
        });

        InventoryContents contents = new InventoryContents.Impl(this, player);
        contents.pagination().page(page);

        this.manager.setContents(player, contents);
        this.provider.init(player, contents);

        InventoryOpener opener = this.manager.findOpener(type)
                .orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + type.name()));
        Inventory handle = opener.open(this, player);

        this.manager.setInventory(player, this);

        return handle;
    }

    @SuppressWarnings("unchecked")
    public void close(Player player) {
        listeners.stream()
                .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                        .accept(new InventoryCloseEvent(player.getOpenInventory())));

        this.manager.setInventory(player, null);
        player.closeInventory();

        this.manager.setContents(player, null);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public InventoryType getType() { return type; }
    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    public boolean isCloseable() { return closeable; }
    public void setCloseable(boolean closeable) { this.closeable = closeable; }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public InventoryProvider getProvider() { return provider; }
    public Optional<OmegaInventory> getParent() { return Optional.ofNullable(parent); }

    public InventoryManager getManager() { return manager; }

    List<InventoryListener<? extends Event>> getListeners() { return listeners; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {

        private String id = "unknown";
        private String title = "";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6, columns = 9;
        private boolean closeable = true;
        private boolean clickable = false;

        private InventoryManager manager;
        private InventoryProvider provider;
        private OmegaInventory parent;

        private List<InventoryListener<? extends Event>> listeners = new ArrayList<>();

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder size(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            return this;
        }

        public Builder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }

        public Builder clickable(boolean clickable) {
            this.clickable = clickable;
            return this;
        }

        public Builder provider(InventoryProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder parent(OmegaInventory parent) {
            this.parent = parent;
            return this;
        }

        public Builder listener(InventoryListener<? extends Event> listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder manager(InventoryManager manager) {
            this.manager = manager;
            return this;
        }

        public OmegaInventory build() {
            if(this.provider == null)
                throw new IllegalStateException("The provider of the OmegaInventory.Builder must be set.");

            InventoryManager manager = this.manager != null ? this.manager : NuclearPlugin.manager();

            if(manager == null)
                throw new IllegalStateException("The manager of the OmegaInventory.Builder must be set, "
                        + "or the OmegaInvs should be loaded as a plugin.");

            OmegaInventory inv = new OmegaInventory(manager);
            inv.id = this.id;
            inv.title = this.title;
            inv.type = this.type;
            inv.rows = this.rows;
            inv.columns = this.columns;
            inv.closeable = this.closeable;
            inv.clickable = this.clickable;
            inv.provider = this.provider;
            inv.parent = this.parent;
            inv.listeners = this.listeners;

            return inv;
        }
    }
}