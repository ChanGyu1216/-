import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;

import java.awt.BorderLayout;

public class Explorer extends JFrame implements TreeSelectionListener {
	private WrappedFile rootDir;
	private FileSystemTableModel tableModel;
	private FileSystemTreeModel treeModel;
	private JTree tree;
	private JTable table;
	private JTabbedPane tabbedPane;
	private JTextArea textArea;
	private JMenu historyMenu;//추가
	
	private TextBox tb;
	private JTextField search_bar;
	private static ArrayList<HistoryFile> history = new ArrayList<HistoryFile>();//추가

	public Explorer(WrappedFile rootDir) {
		super("Explorer");

		this.rootDir = rootDir;

		buildMenuBar();
		buildContentPane();

		setDefaultCloseOperation(3);
		setSize(1000, 800);
		setVisible(true);
	}

	private void buildContentPane() {
		this.treeModel = new FileSystemTreeModel(this.rootDir);
		this.tree = new JTree(this.treeModel);
		this.tree.addTreeSelectionListener(this);

		this.tableModel = new FileSystemTableModel(this.rootDir);
		
		this.table = new JTable(this.tableModel);
		this.table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = Explorer.this.table.getSelectedRow();
					String fileName = Explorer.this.table.getValueAt(row, 0).toString();
					String path = Explorer.this.tableModel.getCurrentDir().getPath();
					File file = new File(path, fileName);
					String text = Explorer.this.openFile(file);
//----------------------------------------------------------------------추가~
					boolean isSame = false;
					for (int i = 0; i < history.size(); ++i) {
						if (history.get(i).getFile().equals(file)) {
							isSame = true;
						}
					}
					if (!isSame) {
						Explorer.history.add(new HistoryFile(file, text));
						JMenuItem historyItem = new JMenuItem(path + fileName);
						historyMenu.add(historyItem);
						historyItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								Explorer.this.textArea.setText(text);
								Explorer.this.tabbedPane.setTitleAt(0, file.getAbsolutePath());
							}
						});
						historyMenu.updateUI();
					}
//----------------------------------------------------------------------~추가
					if (text != null) {
						Explorer.this.tabbedPane.setTitleAt(0, file.getAbsolutePath());
						Explorer.this.textArea.setText(text);
					}
				}
			}
		});
		JTableHeader header = this.table.getTableHeader();
		header.addMouseListener(new FileOrder(this.tableModel.getChildren(), this.tableModel));
		
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		search_bar = new JTextField();
		panel.add(search_bar);
		search_bar.setColumns(20);
		
		
		JSplitPane sp1 = new JSplitPane(1);
		sp1.setLeftComponent(new JScrollPane(this.tree));
		JSplitPane sp2 = new JSplitPane(0);
		sp1.setRightComponent(sp2);
		sp2.setTopComponent(new JScrollPane(this.table));
		this.tabbedPane = new JTabbedPane(1);
		this.textArea = new JTextArea();
		this.tabbedPane.addTab("", new JScrollPane(this.textArea));
		sp2.setBottomComponent(this.tabbedPane);

		tb = new TextBox(tableModel.getHeaders(), table, search_bar, sp2);
		
		getContentPane().add(sp1);
	}

	private void buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");

		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int state = chooser.showOpenDialog(null);
				if (state == 0) {
					File file = chooser.getSelectedFile();
					String text = Explorer.this.openFile(file);
					if (text != null) {
						Explorer.this.tabbedPane.setTitleAt(0, file.getAbsolutePath());
						Explorer.this.textArea.setText(text);
					}
				}
			}
		});
		file.add(open);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = Explorer.this.tabbedPane.getTitleAt(Explorer.this.tabbedPane
						.getSelectedIndex());
				Explorer.this.saveFile(fileName);
			}
		});
		file.add(save);

		historyMenu = new JMenu("History");//추가

		file.add(historyMenu);//추가

		file.addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exit);

		menuBar.add(file);

		setJMenuBar(menuBar);
	}

	private String openFile(File file) {
		StringBuffer text = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = br.readLine()) != null) {
				text.append(line + '\n');
			}

			br.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "텍스트 문서가 아닙니다.", "Message", 0);
			return null;

		}
		return text.toString();
	}

	private void saveFile(String fileName) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

			bw.write(this.textArea.getText());

			bw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "파일 저장 오류!", "Message", 0);
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		Object o = this.tree.getLastSelectedPathComponent();
		if ((o instanceof WrappedFile)) {
			WrappedFile currentDir = (WrappedFile) o;
			this.tableModel.setCurrentDir(currentDir);
			this.table.updateUI();
		}
	}

	public static void main(String[] args) {
		new Explorer(new WrappedFile("C:/"));
	}
}