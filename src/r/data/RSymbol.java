package r.data;

import java.util.*;

import r.*;
import r.Convert.*;
import r.data.internal.*;

public final class RSymbol extends BaseObject implements RAny {

    final String name;
    // The next two fields are for the topLevel
    RAny value;
    int version;

    private static final SymbolTable symbolTable = new SymbolTable(); // TODO put in Context ??!!
    public static final RSymbol NA_SYMBOL = RSymbol.getSymbol(RString.NA);
    public static final RSymbol EMPTY_SYMBOL = RSymbol.getSymbol("");

    private RSymbol(String identifier) {
        name = identifier;
    }

    public static RSymbol getSymbol(String name) {
        return symbolTable.get(name);
    }

    public static RSymbol[] getSymbols(String[] names) {
        RSymbol[] symbols = new RSymbol[names.length];
        for (int i = 0; i < names.length; i++) {
            symbols[i] = RSymbol.getSymbol(names[i]);
        }
        return symbols;
    }

    public static RSymbol[] getSymbols(RString names) {
        int size = names.size();
        RSymbol[] symbols = new RSymbol[size];
        for (int i = 0; i < size; i++) {
            symbols[i] = RSymbol.getSymbol(names.getString(i));
        }
        return symbols;
    }

    @Override
    public String pretty() {
        // TODO put `` when needed
        return name;
    }

    public int id() { // TODO add a field for global numbering and use it !
        return hashCode(); // id = currentId++;
    }

    public int hash() { // TODO add a field for filtering!
        return hashCode(); // hash = 1 << (currentHash = currentHash + 1 % Integer.size);
    }

    private static class SymbolTable {

        // TODO A less stupid implementation for symbol table
        // i.e., close to a set implementation with linear probing
        final Map<String, RSymbol> table = new HashMap<>();

        private RSymbol get(String name) {
            RSymbol sym = table.get(name);
            if (sym == null) {
                table.put(name, sym = new RSymbol(name));
            }
            return sym;
        }
    }

    @Override
    public RRaw asRaw() {
        Utils.nyi();
        return null;
    }

    @Override
    public RLogical asLogical() {
        Utils.nyi();
        return null;
    }

    @Override
    public RInt asInt() {
        Utils.nyi();
        return null;
    }

    @Override
    public RDouble asDouble() {
        Utils.nyi();
        return null;
    }

    @Override
    public RComplex asComplex() {
        Utils.nyi();
        return null;
    }

    @Override
    public RString asString() {
        Utils.nyi();
        return null;
    }

    @Override
    public RList asList() {
        Utils.nyi();
        return null;
    }

    public RAny getValue() {
        return value;
    }

    void setValue(RAny val) {
        value = val;
    }

    public int getVersion() {
        return version;
    }

    void markDirty() {
        version++;
    }

    @Override
    public boolean isShared() {
        return false;
    }

    @Override
    public void ref() {
    }

    @Override
    public RSymbol stripAttributes() {
        return this;
    }

    @Override
    public RRaw asRaw(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public RLogical asLogical(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public RInt asInt(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public RDouble asDouble(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public RComplex asComplex(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public RString asString(ConversionStatus warn) {
        Utils.nyi();
        return null;
    }

    @Override
    public String typeOf() {
        Utils.nyi();
        return null;
    }

}
