interface Expression {
    Expression run();
    Expression feed(Expression var, Expression value);
}
