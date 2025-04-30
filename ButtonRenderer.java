import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setText("Delete");
        setBackground(new Color(255, 80, 80));  // Customize button color
        setForeground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this; // Just return this button
    }
}
