package fr.ribesg.minecraft.pure.vanilla.r1_8;

import r1_8.net.minecraft.server.bqc;
import r1_8.net.minecraft.server.brn;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyMapStorage extends brn /* MapStorage */ {

    public NmsDummyMapStorage() {
        super(null);
    }

    @Override
    public void a(final String arg0, final bqc arg1) {
        // NOP
    }

    @Override
    public int a(final String arg0) {
        return 0; // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public bqc a(final Class arg0, final String arg1) {
        return null; // NOP
    }
}
