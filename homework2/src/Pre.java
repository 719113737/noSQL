import java.util.ArrayList;

public class Pre {
    public ArrayList<Integer>preorderTraversal(TreeNode root) {
        ArrayList<Integer>result = new ArrayList<Integer>();
        if(root == null) {
            return result;
        }

        ArrayList<Integer>left = preorderTraversal(root.left);
        ArrayList<Integer>right = preorderTraversal(root.right);

        result.add(root.val);
        result.addAll(left);
        result.addAll(right);

        return result;
    }
/* test function
    public static void main(String args[]) {
        TreeNode root = new TreeNode();
        root.val = 0;

        TreeNode node1 = new TreeNode();
        node1.val = 1;

        TreeNode node2 = new TreeNode();
        node2.val = 2;

        TreeNode node3 = new TreeNode();
        node3.val = 3;

        root.left = node1;
        root.right = node2;
        node1.left = node3;

        Pre a = new Pre();

        System.out.println(a.preorderTraversal(root));
    }

 */
}
