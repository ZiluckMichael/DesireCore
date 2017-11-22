package com.desiremc.core.api.items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.util.com.google.common.io.BaseEncoding;

public class ItemStackTypeConverter extends TypeConverter
{

    public ItemStackTypeConverter()
    {
        this(ItemStack.class);
    }

    public ItemStackTypeConverter(Class<?>... clazz)
    {
        super(clazz);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo)
    {
        if (!(value instanceof ItemStack))
        {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        NBTTagCompound tag = getTag((ItemStack) value);
        NBTCompressedStreamTools.a(tag, output);

        return BaseEncoding.base64().encode(output.toByteArray());
    }

    @Override
    public Object decode(Class<?> targetClass, Object val, MappedField optionalExtraInfo)
    {
        ByteArrayInputStream input = new ByteArrayInputStream(BaseEncoding.base64().decode(val.toString()));

        NBTTagCompound tag = NBTCompressedStreamTools.a(input);
        net.minecraft.server.v1_7_R4.ItemStack nms = net.minecraft.server.v1_7_R4.ItemStack.createStack(tag);

        return CraftItemStack.asBukkitCopy(nms);
    }

    private NBTTagCompound getTag(ItemStack item)
    {
        if (item == null)
        {
            return null;
        }
        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.server.v1_7_R4.ItemStack stack = getMinecraftStack(item);
        stack.save(tag);
        return tag;
    }

    private net.minecraft.server.v1_7_R4.ItemStack getMinecraftStack(ItemStack stack)
    {
        return CraftItemStack.asNMSCopy(stack);
    }

}
