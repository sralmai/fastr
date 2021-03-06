package r;

import com.oracle.truffle.api.nodes.*;

import r.data.*;
import r.data.internal.*;

public class RValueConversion {

    public static int expectScalarLogical(RAny value) throws UnexpectedResultException {
        if (value instanceof ScalarLogicalImpl) {
            return ((ScalarLogicalImpl) value).getLogical();
        }
        throw new UnexpectedResultException(value);
    }

    public static int expectScalarNonNALogical(RAny value) throws UnexpectedResultException {
        if (value instanceof ScalarLogicalImpl) {
            int res = ((ScalarLogicalImpl) value).getLogical();
            if (res != RLogical.NA) {
                return res;
            }
        }
        throw new UnexpectedResultException(value);
    }

    public static int expectScalarInteger(RAny value) throws UnexpectedResultException {
        if (value instanceof ScalarIntImpl) {
            return ((ScalarIntImpl) value).getInt();
        }
        throw new UnexpectedResultException(value);
    }

    public static RArray expectScalar(RAny value) throws UnexpectedResultException {
        if (value instanceof RArray) {
            RArray array = (RArray) value;
            if (array.size() == 1) {
                return array;
            }
        }
        throw new UnexpectedResultException(value);
    }
}
