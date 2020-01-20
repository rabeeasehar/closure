       // Look for a function type in the argument list.
        Iterator<Node> paramIter =
            ((FunctionType) recvType).getParameters().iterator();
        Iterator<Node> argumentIter = n.children().iterator();
        argumentIter.next(); // Skip the function name.
        while (paramIter.hasNext() && argumentIter.hasNext()) {
          Node arg = argumentIter.next();
          Node param = paramIter.next();
          if (arg.getJSType() instanceof FunctionType) {
            actions.addAll(getImplicitActionsFromArgument(
                arg,
                ((FunctionType) arg.getJSType()).getTypeOfThis(),
                param.getJSType()));