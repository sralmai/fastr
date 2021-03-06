package r.builtins;

import com.oracle.truffle.api.frame.*;

import r.*;
import r.data.*;
import r.errors.*;
import r.nodes.*;
import r.nodes.truffle.*;

// TODO: add S4
public class All extends CallFactory {

    static final CallFactory _ = new All("all", new String[]{"...", "na.rm"}, new String[]{});

    private All(String name, String[] params, String[] required) {
        super(name, params, required);
    }

    static boolean parseNarm(RAny arg) {

        RLogical l = arg.asLogical();
        if (l.size() >= 1) {
            int v = l.getLogical(0);
            if (v == RLogical.FALSE)  {
                return false;
            }
        }
        return true;
    }

    // FIXME: this could be optimized for speed if needed (avoid coercion for some types, assert that naRM is last when checking, etc)
    @Override
    public RNode create(ASTNode call, RSymbol[] names, RNode[] exprs) {
        ArgumentInfo ia = check(call, names, exprs);
        final int posNarm = ia.position("na.rm");

        return new Builtin(call, names, exprs) {

            @Override
            public RAny doBuiltIn(Frame frame, RAny[] args) {

                boolean naRM = posNarm == -1 ? false : parseNarm(args[posNarm]);
                boolean didWarn = false;
                boolean hasNA = false;
                for (int i = 0; i < args.length; i++) {
                    if (i == posNarm) {
                        continue;
                    }
                    RAny v = args[i];
                    RLogical l;
                    if (v instanceof RLogical) {
                        l = (RLogical) v;
                    } else if (v instanceof RInt) {
                        l = v.asLogical();
                    } else {
                        l = v.asLogical();
                        if (!didWarn) {
                            RContext.warning(ast, RError.COERCING_ARGUMENT, v.typeOf(), "logical");
                            didWarn = true;
                        }
                    }
                    int size = l.size();
                    for (int j = 0; j < size; j++) {
                        int ll = l.getLogical(j);
                        switch(ll) {
                            case RLogical.TRUE: break;
                            case RLogical.FALSE: return RLogical.BOXED_FALSE;
                            case RLogical.NA: hasNA = true; break;
                        }
                    }
                }

                if (!naRM && hasNA) {
                    return RLogical.BOXED_NA;
                }
                return RLogical.BOXED_TRUE;
            }
        };
    }
}
