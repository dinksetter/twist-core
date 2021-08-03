package com.inksetter.twist.expression.operators;

import com.inksetter.twist.TwistException;
import com.inksetter.twist.ValueUtils;
import com.inksetter.twist.exec.ExecContext;
import com.inksetter.twist.expression.Expression;

public class AndExpression implements Expression {
    public AndExpression(Expression left, Expression right) {
        _left = left;
        _right = right;
    }
    
    public Object evaluate(ExecContext ctx) throws TwistException {
        Object leftValue = _left.evaluate(ctx);
        boolean result;
        
        if (ValueUtils.asBoolean(leftValue)) {
            Object rightValue = _right.evaluate(ctx);
            result = ValueUtils.asBoolean(rightValue);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return _left.toString() + " && " + _right.toString();
    }
    
    private final Expression _left;
    private final Expression _right;    
}
