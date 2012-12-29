package net.minecraft.src;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class InvTweaksConfigProperties extends Properties
{
    private static final long serialVersionUID = 1L;
    private final List keys = new LinkedList();

    public Enumeration keys()
    {
        return Collections.enumeration(new LinkedHashSet(this.keys));
    }

    public Object put(String var1, Object var2)
    {
        this.keys.add(var1);
        return super.put(var1, var2);
    }

    public void sortKeys()
    {
        Collections.sort(this.keys);
    }
}
