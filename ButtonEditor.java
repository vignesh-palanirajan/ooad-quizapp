import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonEditor extends DefaultCellEditor {

    private String label;
    private AdminDashboard adminDashboard;

    public ButtonEditor(JCheckBox checkBox, AdminDashboard adminDashboard) {
        super(checkBox);
        this.adminDashboard = adminDashboard;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "Delete" : value.toString();
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            // Get the user ID from the selected row
            int userId = (int) table.getValueAt(row, 0); // Assuming ID is in the first column
            adminDashboard.deleteUser(userId); // Call delete method
        });
        return button;
    }
}
