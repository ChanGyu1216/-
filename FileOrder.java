import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.JTableHeader;

// 파일 검색을 담당하는 클래스
public class FileOrder implements MouseListener {
	
	private List<WrappedFile> childrenList;
	private int[] order = new int[4];	// header별 현 정렬 상태를 저장하는 array
	private FileSystemTableModel fst;	// tablemodel을 받는다.
	//0 > 오름차순, 1 > 내림차순
	
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
			return String.format("%,d", new Object[] { Long.valueOf((long) Math
					.ceil(fileWrapper.getSize() / 1024.0D)) })
					+ "KB";
		}
		return "";
	}


	public void sortFilesTable(int type, int lexicographicalOrder) {
		sortFileList(fst.getChildren(), type);
	}

	// header에 mouse click이 감지되었을 때 
	@Override
	public void mouseClicked(MouseEvent e) {
		// header의 index 정보를 가져와서 sortFilesTable method에 파라미터를 넘긴다.
		int index = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
		sortFilesTable(index, order[index]);
		
		// order[index]의 값을 변경한다.(내림차순, 오름차순을 위해)
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
	// 파일 정렬 method
	// comparator interface를 구현하여 비교 대상과 return 값을  설정한다.
	public void sortFileList(List<WrappedFile> list, int type){
		
		if(type == 0){	// 파일 명 정렬
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					if(order[type] == 0)
						return o1.getName().compareTo(o2.getName());
					
					return o2.getName().compareTo(o1.getName());
				}
			});
		}
		
		else if(type == 1){		// 파일 수정날짜 별 정렬
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					if(order[type] == 0)
						return o1.getDate().compareTo(o2.getDate());
					return o2.getDate().compareTo(o1.getDate());
				}
			});
			
		}
		
		else if(type == 2){		// 파일 타입 별 정렬
			
			Collections.sort(list, new Comparator<WrappedFile>() {

				@Override
				public int compare(WrappedFile o1, WrappedFile o2) {
					
					// 디렉터리 여부를 체크
					boolean d1 = o1.isDirectory();
					boolean d2 = o2.isDirectory();
					
					if(d1 && d2)
						return -1;
					else if(!d1 && d2)
						return 1;
					else	// 비교 대상 모두 디렉터리가 아닐 경우, 파일명으로 정렬한다.
						return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
				}
			});
		}
		
		else if(type == 3){	// 파일 크기 별 정렬
			
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
