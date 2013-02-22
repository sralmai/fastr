package r.builtins;

import java.util.*;

import r.*;
import r.data.*;

public class Primitives {

    public static final boolean STATIC_LOOKUP = false;

    private static Map<RSymbol, PrimitiveEntry> map;
    static {
        map = new HashMap<>();
        initializePrimitives();
    }

    public static void initializePrimitives() {
        map.clear();
        add(":", 2, 2, Colon.FACTORY);
        add("+", 1, 2, Operators.ADD_FACTORY);
        add("-", 1, 2, Operators.SUB_FACTORY);
        add("*", 2, 2, Operators.MULT_FACTORY);
        add("/", 2, 2, Operators.DIV_FACTORY);
        add("==", 2, 2, Operators.EQ_FACTORY);
        add("!=", 2, 2, Operators.NE_FACTORY);
        add(">", 2, 2, Operators.GT_FACTORY);
        add("<", 2, 2, Operators.LT_FACTORY);
        add("<=", 2, 2, Operators.LE_FACTORY);
        add(">=", 2, 2, Operators.GE_FACTORY);
        add("%%", 2, 2, Operators.MOD_FACTORY);
        add("%/%", 2, 2, Operators.INTEGER_DIV_FACTORY);
        add("^", 2, 2, Operators.POW_FACTORY);
        add("%*%", 2, 2, Operators.MAT_MULT_FACTORY);
        add("%o%", 2, 2, Operators.OUTER_MULT_FACTORY);
        add("|", 2, 2, Operators.ELEMENTWISE_OR_FACTORY);
        add("&", 2, 2, Operators.ELEMENTWISE_AND_FACTORY);
        add("||", 2, 2, Operators.OR_FACTORY);
        add("&&", 2, 2, Operators.AND_FACTORY);
        add("!", 1, 1, Operators.NOT_FACTORY);
        add("abs", 1, 1, Abs.FACTORY);
        add("aperm", 1, 3, Aperm.FACTORY);
        add("array", 0, 3, Array.FACTORY);
        add("assign", 2, 6, Environment.ASSIGN_FACTORY);
        add("as.character", 0, -1, Cast.STRING_FACTORY);
        add("as.complex", 0, -1, Cast.COMPLEX_FACTORY);
        add("as.double", 0, -1, Cast.DOUBLE_FACTORY);
        add("as.environment", 1, 1, Environment.ASENVIRONMENT_FACTORY);
        add("as.integer", 0, -1, Cast.INT_FACTORY);
        add("as.logical", 0, -1, Cast.LOGICAL_FACTORY);
        add("as.raw", 0, -1, Cast.RAW_FACTORY);
        add("as.vector", 1, 2, Cast.VECTOR_FACTORY);
        add("attr", 2, 3, Attributes.ATTR_FACTORY);
        add("attr<-", 3, 3, Attributes.ATTR_REPLACEMENT_FACTORY);
        add("attributes", 1, 1, Attributes.ATTRIBUTES_FACTORY);
        add("attributes<-", 2, 2, Attributes.ATTRIBUTES_REPLACEMENT_FACTORY);
        add("c", 0, -1, Combine.FACTORY);
        add("cat", 0, -1, Cat.FACTORY);
        add("character", 0, 1, ArrayConstructor.STRING_FACTORY);
        add("close", 1, 1, ConnectionOperation.CLOSE_FACTORY);
        add("colMeans", 1, 3, ColumnsRowsStats.COLMEANS_FACTORY);
        add("colSums", 1, 3, ColumnsRowsStats.COLSUMS_FACTORY);
        add("cumsum", 1, 1, CumulativeSum.FACTORY);
        add("diag<-", 2, 2, Diagonal.REPLACEMENT_FACTORY);
        add("dim", 1, 1, Dimensions.DIM_FACTORY);
        add("double", 0, 1, ArrayConstructor.DOUBLE_FACTORY);
        add("get", 1, 5, Environment.GET_FACTORY);
        add("gregexpr", 2, 6, RegExpr.GREGEXPR_FACTORY);
        add("gsub", 3, 7, Sub.GSUB_FACTORY);
        add("eigen", 1, 4, Eigen.EIGEN_FACTORY);
        add("emptyenv", 0, 0, Environment.EMPTYENV_FACTORY);
        add("exists", 1, 6, Environment.EXISTS_FACTORY);
        add("exp", 1, 1, Exp.FACTORY);
        add("file", 0, 5, OpenConnection.FILE_FACTORY);
        add("flush", 1, 1, ConnectionOperation.FLUSH_FACTORY);
        add("integer", 0, 1, ArrayConstructor.INT_FACTORY);
        add("is.character", 1, 1, TypeCheck.STRING_FACTORY);
        add("is.complex", 1, 1, TypeCheck.COMPLEX_FACTORY);
        add("is.double", 1, 1, TypeCheck.DOUBLE_FACTORY);
        add("is.integer", 1, 1, TypeCheck.INT_FACTORY);
        add("is.list", 1, 1, TypeCheck.LIST_FACTORY);
        add("is.logical", 1, 1, TypeCheck.LOGICAL_FACTORY);
        add("is.null", 1, 1, TypeCheck.IS_NULL_FACTORY);
        add("is.numeric", 1, 1, TypeCheck.NUMERIC_FACTORY);
        add("is.na", 1, 1, IsNA.FACTORY);
        add("is.null", 1, 1, TypeCheck.IS_NULL_FACTORY);
        add("is.raw", 1, 1, TypeCheck.RAW_FACTORY);
        add("lapply", 2, -1, Apply.LAPPLY_FACTORY);
        add("length", 1, 1, Length.FACTORY);
        add("length<-", 2, 2, Length.REPLACEMENT_FACTORY);
        add("list", 0, -1, List.FACTORY);
        add("log", 1, 2, MathFunctions.LOG_FACTORY);
        add("log10", 1, 1, MathFunctions.LOG10_FACTORY);
        add("log2", 1, 1, MathFunctions.LOG2_FACTORY);
        add("logical", 0, 1, ArrayConstructor.LOGICAL_FACTORY);
        add("lower.tri", 1, 2, TriangularPart.LOWER_FACTORY);
        add("ls", 0, 5, Environment.LS_FACTORY);
        add("matrix", 0, 5, Matrix.FACTORY);
        add("max", 0, -1, Extreme.MAX_FACTORY);
        add("min", 0, -1, Extreme.MIN_FACTORY);
        add("names", 1, 1, Names.FACTORY);
        add("names<-", 2, 2, Names.REPLACEMENT_FACTORY);
        add("nchar", 1, 3, NChar.FACTORY);
        add("ncol", 1, 1, Dimensions.NCOL_FACTORY);
        add("new.env", 0, 3, Environment.NEWENV_FACTORY);
        add("nrow", 1, 1, Dimensions.NROW_FACTORY);
        add("options", 0, -1, Options.FACTORY);
        add("order", 0, -1, Sort.ORDER_FACTORY);
        add("outer", 2, -1, Outer.FACTORY);
        add("paste", 0, -1, Paste.FACTORY);
        add("pipe", 1, 3, OpenConnection.PIPE_FACTORY);
        add("raw", 0, 1, ArrayConstructor.RAW_FACTORY);
        add("readLines", 0, 5, ReadLines.FACTORY);
        add("regexpr", 2, 6, RegExpr.REGEXPR_FACTORY);
        add("rep", 2, 2, Rep.REP_FACTORY);
        add("rep.int", 2, 2, Rep.REPINT_FACTORY);
        add("return", 0, 1, Return.FACTORY);
        add("rev", 1, 1, Rev.FACTORY);
        add("rev.default", 1, 1, Rev.FACTORY);
        add("rowMeans", 1, 3, ColumnsRowsStats.ROWMEANS_FACTORY);
        add("rowSums", 1, 3, ColumnsRowsStats.ROWSUMS_FACTORY);
        add("sapply", 2, -1, Apply.SAPPLY_FACTORY);
        add("scan", 0, 21, Scan.FACTORY);
        add("seq", 0, -1, Seq.FACTORY);  // in fact seq.default (and only part of it)
        add("seq.default", 0, -1, Seq.FACTORY);
        add("strsplit", 1, 5, StrSplit.FACTORY);
        add("sub", 3, 7, Sub.SUB_FACTORY);
        add("substr", 3, 3, Substring.SUBSTR_FACTORY);
        add("substring", 2, 3, Substring.SUBSTRING_FACTORY);
        add("sum", 0, -1, Sum.FACTORY);
        add("sqrt", 1, 1, Sqrt.FACTORY);
        add("t", 1, 1, Transpose.FACTORY);
        add("t.default", 1, 1, Transpose.FACTORY);
        add("tolower", 1, 1, CharUtils.TOLOWER_FACTORY);
        add("toupper", 1, 1, CharUtils.TOUPPER_FACTORY);
        add("typeof", 1, 1, TypeOf.TYPEOF_FACTORY);
        add("unlist", 1, 3, Unlist.FACTORY);
        add("upper.tri", 1, 2, TriangularPart.UPPER_FACTORY);
        add("which", 1, 3, Which.FACTORY);
        add("writeBin", 2, 5, WriteBin.FACTORY);
        add("commandArgs", 0, 1, CommandArgs.FACTORY);
    }

