import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class FileSystemTableModel
  implements TableModel
{
  private String[] headers = { "이름", "수정한 날짜", "유형", "크기" };
  private WrappedFile currentDir;
  private List<WrappedFile> children;
  //private FileOrder fo;
  

  FileSystemTableModel(WrappedFile currentDir)
  {
    setCurrentDir(currentDir);
  }

  void setCurrentDir(WrappedFile currentDir) {
    this.currentDir = currentDir;
    this.children = this.currentDir.getChildren();
  }

  WrappedFile getCurrentDir() {
    return this.currentDir;
  }

  public void addTableModelListener(TableModelListener l)
  {
  }

  public Class<?> getColumnClass(int columnIndex) {
    return Object.class;
  }

  public int getColumnCount()
  {
    return this.headers.length;
  }

  public String getColumnName(int column)
  {
    return this.headers[column];
  }

  public int getRowCount()
  {
    return this.children.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    WrappedFile fileWrapper = (WrappedFile)this.children.get(rowIndex);

    if (columnIndex == 0)
      return fileWrapper.getName();
    if (columnIndex == 1)
      return fileWrapper.getDate();
    if (columnIndex == 2) {
      if (fileWrapper.isDirectory()) {
        return "파일 폴더";
      }
      String fileName = fileWrapper.getName();
      int dotIndex = fileName.lastIndexOf('.');
      if (dotIndex > -1) {
        String extension = fileName.substring(dotIndex + 1);
        return extension.toUpperCase() + " 파일";
      }

      return "파일";
    }

    if (!fileWrapper.isDirectory()) {
      return String.format("%,d", new Object[] { Long.valueOf((long)Math.ceil(fileWrapper.getSize() / 1024.0D)) }) + "KB";
    }
    return "";
  }

  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return false;
  }

  public void removeTableModelListener(TableModelListener l)
  {
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
  }
  

	public List<WrappedFile> getChildren() {
	return children;
}

	public void setChildren(List<WrappedFile> children) {
		this.children = children;
	}

	public String[] getHeaders() {
		return headers;
	}
	
	public List<WrappedFile> getOlderChildren(){
		return children;
	}
  
  
}