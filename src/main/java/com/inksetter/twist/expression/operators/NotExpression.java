package com.inksetter.twist.expression.operators;

import com.inksetter.twist.TwistException;
import com.inksetter.twist.ValueUtils;
import com.inksetter.twist.EvalContext;
import com.inksetter.twist.Expression;

public class NotExpression implements Expression {
    public NotExpression(Expression target) {
        _target = target;
    }
    
    public Object evaluate(EvalContext ctx) throws TwistException {
        Object targetValue = _target.evaluate(ctx);

        return !ValueUtils.asBoolean(targetValue);
    }
    
    // @see java.lang.Object#toString()
    @Override
    public String toString() {
        return "!" + _target;
    }
    
    private final Expression _target;
}

