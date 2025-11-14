package TheLoai;

import Sach.BookManagerPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Lớp panel quản lý thông tin Danh mục sách
public class CategoryManagerPanel extends JPanel {

    // Khai báo các thành phần giao diện
    private JTable table; // Bảng hiển thị danh sách danh mục
    private DefaultTableModel tableModel; // Model cho bảng
    // Các trường nhập liệu và tìm kiếm
    private JTextField tfId, tfName, tfSearch;
    private JTextArea taDescription; // Ô nhập liệu cho Mô tả
    private JScrollPane spDescription; // Thanh cuộn cho JTextArea

    // Khai báo DAO để tương tác với cơ sở dữ liệu danh mục
    private CategoryDAO categoryDAO = new CategoryDAO();

    // Constructor của lớp CategoryManagerPanel
    public CategoryManagerPanel() {
        // Thiết lập layout cho panel chính là BorderLayout
        setLayout(new BorderLayout(10, 10));
        // Thiết lập màu nền cho panel
        setBackground(new Color(245, 250, 255));

        // ====== PANEL TÌM KIẾM Ở TRÊN ======
        // Tạo panel chứa ô tìm kiếm và nút tìm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(220, 235, 245));
        // Ô nhập liệu tìm kiếm
        tfSearch = new JTextField(20);
        tfSearch.setBorder(new RoundedBorder(8)); // Sử dụng RoundedBorder
        // Nút tìm kiếm
        JButton btnSearch = new JButton("Tìm");
        // Thiết lập style cho nút tìm kiếm
        btnSearch.setBackground(new Color(100, 180, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));
        // Thêm nhãn, ô tìm kiếm và nút tìm vào searchPanel
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);

