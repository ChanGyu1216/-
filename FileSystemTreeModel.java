import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

class FileSystemTreeModel
  implements TreeModel
{
  private WrappedFile root;
  private List<TreeModelListener> listeners;

  FileSystemTreeModel(WrappedFile root)
  {
    this.root = root;
    this.listeners = new ArrayList();
  }

  public void addTreeModelListener(TreeModelListener l)
  {
    if ((l != null) && (!this.listeners.contains(l)))
      this.listeners.add(l);
  }

  public Object getChild(Object parent, int index)
  {
    Object child = null;

    if ((parent instanceof WrappedFile)) {
      List children = ((WrappedFile)parent).getSubdirectory();

      if ((children != null) && 
        (index < children.size()))
        child = children.get(index);
    }
    return child;
  }

  public int getChildCount(Object parent)
  {
    int count = 0;

    if ((parent instanceof WrappedFile)) {
      count = ((WrappedFile)parent).getSubdirectoryCount();
    }
    return count;
  }

  public int getIndexOfChild(Object parent, Object child)
  {
    int index = -1;

    if ((parent instanceof WrappedFile)) {
      List children = ((WrappedFile)parent).getSubdirectory();

      if (children != null) {
        index = children.indexOf(child);
      }
    }
    return index;
  }

  public Object getRoot()
  {
    return this.root;
  }

  public boolean isLeaf(Object node)
  {
    return !((WrappedFile)node).isDirectory();
  }

  public void removeTreeModelListener(TreeModelListener l)
  {
    this.listeners.remove(l);
  }

  public void valueForPathChanged(TreePath path, Object newValue)
  {
  }
}