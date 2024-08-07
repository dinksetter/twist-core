package com.inksetter.twist.exec;

import com.inksetter.twist.TwistException;
import com.inksetter.twist.ValueUtils;
import com.inksetter.twist.Expression;

import java.io.Serializable;
import java.util.List;

public class Statement implements Serializable {

    private Expression ifTest;
    private Statement ifStatement;
    private Statement elseStatement;
    private Expression expression;

    private List<CatchBlock> catchBlocks;
    private StatementBlock finallyBlock;
    private StatementBlock subSequence;

    public void setIfTest(Expression ifTest) {
        this.ifTest = ifTest;
    }

    public void setIfStatement(Statement ifStatement) {
        this.ifStatement = ifStatement;
    }

    public void setElseStatement(Statement elseStatement) {
        this.elseStatement = elseStatement;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() { return expression; }

    public void setCatchBlocks(List<CatchBlock> catchBlocks) {
        this.catchBlocks = catchBlocks;
    }

    public void setFinallyBlock(StatementBlock finallyBlock) {
        this.finallyBlock = finallyBlock;
    }

    public Object execute(ScriptContext exec) throws TwistException {
        return executeStatement(exec);
    }

    // @see java.lang.Object#toString()
    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (ifTest != null) {
            tmp.append("if (");
            tmp.append(ifTest);
            tmp.append(") ");
            tmp.append(ifStatement);
            if (elseStatement != null) {
                tmp.append(" else ");
                tmp.append(elseStatement);
            }
        }

        else {
            if ((catchBlocks != null && catchBlocks.size() != 0)
                    || finallyBlock != null) {
                tmp.append("try {");
                tmp.append(subSequence);
                tmp.append('}');
                if (catchBlocks != null) {
                    for (CatchBlock cblock : catchBlocks) {
                        tmp.append(cblock);
                    }
                }
                if (finallyBlock != null) {
                    tmp.append("finally");
                    tmp.append(finallyBlock);
                }
            }
            else {
                if (subSequence != null) {
                    tmp.append(subSequence);
                }
                else {
                    tmp.append(expression).append(";");
                }
            }
        }

        return tmp.toString();
    }


    private Object executeStatement(ScriptContext exec) throws TwistException {
        if (ifTest != null) {
            Object testValue = ifTest.evaluate(exec);
            if (ValueUtils.asBoolean(testValue)) {
                ifStatement.execute(exec);
            }
            else if (elseStatement != null) {
                elseStatement.execute(exec);
            }

            // If statements do not have value, just side effects.
            return null;
        }

        if (subSequence != null) {
            try {
                return subSequence.execute(exec, true);
            } catch (Exception e) {
                if (catchBlocks != null) {
                    // If we're set up to catch errors, do so.
                    String exceptionClassName = e.getClass().getTypeName();
                    for (CatchBlock catchBlock : catchBlocks) {

                        boolean matches = exceptionClassName.equals(catchBlock.getTypeName());

                        if (matches) {
                            // We execute the catch block, if it exists. If it's a
                            // simple catch expression, then
                            // we return the error results of the exception that got
                            // thrown.
                            StatementBlock block = catchBlock.getBlock();

                            // If there's a block of code to execute on this catch
                            // expression, return the result of executing that
                            // block.
                            if (block != null) {
                                block.execute(exec, true);
                                break;
                            }
                        }
                    }
                }

                // If we got through the entire catch expression set, throw the
                // original exception out.
                throw e;
            } finally {
                if (finallyBlock != null) {
                    finallyBlock.execute(exec, true);
                }
            }
        }
        else {
            if (expression != null) {
                Object value = expression.evaluate(exec);
                return value;
            }
        }
        return null;
    }

    public void setSubSequence(StatementBlock subSequence) {
        this.subSequence = subSequence;
    }
}
