package com.inksetter.twist;

import java.util.*;

public class BaseScriptContext implements ScriptContext {

    private final Deque<Map<String, Object>> varStack = new LinkedList<>();

    public BaseScriptContext() {
        varStack.addFirst(new LinkedHashMap<>());
    }

    @Override
    public void popStack() {
        varStack.removeFirst();
    }

    @Override
    public void pushStack() {
        varStack.addFirst(new LinkedHashMap<>());
    }

    @Override
    public Object getVariable(String name) {
        for (Map<String, Object> symbols : varStack) {
            if (symbols.containsKey(name)) {
                return symbols.get(name);
            }
        }
        return null;
    }

    @Override
    public void setVariable(String name, Object value) {
        for (Map<String, Object> symbols : varStack) {
            if (symbols.containsKey(name)) {
                symbols.put(name, value);
                return;
            }
        }
        varStack.getFirst().put(name, value);
    }

    @Override
    public boolean lookupExternalFunction(String functionName) {
        return false;
    }

    @Override
    public Object invokeExternalFunction(String functionName, List<Object> argValues) {
        return null;
    }

    @Override
    public void debug(String format, Object... args) {
        // by default, do nothing
    }
}