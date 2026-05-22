
//SP25-BCS-151
public class OrderSystem {
    private TreeNode rootNode;
    private boolean usePriority;

    public OrderSystem(boolean usePriority) {
        this.usePriority = usePriority;
    }


    private int getHeight(TreeNode n) {
        return (n == null) ? 0 : n.nodeHeight;
    }


    private int getDiff(TreeNode n) {
        return (n == null) ? 0 : getHeight(n.leftChild) - getHeight(n.rightChild);
    }


    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.leftChild;
        TreeNode temp = x.rightChild;

        x.rightChild = y;
        y.leftChild = temp;

        y.nodeHeight = Math.max(getHeight(y.leftChild), getHeight(y.rightChild)) + 1;
        x.nodeHeight = Math.max(getHeight(x.leftChild), getHeight(x.rightChild)) + 1;

        return x;
    }

    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.rightChild;
        TreeNode temp = y.leftChild;

        y.leftChild = x;
        x.rightChild = temp;

        x.nodeHeight = Math.max(getHeight(x.leftChild), getHeight(x.rightChild)) + 1;
        y.nodeHeight = Math.max(getHeight(y.leftChild), getHeight(y.rightChild)) + 1;

        return y;
    }


    private int compare(Item a, Item b) {
        if (usePriority) {
            if (a.level != b.level)
                return Integer.compare(a.level, b.level);

            return Long.compare(a.time, b.time);
        } else {
            return Long.compare(a.time, b.time);
        }
    }


    public void add(Item item) {
        rootNode = addNode(rootNode, item);
    }

    private TreeNode addNode(TreeNode node, Item item) {
        if (node == null) return new TreeNode(item);

        int c = compare(item, node.data);

        if (c < 0)
            node.leftChild = addNode(node.leftChild, item);
        else if (c > 0)
            node.rightChild = addNode(node.rightChild, item);
        else
            return node;

        node.nodeHeight = 1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild));

        int bal = getDiff(node);

        if (bal > 1 && compare(item, node.leftChild.data) < 0)
            return rotateRight(node);

        if (bal < -1 && compare(item, node.rightChild.data) > 0)
            return rotateLeft(node);

        if (bal > 1 && compare(item, node.leftChild.data) > 0) {
            node.leftChild = rotateLeft(node.leftChild);
            return rotateRight(node);
        }

        if (bal < -1 && compare(item, node.rightChild.data) < 0) {
            node.rightChild = rotateRight(node.rightChild);
            return rotateLeft(node);
        }

        return node;
    }


    public Item search(String id) {
        TreeNode res = searchNode(rootNode, id);
        return (res == null) ? null : res.data;
    }

    private TreeNode searchNode(TreeNode node, String id) {
        if (node == null) return null;

        if (node.data.id.equals(id))
            return node;

        TreeNode left = searchNode(node.leftChild, id);
        if (left != null) return left;

        return searchNode(node.rightChild, id);
    }


    public void remove(String id) {
        rootNode = removeNode(rootNode, id);
    }

    private TreeNode removeNode(TreeNode root, String id) {
        if (root == null) return null;

        TreeNode target = searchNode(root, id);
        if (target == null) return root;

        int c = compare(target.data, root.data);

        if (c < 0)
            root.leftChild = removeNode(root.leftChild, id);
        else if (c > 0)
            root.rightChild = removeNode(root.rightChild, id);
        else {
            if (root.leftChild == null || root.rightChild == null) {
                root = (root.leftChild != null) ? root.leftChild : root.rightChild;
            } else {
                TreeNode min = getMin(root.rightChild);
                root.data = min.data;
                root.rightChild = removeNode(root.rightChild, min.data.id);
            }
        }

        if (root == null) return null;

        root.nodeHeight = 1 + Math.max(getHeight(root.leftChild), getHeight(root.rightChild));

        int bal = getDiff(root);

        if (bal > 1 && getDiff(root.leftChild) >= 0)
            return rotateRight(root);

        if (bal > 1 && getDiff(root.leftChild) < 0) {
            root.leftChild = rotateLeft(root.leftChild);
            return rotateRight(root);
        }

        if (bal < -1 && getDiff(root.rightChild) <= 0)
            return rotateLeft(root);

        if (bal < -1 && getDiff(root.rightChild) > 0) {
            root.rightChild = rotateRight(root.rightChild);
            return rotateLeft(root);
        }

        return root;
    }


    private TreeNode getMin(TreeNode n) {
        while (n.leftChild != null)
            n = n.leftChild;
        return n;
    }

   // ye next item ha
    public Item next() {
        if (rootNode == null) return null;

        TreeNode cur = rootNode;

        if (usePriority) {
            while (cur.rightChild != null)
                cur = cur.rightChild;
        } else {
            while (cur.leftChild != null)
                cur = cur.leftChild;
        }

        return cur.data;
    }


    public void display() {
        inorder(rootNode);
    }

    private void inorder(TreeNode node) {
        if (node == null) return;

        inorder(node.leftChild);

        System.out.println(node.data.id + " , " + node.data.level + " , " + node.data.time);

        inorder(node.rightChild);
    }
}