import java.util.Random;

class Node {
  int _value;
  Node _left;
  Node _right;

  Node(int value) {
    _value = value;
    _left = _right = null;
  }
}

class binary_tree {
  Node root;
  binary_tree() {
    root = null;
  }
  void insert(int value) {
    root = insert_rec(root, value);
  }
  Node insert_rec(Node root, int value) {
    if (root == null) {
      root = new Node(value);
      return root;
    }
    if (value < root._value)
      root._left = insert_rec(root._left, value);
    else if (value > root._value) 
      root._right = insert_rec(root._right, value);
    return root;
  }
  void in_order() {
    in_order_rec(root);
  }
  void in_order_rec(Node root) {
    if (root != null) {
      in_order_rec(root._left);
      System.out.println(root._value+" ");
      in_order_rec(root._right);
    }
  }
  public static void main(String[] args) {
    Random rand = new Random();
    int rng = rand.nextInt(999)+1;
    binary_tree tree = new binary_tree();
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    tree.insert(rng);
    System.out.println("""
      Inorder traversal: 
    """);
      tree.in_order();
  }
}
