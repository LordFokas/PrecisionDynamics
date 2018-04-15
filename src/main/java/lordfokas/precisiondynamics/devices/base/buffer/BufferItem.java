package lordfokas.precisiondynamics.devices.base.buffer;

import lordfokas.precisiondynamics.PrecisionDynamics;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BufferItem extends Buffer<IItemHandler, BufferItem> implements IItemHandler {
    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> CAPABILITY_ITEM;
    private static final int CAPACITY_ITEM = 16;

    private final ItemStackHandler inventory = new ItemStackHandler(CAPACITY_ITEM);

    @Override public void refill(BufferItem other) { pushInto(other.inventory); }
    @Override public void pushInto(IItemHandler capability) { movedOut(refill(inventory, capability), true); }
    @Override public void pullFrom(IItemHandler capability) { movedIn(refill(capability, inventory), true); }

    private int refill(IItemHandler from, IItemHandler into){
        int moved = 0;
        for(int i = 0; i < from.getSlots(); i++){
            int sim = merge(from, into, i, 64, true);
            if(sim == 0) continue;
            moved += merge(from, into, i, sim, false);
        }
        return moved;
    }

    private int merge(IItemHandler from, IItemHandler into, int slot, int amount, boolean sim){
        int moved = 0;
        ItemStack stack = from.extractItem(slot, amount, sim);
        if(stack.isEmpty()) return 0;
        moved += stack.getCount();
        stack = merge(stack, into, true, sim); // merge on same item stacks
        if(!stack.isEmpty())
            stack = merge(stack, into, false, sim); // merge on empty slots
        moved -= stack.getCount();
        if(!sim && stack.getCount() != 0){
            PrecisionDynamics.logger.error("Error moving stack!");
            PrecisionDynamics.logger.error(from);
            PrecisionDynamics.logger.error(into);
            PrecisionDynamics.logger.error(slot);
            PrecisionDynamics.logger.error(amount);
            PrecisionDynamics.logger.error(stack);
        }
        return moved;
    }

    private ItemStack merge(ItemStack stack, IItemHandler inventory, boolean same, boolean sim){
        for(int i = 0; i < inventory.getSlots(); i++){
            ItemStack existing = inventory.getStackInSlot(i);
            if((same && ItemStack.areItemsEqual(existing, stack)) || (!same && existing.isEmpty())){
                stack = inventory.insertItem(i, stack, sim);
            }
            if(stack.isEmpty())
                break;
        }
        return stack;
    }

    @Override public Capability<IItemHandler> getCapability() { return CAPABILITY_ITEM; }
    @Override public NBTTagCompound serialize() { return inventory.serializeNBT(); }
    @Override public void deserialize(NBTTagCompound data) { inventory.deserializeNBT(data); }
    @Override public int getSlots() { return inventory.getSlots(); }
    @Override public int getSlotLimit(int i) { return inventory.getSlotLimit(i); }
    @Nonnull @Override public ItemStack getStackInSlot(int i) { return inventory.getStackInSlot(i); }

    @Nonnull @Override
    public ItemStack insertItem(int i, @Nonnull ItemStack itemStack, boolean sim) {
        if(!canInput) return itemStack;
        ItemStack inserted = inventory.insertItem(i, itemStack, sim);
        movedIn(itemStack.getCount() - inserted.getCount(), !sim);
        return inserted;
    }

    @Nonnull @Override
    public ItemStack extractItem(int i, int i1, boolean sim) {
        if(!canOutput) return ItemStack.EMPTY;
        ItemStack extracted = inventory.extractItem(i, i1, sim);
        movedOut(extracted.getCount(), !sim);
        return extracted;
    }
}
