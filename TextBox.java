import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TextBox extends KeyAdapter {

	private Object data[][];
	private int count[];
	private String[] columNames;
	
	private JTable table;
	private JTextField search_bar;
	private JScrollPane tableScroll;
	private JPanel panel;
	private JSplitPane pan2;

	
	public TextBox(String[] columNames, JTable table, JTextField search_bar, JSplitPane pan2) {
		super();
		this.columNames = columNames;
		this.table = table;
		this.search_bar = search_bar;
		this.search_bar.addKeyListener(this);
		this.pan2 = pan2;
		
	}

	public void keyPressed(KeyEvent e) {
		data = new Object[0][4];
		DefaultTableModel model = new DefaultTableModel(data, columNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// Enter키를 감지한다.
		if (e.getKeyCode() == 10) {
			
			if (table != null) {
				// 넘겨받은 table을 반복문으로 순회하며 textfield의 내용과 같은 값이 있으면 table model을 재생성한다.
				for (int i = 0; i < table.getRowCount(); ++i) {	
					String name = (String) table.getValueAt(i, 0);
					if (name.equals(search_bar.getText())) {

						Object[] str = new Object[4];
						for (int k = 0; k < 4; ++k) {
							str[k] = table.getValueAt(i, k);
						}
						model.addRow(str);
						break;
					}
				
				}
				// table model을 다시 table로 감싸 Panel에 붙인다.
				table = new JTable(model);
				tableScroll = new JScrollPane(table);
				panel = new JPanel();
				panel.setLayout(new BorderLayout());
				panel.add(tableScroll);
				pan2.setTopComponent(panel);
				pan2.setDividerLocation(300);
				
				System.out.println(pan2);
			}

			else {
				JOptionPane.showMessageDialog(null, "열린 파일이 없습니다.", "Warning",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


}
