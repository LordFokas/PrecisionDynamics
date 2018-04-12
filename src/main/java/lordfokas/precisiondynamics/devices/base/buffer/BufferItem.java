package lordfokas.precisiondynamics.devices.base.buffer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BufferItem extends Buffer<BufferItem> implements IItemHandler {
    private final ItemStackHandler inventory = new ItemStackHandler(CAPACITY_ITEM);

    @Override
    public void refill(BufferItem other) {
        for(int i = 0; i < inventory.getSlots(); i++){
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.isEmpty()) continue;
            stack = merge(stack, other.inventory, true); // merge on same item stacks
            if(!stack.isEmpty())
                stack = merge(stack, other.inventory, false); // merge on empty slots
            inventory.setStackInSlot(i, stack);
        }
    }

    private ItemStack merge(ItemStack stack, IItemHandler inventory, boolean same){
        for(int i = 0; i < inventory.getSlots(); i++){
            ItemStack existing = inventory.getStackInSlot(i);
            if((same && ItemStack.areItemsEqual(existing, stack)) || (!same && existing.isEmpty())){
                stack = inventory.insertItem(i, stack, false);
            }
            if(stack.isEmpty())
                break;
        }
        return stack;
    }

    @Override public Capability getCapability() { return CAPABILITY_ITEM; }
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
