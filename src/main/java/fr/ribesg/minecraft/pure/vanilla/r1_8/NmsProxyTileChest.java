package fr.ribesg.minecraft.pure.vanilla.r1_8;

import fr.ribesg.minecraft.pure.common.Log;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import r1_8.net.minecraft.server.*;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends bcr {

    private final Inventory inv;

    public NmsProxyTileChest(final Chest chest) {
        this.inv = chest.getBlockInventory();
    }

    /*
     * Note that (bcr) implements (vq).
     * (bcr)           is the obfuscated class  name of TileEntityChest
     * (vq)            is the obfuscated class  name of IInventory
     * (amj)           is the obfuscated class  name of ItemStack
     * (vq.a(int,amj)) is the obfuscated method name of IInventory.setInventorySlotContents(int, ItemStack)
     */
    @Override
    public void a(final int index, final amj amjArg) {
        /*
         * (alq)        is the obfuscated class  name of Item
         * (alq.b(alq)) is the obfuscated method name of Item.getIdFromItem(Item)
         * (amj.b())    is the obfuscated method name of ItemStack.getItem()
         * (amj.b)      is the obfuscated field  name of ItemStack.stackSize
         * (amj.h())    is the obfuscated method name of ItemStack.getItemDamage()
         */
        @SuppressWarnings("deprecation")
        final ItemStack item = new ItemStack(
            alq.b(amjArg.b()),
            amjArg.b,
            (short) amjArg.h()
        );

        /*
         * (fn)           is the obfuscated class  name of NBTTagCompound
         * (amj.o())      is the obfuscated method name of ItemStack.getTagCompound()
         * (fv)           is the obfuscated class  name of NBTTagList
         * (fn.a(String)) is the obfuscated method name of NBTTagCompound.getTag(String)
         * (fv.c())       is the obfuscated method name of NBTTagList.tagCount()
         * (fv.b(int))    is the obfuscated method name of NBTTagList.getCompoundTagAt(int)
         */
        // Enchanted Books can be generated, let's handle that.
        if (item.getType() == Material.ENCHANTED_BOOK) {
            final fn itemNbt = amjArg.o();
            if (itemNbt != null) {
                final fv storedEnchNbt = (fv) itemNbt.a("StoredEnchantments");
                if (storedEnchNbt != null && storedEnchNbt.c() > 0) {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    for (int i = 0; i < storedEnchNbt.c(); i++) {
                        this.addMcEnchant(meta, storedEnchNbt.b(i));
                    }
                    item.setItemMeta(meta);
                }
            }
        }

        this.inv.setItem(index, item);
    }

    /*
     * (fn.a(String)) is the obfuscated method name of NBTTagCompound.getTag(String)
     * (gb)           is the obfuscated class  name of NBTTagShort
     * (gb.e())       is the obfuscated method name of NBTTagShort.getShort()
     */
    private void addMcEnchant(final EnchantmentStorageMeta meta, final fn enchNbt) {
        try {
            // Here we are not using (fn.e(String)) because it does not fail correctly:
            // it returns 0 instead of throwing an exception.
            // Instead, we use (gb.e()). This way either the cast will fail or a NPE will be thrown.
            final short enchId = ((gb) enchNbt.a("r1_8/et/minecraft/server/id")).e();
            final short enchLvl = ((gb) enchNbt.a("lvl")).e();
            @SuppressWarnings("deprecation")
            final Enchantment ench = Enchantment.getById(enchId);
            if (ench == null) {
                Log.warn("Unknown Enchantment ID (" + enchId + "), ignored.");
            } else {
                meta.addStoredEnchant(ench, enchLvl, false);
            }
        } catch (final RuntimeException e) {
            if (e instanceof ClassCastException || e instanceof NullPointerException) {
                Log.error("Failed to add Enchantment to Enchanted Book, ignored.\nThe NBT was: " + enchNbt, e);
            } else {
                throw e;
            }
        }
    }
}
