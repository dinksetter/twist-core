package com.inksetter.twist.expression;

import com.inksetter.twist.EvalContext;
import com.inksetter.twist.Expression;

public class IntegerLiteral implements Expression {
    public IntegerLiteral(Integer value) {
        _value = value;
    }

    public Integer evaluate(EvalContext ctx) {
        return _value;
    }

    @Override
    public String toString() { return _value.toString(); }

    private final Integer _value;
}
