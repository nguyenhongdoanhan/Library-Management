package TacGia;

import Sach.BookManagerPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AuthorManagerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfAuthorId, tfAuthorName, tfBio, tfSearch;
    private AuthorDAO authorDAO = new AuthorDAO();

    public AuthorManagerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 255));

        // ====== PANEL TÌM KIẾM ======
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(220, 235, 245));
        tfSearch = new JTextField(20);
        tfSearch.setBorder(new RoundedBorder(8));
        JButton btnSearch = new JButton("Tìm");
        btnSearch.setBackground(new Color(100, 180, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);

        // ====== PANEL NHẬP LIỆU ======
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new TitledBorder("Thông tin Tác giả")
        ));
        inputPanel.setBackground(new Color(235, 245, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các trường nhập liệu
        tfAuthorId = new JTextField(10);
        tfAuthorName = new JTextField(20);
        tfBio = new JTextField(30);

        // Style cho các trường nhập liệu
        JTextField[] textFields = {tfAuthorId, tfAuthorName, tfBio};
        for (JTextField f : textFields) {
            f.setBorder(new RoundedBorder(8));
            f.setBackground(new Color(255, 255, 255));
        }

        // Thêm các trường vào panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Mã Tác giả:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(tfAuthorId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Tên Tác giả:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(tfAuthorName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Tiểu sử:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        gbc.gridwidth = 3; // Tiểu sử có thể rộng hơn
        inputPanel.add(tfBio, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // ====== GỘP PANEL TÌM KIẾM VÀ NHẬP LIỆU Ở TRÊN ======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ====== BẢNG HIỂN THỊ DỮ LIỆU ======
        tableModel = new DefaultTableModel(new String[]{
                "Mã Tác giả", "Tên Tác giả", "Tiểu sử"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Không cho phép sửa trực tiếp trên bảng
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(200, 230, 255));
        
        // Điều chỉnh độ rộng các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã Tác giả
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên Tác giả
        table.getColumnModel().getColumn(2).setPreferredWidth(350); // Tiểu sử
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL NÚT CHỨC NĂNG ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 235, 245));
        JButton btnLoad = new JButton("Tải lại");
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        JButton[] btns = {btnLoad, btnAdd, btnUpdate, btnDelete};
        for (JButton b : btns) {
            b.setBackground(new Color(100, 180, 255));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial", Font.BOLD, 13));
        }

        buttonPanel.add(btnLoad);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
        btnLoad.addActionListener(e -> loadAuthors());
        btnSearch.addActionListener(e -> searchAuthor());
        tfSearch.addActionListener(e -> searchAuthor());

        btnAdd.addActionListener(e -> addAuthor());
        btnUpdate.addActionListener(e -> updateAuthor());
        btnDelete.addActionListener(e -> deleteAuthor());

        // Sự kiện click vào bảng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfAuthorId.setText(tableModel.getValueAt(row, 0).toString());
                tfAuthorName.setText(tableModel.getValueAt(row, 1).toString());
                tfBio.setText(tableModel.getValueAt(row, 2).toString());
                tfAuthorId.setEnabled(false);
            }
        });

        // Load dữ liệu ban đầu
        loadAuthors();
        clearInputFields();
    }

    private void loadAuthors() {
        List<Author> authors = authorDAO.getAllAuthors();
        tableModel.setRowCount(0);
        for (Author a : authors) {
            String name = a.getName() != null ? a.getName() : "(Không xác định)";
            String bio = a.getBio() != null ? a.getBio() : "(Không xác định)";
            tableModel.addRow(new Object[]{
                a.getId(), name, bio
            });
        }
    }

    private void addAuthor() {
        try {
            String name = tfAuthorName.getText().trim();
            String bio = tfBio.getText().trim();

            if (name.isEmpty() || bio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            // Tạo đối tượng Author từ dữ liệu nhập liệu (luôn dùng ID 0 cho thêm mới)
            Author newAuthor = new Author(0, name, bio);
            if (authorDAO.insertAuthor(newAuthor)) {
                JOptionPane.showMessageDialog(this, "Thêm tác giả thành công!");
                loadAuthors();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyAuthorDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm tác giả thất bại.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm tác giả: " + ex.getMessage());
        }
    }

    private void updateAuthor() {
        try {
            if (tfAuthorId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tác giả cần cập nhật từ bảng.");
                return;
            }

            int id = Integer.parseInt(tfAuthorId.getText());
            String name = tfAuthorName.getText().trim();
            String bio = tfBio.getText().trim();

            if (name.isEmpty() || bio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            Author updatedAuthor = new Author(id, name, bio);
            if (authorDAO.updateAuthor(updatedAuthor)) {
                JOptionPane.showMessageDialog(this, "Cập nhật tác giả thành công!");
                loadAuthors();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyAuthorDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật tác giả thất bại.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật tác giả: " + ex.getMessage());
        }
    }

    private void deleteAuthor() {
        try {
            if (tfAuthorId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tác giả cần xóa từ bảng.");
                return;
            }

            int id = Integer.parseInt(tfAuthorId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn chắc chắn muốn xóa tác giả này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (authorDAO.deleteAuthor(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa tác giả thành công!");
                    loadAuthors();
                    clearInputFields();
                    // Thông báo cho các panel khác cập nhật dữ liệu
                    notifyAuthorDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa tác giả thất bại.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa tác giả: " + ex.getMessage());
        }
    }

    private void searchAuthor() {
        String keyword = tfSearch.getText().trim().toLowerCase();
        List<Author> allAuthors = authorDAO.getAllAuthors();
        tableModel.setRowCount(0);

        for (Author a : allAuthors) {
            if (String.valueOf(a.getId()).contains(keyword) ||
                a.getName().toLowerCase().contains(keyword) ||
                a.getBio().toLowerCase().contains(keyword)) {

                tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getName(),
                    a.getBio()
                });
            }
        }
    }

    private void clearInputFields() {
        tfAuthorId.setText("");
        tfAuthorName.setText("");
        tfBio.setText("");
        tfAuthorId.setEnabled(true);
    }

    // Class bo góc cho ô nhập
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1);
        }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius+1;
            return insets;
        }
    }

    // Phương thức thông báo cho các panel khác cập nhật dữ liệu tác giả
    private void notifyAuthorDataChanged() {
        // Tìm tất cả các panel trong ứng dụng
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            // Tìm tất cả các panel trong frame
            Component[] components = ((JFrame) parent).getContentPane().getComponents();
            for (Component comp : components) {
                if (comp instanceof BookManagerPanel) {
                    // Gọi phương thức cập nhật dữ liệu của BookManagerPanel
                    ((BookManagerPanel) comp).loadComboBoxData();
                }
            }
        }
    }
} 