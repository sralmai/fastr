package r.data.internal;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import r.*;
import r.builtins.Return.*;
import r.data.*;
import r.nodes.Function;
import r.nodes.truffle.*;

// FIXME: with the new Truffle API, some of our older structures are no longer needed (e.g. read set, write set), could remove them
// FIXME: in theory, a read set could be larger, simply a union of all write sets (slots) of enclosing functions
// FIXME: "read set" and "write set" may not be the same names ; it is more "cached parent slots" and "slots"
public class FunctionImpl extends RootNode implements RFunction {

    final RFunction enclosing;
    final Function source;

    final RSymbol[] paramNames;
    @Children final RNode[] paramValues;
    final RNode body;

    final FrameDescriptor frameDescriptor;
    final FrameSlot[] paramSlots;
    final CallTarget callTarget;

    final RSymbol[] writeSet;
    final int writeSetBloom;
    final ReadSetEntry[] readSet;
    final int readSetBloom;

    private static final boolean DEBUG_CALLS = false;

    public FunctionImpl(Function source, RSymbol[] paramNames, RNode[] paramValues, RNode body, RFunction enclosing, RSymbol[] writeSet, ReadSetEntry[] readSet) {
        this.source = source;
        this.paramNames = paramNames;
        this.paramValues = paramValues;
        this.body = body;
        this.enclosing = enclosing;
        this.writeSet = writeSet;
        this.readSet = readSet;

        // calculate bloom masks
        int wsBloom = 0;
        int rsBloom = 0;
        for (RSymbol sym : writeSet) {
            wsBloom |= sym.hash();
        }
        for (ReadSetEntry rse : readSet) {
            rsBloom |= rse.symbol.hash();
        }
        this.readSetBloom = rsBloom;
        this.writeSetBloom = wsBloom;

        if (DEBUG_CALLS) {
            Utils.debug("creating function with");
            Utils.debug("  write set [" + writeSet.length + "]: " + printWriteSet(writeSet));
            Utils.debug("  read set  [" + readSet.length + "]: " + printReadSet(readSet));
        }

        // FIXME: this could be turned into nodes and node rewriting, each argument copied by a special node (the Truffle way to do it)
        int nparams = paramNames.length;
        paramSlots = new FrameSlot[nparams];
        frameDescriptor = new FrameDescriptor();
        for (int i = 0; i < nparams; i++) {
            paramSlots[i] = frameDescriptor.addFrameSlot(writeSet[i]);
        }
        for (int i = nparams; i < writeSet.length; i++) {
            frameDescriptor.addFrameSlot(writeSet[i]);
        }

        callTarget = Truffle.getRuntime().createCallTarget(this, frameDescriptor);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        RFrameHeader h = RFrameHeader.header(frame);
        Object[] args = h.arguments();
        for (int i = 0; i < paramSlots.length; i++) {
            RAny value = (RAny) args[i];  // FIXME: use RAny array instead?
            if (value != null) {
                frame.setObject(paramSlots[i], value);
                value.ref();
            } else {
                RNode n = paramValues[i];
                if (n != null) {
                    value = (RAny) n.execute(RContext.instance, frame); // TODO: get rid of the context
                    if (value != null) {
                        frame.setObject(paramSlots[i], value);
                        value.ref();
                    }
                    // NOTE: value can be null when a parameter is missing
                    // NOTE: if such a parameter is not used by the function, R is happy
                }
            }
        }

        Object res;
        try {
            res = body.execute(RContext.instance, frame);
        } catch (ReturnException re) {
            res = h.returnValue();
        }
        return res;
    }

    private static String printWriteSet(RSymbol[] writeSet) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        int i = 0;
        for (RSymbol s : writeSet) {
            if (first) {
                first = false;
            } else {
                str.append(", ");
            }
            str.append(s.pretty());
            str.append(":");
            str.append(i);
            i++;
        }
        return str.toString();
    }

    private static String printReadSet(ReadSetEntry[] readSet) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (ReadSetEntry e : readSet) {
            if (first) {
                first = false;
            } else {
                str.append(", ");
            }
            str.append(e.symbol.pretty());
            str.append(":");
            str.append("(");
            str.append(e.hops);
            str.append(",");
            str.append(e.slot);
            str.append(")");
        }
        return str.toString();
    }

    public int nlocals() {
        return writeSet.length;
    }

    public int nparams() {
        return paramNames.length;
    }

    public int positionInLocalWriteSet(RSymbol sym) {
        if (isIn(sym.hash(), writeSetBloom)) {
            RSymbol[] ws = writeSet;
            int len = ws.length;
            for (int i = 0; i < len; i++) {
                if (ws[i] == sym) {
                    return i;
                }
            }
        }
        return -1;
    }

    // a version without allocation
    public int positionInLocalReadSet(RSymbol sym) {
        if (isIn(sym.hash(), readSetBloom)) {
            ReadSetEntry[] rs = readSet;
            int len = rs.length;
            for (int i = 0; i < len; i++) {
                if (rs[i].symbol == sym) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ReadSetEntry getLocalReadSetEntry(RSymbol sym) {
        int i = positionInLocalReadSet(sym);
        return (i == -1) ? null : readSet[i];
    }

    public static boolean isIn(int id, int bloomfilter) { // TODO maybe move to Utils ?
        return (id & bloomfilter) == id;
    }

    @Override
    public RSymbol[] paramNames() {
        return paramNames;
    }

    @Override
    public RNode[] paramValues() {
        return paramValues;
    }

    @Override
    public RNode body() {
        return body;
    }

    @Override
    public RFunction enclosing() {
        return enclosing;
    }

    @Override
    public Function getSource() {
        return source;
    }

    @Override
    public CallTarget callTarget() {
        return callTarget;
    }

    @Override
    public RClosure createClosure(MaterializedFrame enclosingEnvironment) {
        return new ClosureImpl(this, enclosingEnvironment);
    }

    @Override
    public boolean isInWriteSet(RSymbol sym) {
        if (positionInLocalWriteSet(sym) != -1) {
            return true;
        }
        if (enclosing == null) {
            return false;
        }
        return enclosing.isInWriteSet(sym);
    }

    @Override
    public RSymbol[] localWriteSet() {
        return writeSet;
    }

    @Override
    public FrameSlot slotInWriteSet(RSymbol symbol) {
        return frameDescriptor.findFrameSlot(symbol);
    }
}
