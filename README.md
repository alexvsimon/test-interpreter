# test-interpreter
simple interprter for simple gramma:

* `expr ::= expr op expr | (expr) | identifier | { expr, expr } | number | map(expr, identifier -> expr) | reduce(expr, expr, identifier identifier -> expr)`
* `op ::= + | - | * | / | ^`
* `stmt ::= var identifier = expr | out expr | print "string"`
* `program ::= stmt | program stmt`
