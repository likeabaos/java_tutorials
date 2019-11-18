package likeabaos.tools.util.scripts.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class SimpleDataTable {
    private boolean zebraStripe = false;
    private boolean headerBold = true;

    public boolean IsZebraStrip() {
	return zebraStripe;
    }

    public void setZebraStripe(boolean value) {
	this.zebraStripe = value;
    }

    public boolean isHeaderBold() {
	return headerBold;
    }

    public void setHeaderBold(boolean headerBold) {
	this.headerBold = headerBold;
    }

    public void show(String title, Object[][] data, String[] header) {
	final boolean useZebraStripe = this.IsZebraStrip();
	JTable jtable = new JTable(data, header) {
	    private static final long serialVersionUID = 1L;
	    private final Color alternate = new Color(Color.lightGray.getRed(), Color.lightGray.getBlue(),
		    Color.lightGray.getGreen(), 125);

	    @Override
	    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component comp = super.prepareRenderer(renderer, row, column);
		if (useZebraStripe)
		    comp.setBackground((row % 2 == 1) ? alternate : Color.WHITE);
		return comp;
	    }
	};
	if (this.isHeaderBold()) {
	    Font font = jtable.getTableHeader().getFont();
	    jtable.getTableHeader().setFont(font.deriveFont(Font.BOLD));
	}

	jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	resizeColumnWidth(jtable);
	JScrollPane jspane = new JScrollPane(jtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JFrame jframe = new JFrame();
	jframe.add(jspane);

	jframe.setTitle(title);
	jframe.pack();
	jframe.setLocationRelativeTo(null);
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
    }

    public static void resizeColumnWidth(JTable table) {
	final TableColumnModel columnModel = table.getColumnModel();
	for (int column = 0; column < table.getColumnCount(); column++) {
	    int width = 15; // Min width
	    for (int row = 0; row < table.getRowCount(); row++) {
		TableCellRenderer renderer = table.getCellRenderer(row, column);
		Component comp = table.prepareRenderer(renderer, row, column);
		width = Math.max(comp.getPreferredSize().width + 10, width);
		width = Math.min(1000, width);
	    }
	    columnModel.getColumn(column).setPreferredWidth(width);
	}
    }

}