        // ====== PANEL NHẬP LIỆU Ở TRÊN ======
        // Tạo panel chứa các ô nhập liệu và nhãn
        JPanel inputPanel = new JPanel(new GridBagLayout()); // Sử dụng GridBagLayout để sắp xếp linh hoạt
        // Thiết lập border cho inputPanel
        inputPanel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new TitledBorder("Thông tin Danh mục")
        ));
        inputPanel.setBackground(new Color(235, 245, 255));
        // Khởi tạo GridBagConstraints để cấu hình vị trí và kích thước các thành phần trong GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cho phép giãn theo chiều ngang
        // gbc.weightx = 1.0; // Give extra horizontal space to components that can expand (adjusted)

        // Khởi tạo các trường nhập liệu và JTextArea
        tfId = new JTextField(10);
        tfName = new JTextField(15);
        taDescription = new JTextArea(3, 20); // 3 hàng, 20 cột
        taDescription.setLineWrap(true); // Tự động xuống dòng
        taDescription.setWrapStyleWord(true); // Xuống dòng theo từ
        // Tạo thanh cuộn cho JTextArea
        spDescription = new JScrollPane(taDescription);
        // Áp dụng style bo góc cho JTextArea (hoặc JScrollPane chứa nó)
        spDescription.setBorder(new RoundedBorder(8)); // Áp dụng border cho ScrollPane
        taDescription.setBackground(new Color(255, 255, 255));

        // Áp dụng style bo góc cho các ô nhập liệu
        JTextField[] textFields = {tfId, tfName};
        for (JTextField f : textFields) {
            f.setBorder(new RoundedBorder(8));
            f.setBackground(new Color(255, 255, 255));
        }

        // Thêm các components vào panel nhập liệu với GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Mã Danh mục:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Field
        inputPanel.add(tfId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Tên Danh mục:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Field
        inputPanel.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST; gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0; // Field
        inputPanel.add(spDescription, gbc); // Thêm JScrollPane chứa JTextArea
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0; // Reset constraints

        // ====== GỘP PANEL TÌM KIẾM VÀ NHẬP LIỆU Ở TRÊN ======
        // Tạo panel chứa cả searchPanel và inputPanel và đặt ở phía trên cùng của panel chính
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ====== BẢNG HIỂN THỊ DỮ LIỆU ======
        // Khởi tạo tableModel với các cột và không cho phép sửa trực tiếp
        tableModel = new DefaultTableModel(new String[]{
                "Mã Danh mục", "Tên Danh mục", "Mô tả"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Không cho phép sửa trực tiếp trên bảng
                return false;
            }
        };
        // Khởi tạo bảng với tableModel
        table = new JTable(tableModel);
        // Thiết lập chiều cao hàng, font chữ, màu chọn cho bảng
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(200, 230, 255));

        // Điều chỉnh độ rộng các cột (nếu cần)
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã Danh mục
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên Danh mục
        table.getColumnModel().getColumn(2).setPreferredWidth(300); // Mô tả
        
        // Thêm thanh cuộn cho bảng
        JScrollPane scrollPane = new JScrollPane(table);
        // Đặt bảng vào giữa panel chính
        add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL NÚT CHỨC NĂNG Ở DƯỚI ======
        // Tạo panel chứa các nút chức năng CRUD
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 235, 245));
        // Khởi tạo các nút
        JButton btnLoad = new JButton("Tải lại");
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        // Thiết lập style chung cho các nút
        JButton[] btns = {btnLoad, btnAdd, btnUpdate, btnDelete};
        for (JButton b : btns) {
            b.setBackground(new Color(100, 180, 255));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial", Font.BOLD, 13));
        }

        // Thêm các nút vào buttonPanel
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        // Đặt buttonPanel ở phía dưới cùng của panel chính
        add(buttonPanel, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
        // Thêm các ActionListener cho các nút để xử lý sự kiện click
        btnLoad.addActionListener(e -> loadCategories());
        btnAdd.addActionListener(e -> addCategory());
        btnUpdate.addActionListener(e -> updateCategory());
        btnDelete.addActionListener(e -> deleteCategory());
        btnSearch.addActionListener(e -> searchCategory());
        tfSearch.addActionListener(e -> searchCategory()); // Thêm listener cho Enter trong ô tìm kiếm

        // Thêm MouseListener cho bảng để xử lý sự kiện click chuột vào hàng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfId.setText(tableModel.getValueAt(row, 0).toString());
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                taDescription.setText(tableModel.getValueAt(row, 2).toString());
                tfId.setEnabled(false);
            }
        });

        // Tải dữ liệu danh mục lần đầu khi panel được hiển thị
        loadCategories();
        clearInputFields(); // Xóa trắng các ô nhập lúc đầu
    }

    // Phương thức để tải danh sách danh mục từ database và hiển thị lên bảng
    private void loadCategories() {
        // Lấy danh sách danh mục từ DAO
        List<Category> categories = categoryDAO.getAllCategories();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách danh mục và thêm vào bảng
        for (Category c : categories) {
            String name = c.getName() != null ? c.getName() : "(Không xác định)";
            String description = c.getDescription() != null ? c.getDescription() : "(Không xác định)";
            tableModel.addRow(new Object[]{
                    c.getId(), name, description
            });
        }
        clearInputFields(); // Xóa trắng ô nhập sau khi tải lại
    }

    // Phương thức xử lý thêm danh mục mới
    private void addCategory() {
        try {
             // Kiểm tra các trường bắt buộc
            if (tfName.getText().trim().isEmpty() || taDescription.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Tên Danh mục, Mô tả).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // Tạo đối tượng Category từ dữ liệu nhập liệu (luôn dùng ID 0 cho thêm mới)
            Category c = new Category(0,
                    tfName.getText().trim(),
                    taDescription.getText().trim()
            );
            // Gọi phương thức insert của DAO
            if (categoryDAO.insertCategory(c)) {
                // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
                loadCategories();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyCategoryDataChanged();
            } else {
                // Hiển thị thông báo thất bại
                JOptionPane.showMessageDialog(this, "Thêm danh mục thất bại.");
            }
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm danh mục: " + ex.getMessage());
        }
    }

    // Phương thức xử lý cập nhật thông tin danh mục
    private void updateCategory() {
        try {
             // Kiểm tra xem đã chọn danh mục từ bảng chưa
             if (tfId.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần cập nhật từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                 return;
             }
              // Kiểm tra các trường bắt buộc
            if (tfName.getText().trim().isEmpty() || taDescription.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Tên Danh mục, Mô tả).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // Tạo đối tượng Category từ dữ liệu nhập liệu (bao gồm ID)
            Category c = new Category(
                    Integer.parseInt(tfId.getText()), // Lấy ID từ ô nhập liệu
                    tfName.getText().trim(),
                    taDescription.getText().trim()
            );
            // Gọi phương thức update của DAO
            if (categoryDAO.updateCategory(c)) {
                // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadCategories();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyCategoryDataChanged();
            } else {
                // Hiển thị thông báo thất bại
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Mã danh mục không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật danh mục: " + ex.getMessage());
        }
    }

    // Phương thức xử lý xóa danh mục
    private void deleteCategory() {
        try {
             // Kiểm tra xem đã chọn danh mục từ bảng chưa
            if (tfId.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần xóa từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                 return;
             }
            // Lấy ID danh mục cần xóa
            int id = Integer.parseInt(tfId.getText());
            // Hiển thị hộp thoại xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn xoá danh mục này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            // Nếu người dùng xác nhận xóa
            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi phương thức delete của DAO
                if (categoryDAO.deleteCategory(id)) {
                    // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                    JOptionPane.showMessageDialog(this, "Xoá thành công!");
                    loadCategories();
                    clearInputFields();
                    // Thông báo cho các panel khác cập nhật dữ liệu
                    notifyCategoryDataChanged();
                } else {
                    // Hiển thị thông báo thất bại
                    JOptionPane.showMessageDialog(this, "Xoá thất bại.");
                }
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Mã danh mục không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa danh mục: " + ex.getMessage());
        }
    }

    // Phương thức xử lý tìm kiếm danh mục
    private void searchCategory() {
        // Lấy từ khóa tìm kiếm và chuyển về chữ thường
        String keyword = tfSearch.getText().trim().toLowerCase();
         // Nếu từ khóa rỗng, tải lại toàn bộ danh sách
        if (keyword.isEmpty()) {
            loadCategories(); // Tải lại toàn bộ danh sách nếu ô tìm kiếm trống
            return;
        }
        // Lấy toàn bộ danh sách danh mục
        List<Category> categories = categoryDAO.getAllCategories();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách danh mục để tìm kiếm
        for (Category c : categories) {
            // Kiểm tra nếu từ khóa khớp với bất kỳ trường nào (ID, tên, mô tả)
            if (String.valueOf(c.getId()).contains(keyword) ||
                c.getName().toLowerCase().contains(keyword) ||
                c.getDescription().toLowerCase().contains(keyword)) {
                // Thêm hàng dữ liệu nếu khớp
                String name = c.getName() != null ? c.getName() : "(Không xác định)";
                String description = c.getDescription() != null ? c.getDescription() : "(Không xác định)";
                tableModel.addRow(new Object[]{
                        c.getId(), name, description
                });
            }
        }
    }

    // ====== CLASS BO GÓC CHO Ô NHẬP ======
    // Lớp nội bộ để tạo hiệu ứng bo tròn cho viền các ô nhập liệu
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            // Vẽ hình chữ nhật bo góc
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        @Override
        public Insets getBorderInsets(Component c) {
            // Điều chỉnh khoảng cách nội dung bên trong border
            return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1);
        }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            // Điều chỉnh khoảng cách nội dung bên trong border (override)
            insets.left = insets.right = insets.top = insets.bottom = this.radius+1;
            return insets;
        }
    }
    
    // Phương thức tiện ích để xóa trắng các ô nhập liệu và bật lại ô ID
    private void clearInputFields() {
        tfId.setText("");
        tfName.setText("");
        taDescription.setText("");
        tfId.setEnabled(true);
    }

    // Phương thức thông báo cho các panel khác cập nhật dữ liệu thể loại
    private void notifyCategoryDataChanged() {
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