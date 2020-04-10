import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WrappedFile
{
  private File file;
  private List<WrappedFile> children;
  private List<WrappedFile> subdir;

  WrappedFile(String fileName)
  {
    this(new File(fileName));
  }

  private WrappedFile(File file) {
    this.file = file;
  }

  boolean isDirectory() {
    return this.file.isDirectory();
  }

  List<WrappedFile> getChildren() {
    if ((isDirectory()) && (this.children == null)) {
      this.children = new ArrayList();
      String[] list = this.file.list();
      if (list == null) {
        return this.children;
      }
      ArrayList onlyFiles = new ArrayList();

      for (int i = 0; i < list.length; i++) {
        File aChild = new File(this.file, list[i]);
        WrappedFile aChildWrapper = new WrappedFile(aChild);
        if (aChildWrapper.isDirectory())
          this.children.add(aChildWrapper);
        else {
          onlyFiles.add(aChildWrapper);
        }
      }
      this.children.addAll(onlyFiles);
    }
    return this.children;
  }

  List<WrappedFile> getSubdirectory() {
    if ((isDirectory()) && (this.subdir == null)) {
      this.subdir = new ArrayList();

      int c = getChildCount();
      for (int i = 0; i < c; i++) {
        WrappedFile child = (WrappedFile)this.children.get(i);
        if (child.isDirectory()) {
          this.subdir.add(child);
        }
      }
    }
    return this.subdir;
  }

  private int getChildCount() {
    if (this.children == null) {
      getChildren();
    }
    return this.children.size();
  }

  int getSubdirectoryCount() {
    if (this.subdir == null) {
      getSubdirectory();
    }
    return this.subdir.size();
  }

  String getName() {
    return this.file.getName();
  }

  long getSize() {
    return this.file.length();
  }

  String getDate() {
    Date d = new Date(this.file.lastModified());
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd a h:mm");
    return f.format(d);
  }

  String getPath() {
    return this.file.getAbsolutePath();
  }

  public String toString() {
    return getName();
  }

public File getFile() {
	return file;
}

public void setFile(File file) {
	this.file = file;
}
  
  
}