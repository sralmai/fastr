package r.nodes.truffle;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import r.*;
import r.nodes.*;

public class LazyBuild extends BaseR {

    public LazyBuild(ASTNode orig) {
        super(orig);
        assert Utils.check(orig != null);
    }

    @Override
    public final Object execute(RContext context, Frame frame) {
        try {
            throw new UnexpectedResultException(null);
        } catch (UnexpectedResultException e) {
            RNode node = context.createNode(getAST());
            replace(node, "expandLazyBuildNode");
            return node.execute(context, frame);
        }
    }

}
