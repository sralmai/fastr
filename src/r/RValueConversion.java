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
