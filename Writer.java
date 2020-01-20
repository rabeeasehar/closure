  private void processCall(Node node, JsonML currentParent) {
    Iterator<Node> it = node.children().iterator();
    Node child = it.next();
    JsonML element;
    // the first child may indicate that it is invoke expression
    // or a standard function call
    switch (child.getType()) {
      case Token.GETPROP:         // a.x()
      case Token.GETELEM:         // a[x]()
        // we have to process this node here and cannot call processNode(child)
        // other children of CALL represent arguments, so we need to have
        // access to them while processing InvokeExpr
        element = new JsonML(TagType.InvokeExpr);
        element.setAttribute(
            TagAttr.OP,
            child.getType() == Token.GETPROP ? "." : "[]");
        currentParent.appendChild(element);

        // there should be exactly two children
        Node grandchild = child.getFirstChild();
        processNode(grandchild, element);
        processNode(grandchild.getNext(), element);


        break;
      case Token.NAME:
        // caja treats calls to eval in a special way
        if (child.getString().equals("eval")) {
          element = new JsonML(TagType.EvalExpr);
        } else {
          // element representing function name is created
          element = new JsonML(TagType.IdExpr);
          element.setAttribute(TagAttr.NAME, child.getString());
          // element representing function is created
          element = new JsonML(TagType.CallExpr, element);
        }
        currentParent.appendChild(element);
        break;
      default:
       // it addresses all cases where the first argument evaluates to
       // another expression
       element = new JsonML(TagType.CallExpr);
       currentParent.appendChild(element);
       processNode(child, element);
       break;
    }

    // there may be arguments applied
    while (it.hasNext()) {
      processNode(it.next(), element);
    }
  }