    public static boolean hasCallFactory(final RSymbol name, final RFunction enclosing) {
        return Primitives.get(name, enclosing) != null;
    }

    public static CallFactory getCallFactory(RSymbol name, RFunction enclosing) {
        final PrimitiveEntry pe = Primitives.get(name, enclosing);
        if (pe == null) {
            return null;
        } else {
            return pe.factory;
        }
    }

    public static RBuiltIn getBuiltIn(RSymbol name, RFunction enclosing) {
        final PrimitiveEntry pe = Primitives.get(name, enclosing);
        if (pe == null) {
            return null;
        } else {
            return pe.builtIn;
        }
    }

    public static PrimitiveEntry get(RSymbol name, RFunction fun) {
        PrimitiveEntry pe = get(name);
        if (pe != null && fun != null && fun.isInWriteSet(name)) {   // TODO: fix these checks
            Utils.debug("IGNORING over-shadowing of built-in " + name.pretty() + "!!!");
            Utils.nyi(); // TODO the case when a primitive is shadowed by a local symbol
            // FIXME: but shouldn't we keep traversing recursively through all frames of the caller?
            // FIXME: also, what about reflections?
        }

        return pe;
    }

    public static PrimitiveEntry get(RSymbol name) {
        return map.get(name);
    }

    private static void add(String name, int minArgs, int maxArgs, CallFactory body) {
        add(name, minArgs, maxArgs, body, PrimitiveEntry.PREFIX);
    }

    private static void add(String name, int minArgs, int maxArgs, CallFactory body, int prettyPrint) {
        RSymbol sym = RSymbol.getSymbol(name);
//        assert Utils.check(!map.containsKey(sym)); no longer holds (or should hold)
        map.put(sym, new PrimitiveEntry(sym, minArgs, maxArgs, body, prettyPrint));
    }
}
