

//SP25-BCS-151
public class TreeNode {
    Item data;
    TreeNode leftChild, rightChild;
    int nodeHeight;

    public TreeNode(Item data) {
        this.data = data;
        this.nodeHeight = 1;
    }
}