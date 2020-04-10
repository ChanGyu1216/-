import java.io.File;


public class HistoryFile {
	private File file;
	private String text;
	
	public HistoryFile() {
		
	}
	public HistoryFile(File file, String text) {
		super();
		this.file = file;
		this.text = text;
	}
	public File getFile() {
		return file;
	}
	public String getText() {
		return text;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "HistoryFile [file=" + file + ", text=" + text + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryFile other = (HistoryFile) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	
}
