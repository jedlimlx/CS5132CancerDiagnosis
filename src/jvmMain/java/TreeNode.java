public class TreeNode<T> extends Node<T> {
    private Integer feature;
    public void setFeature(Integer feature)
    {
        this.feature = feature;
    }
    public Integer getFeature()
    {
        return this.feature;
    }

    private Double threshhold;
    public void setThreshhold(Double threshhold)
    {
        this.threshhold = threshhold;
    }
    public Double getThreshhold()
    {
        return this.threshhold;
    }

    public void setLeft(TreeNode<T> left)
    {
        this.neighbours[0] = left;
    }
    public TreeNode<T> getLeft()
    {
        return (TreeNode<T>) this.neighbours[0];
    }

    public void setRight(TreeNode<T> right)
    {
        this.neighbours[1] = right;
    }
    public TreeNode<T> getRight()
    {
        return (TreeNode<T>) this.neighbours[1];
    }

    public TreeNode(Integer feature, Double threshhold, TreeNode<T> left, TreeNode<T> right, T item)
    {
        super(item, 2);
        setFeature(feature);

        setThreshhold(threshhold);

        setLeft(left);
        setRight(right);
    }

    public TreeNode(Integer feature, Double threshold, TreeNode<T> left, TreeNode<T> right)
    {
        this(feature, threshold, left, right, null);
    }

    public TreeNode(T item)
    {
        this(null, null, null, null, item);
    }

    public boolean is_leaf()
    {
        return getItem() != null;
    }

    public String toString()
    {
        String line1 = "Item is: " + getItem();
        String line2 = "Feature is: " + getFeature();
        String line3 = "Threshhold is: " + getThreshhold();

        String line4LeftHalf, line4RightHalf;
        if(getLeft() != null)
            line4LeftHalf = "Left is not null";
        else
            line4LeftHalf = "Left is null";

        if(getRight() != null)
            line4RightHalf = "Right is not null";
        else
            line4RightHalf = "Right is null";

        String line4 = line4LeftHalf + ", " + line4RightHalf;
        return line1 + "\n" + line2 + "\n" + line3 + "\n" + line4;
    }
}
