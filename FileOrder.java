import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.JTableHeader;

// ���� �˻��� ����ϴ� Ŭ����
public class FileOrder implements MouseListener {
	
	private List<WrappedFile> childrenList;
	private int[] order = new int[4];	// header�� �� ���� ���¸� �����ϴ� array
	private FileSystemTableModel fst;	// tablemodel�� �޴´�.
	//0 > ��������, 1 > ��������
	
	public FileOrder(List<WrappedFile> childrenList, FileSystemTableModel fst) {
		super();
		this.childrenList = childrenList;
		this.fst = fst;
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		WrappedFile fileWrapper = (WrappedFile) this.childrenList.get(rowIndex);

		if (columnIndex == 0)
			return fileWrapper.getName();
		if (columnIndex == 1)
			return fileWrapper.getDate();
		if (columnIndex == 2) {
			if (fileWrapper.isDirectory()) {
				return "���� ����";
			}
			String fileName = fileWrapper.getName();
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex > -1) {
				String extension = fileName.substring(dotIndex + 1);
				return extension.toUpperCase() + " ����";
			}

			return "����";
		}

		if (!fileWrapper.isDirectory()) {
			return String.format("%,d", new Object[] { Long.valueOf((long) Math
					.ceil(fileWrapper.getSize() / 1024.0D)) })
					+ "KB";
		}
		return "";
	}


	public void sortFilesTable(int type, int lexicographicalOrder) {
		sortFileList(fst.getChildren(), type);
	}

	// header�� mouse click�� �����Ǿ��� �� 
	@Override
	public void mouseClicked(MouseEvent e) {
		// header�� index ������ �����ͼ� sortFilesTable method�� �Ķ���͸� �ѱ��.
		int index = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
		sortFilesTable(index, order[index]);
		
		// order[index]�� ���� �����Ѵ�.(��������, ���������� ����)
		order[index] = order[index] == 1 ? 0 : 1;
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	// ���� ���� method
	// comparator interface�� �����Ͽ� �� ���� return ����  �����Ѵ�.
	public void sortFileList(List<WrappedFile> list, int type){
		
		if(type == 0){	// ���� �� ����
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					if(order[type] == 0)
						return o1.getName().compareTo(o2.getName());
					
					return o2.getName().compareTo(o1.getName());
				}
			});
		}
		
		else if(type == 1){		// ���� ������¥ �� ����
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					if(order[type] == 0)
						return o1.getDate().compareTo(o2.getDate());
					return o2.getDate().compareTo(o1.getDate());
				}
			});
			
		}
		
		else if(type == 2){		// ���� Ÿ�� �� ����
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					
					// ���͸� ���θ� üũ
					boolean d1 = o1.isDirectory();
					boolean d2 = o2.isDirectory();
					
					if(d1 && d2)
						return -1;
					else if(!d1 && d2)
						return 1;
					else	// �� ��� ��� ���͸��� �ƴ� ���, ���ϸ����� �����Ѵ�.
						return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
				}
			});
		}
		
		else if(type == 3){	// ���� ũ�� �� ����
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					if(order[type] == 0)
						return o1.getFile().length() > o2.getFile().length() ? -1 : 1;
					return o1.getFile().length() < o2.getFile().length() ? -1 : 1;
				}
			});
			
		}
		
	}

}
