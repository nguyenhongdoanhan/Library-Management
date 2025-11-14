package DocGia;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import MuonTra.BorrowingManagerPanel;

// Lớp panel quản lý thông tin Độc giả
public class ReaderManagerPanel extends JPanel {

    // Khai báo các thành phần giao diện
    private JTable table; // Bảng hiển thị danh sách độc giả
    private DefaultTableModel tableModel; // Model cho bảng
    private JTextField tfId, tfName, tfAddress, tfPhone, tfEmail, tfSearch; // Các trường nhập liệu và tìm kiếm

    // Khai báo DAO để tương tác với cơ sở dữ liệu độc giả
    private ReaderDAO readerDAO = new ReaderDAO();

    // Constructor của lớp ReaderManagerPanel
    public ReaderManagerPanel() {
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
        tfSearch.setBorder(new RoundedBorder(8));
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
        JPanel inputPanel = new JPanel(new GridBagLayout());
        // Thiết lập border cho inputPanel
        inputPanel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new TitledBorder("Thông tin độc giả")
        ));
        inputPanel.setBackground(new Color(235, 245, 255));
        // Khởi tạo GridBagConstraints để cấu hình vị trí và kích thước các thành phần trong GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cho phép giãn theo chiều ngang
        // gbc.weightx = 1.0; // Remove or reduce weightx for fields

        // Khởi tạo các trường nhập liệu
        tfId = new JTextField(10);
        tfName = new JTextField(15);
        tfAddress = new JTextField(20);
        tfPhone = new JTextField(10);
        tfEmail = new JTextField(15);

        // Bo góc cho các ô nhập liệu
        JTextField[] fields = {tfId, tfName, tfAddress, tfPhone, tfEmail};
        for (JTextField f : fields) {
            f.setBorder(new RoundedBorder(8));
            f.setBackground(new Color(255, 255, 255));
        }

        // Thêm các components vào panel với GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label doesn't need weight
        inputPanel.add(new JLabel("Mã độc giả:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Give some weight, but not full
        inputPanel.add(tfId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; // Label doesn't need weight
        inputPanel.add(new JLabel("Tên độc giả:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Give some weight
        inputPanel.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.7; // Adjusted weightx
        inputPanel.add(tfAddress, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; gbc.gridwidth = 1; // Reset gridwidth, Label doesn't need weight
        inputPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Give some weight
        inputPanel.add(tfPhone, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; // Label doesn't need weight
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Give some weight
        inputPanel.add(tfEmail, gbc);

        // ====== GỘP PANEL TÌM KIẾM VÀ NHẬP LIỆU Ở TRÊN ======
        // Tạo panel chứa cả searchPanel và inputPanel và đặt ở phía trên cùng của panel chính
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ====== BẢNG HIỂN THỊ DỮ LIỆU ======
        // Khởi tạo tableModel với các cột và không cho phép sửa trực tiếp
        tableModel = new DefaultTableModel(new String[]{
                "Mã Độc giả", "Họ tên", "Địa chỉ", "SĐT", "Email"
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
        btnLoad.addActionListener(e -> loadReaders());
        btnAdd.addActionListener(e -> addReader());
        btnUpdate.addActionListener(e -> updateReader());
        btnDelete.addActionListener(e -> deleteReader());
        btnSearch.addActionListener(e -> searchReader());
        tfSearch.addActionListener(e -> searchReader()); // Thêm listener cho Enter trong ô tìm kiếm

        // Thêm MouseListener cho bảng để xử lý sự kiện click chuột vào hàng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfId.setText(tableModel.getValueAt(row, 0).toString());
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfAddress.setText(tableModel.getValueAt(row, 2).toString());
                tfPhone.setText(tableModel.getValueAt(row, 3).toString());
                tfEmail.setText(tableModel.getValueAt(row, 4).toString());
                tfId.setEnabled(false);
            }
        });

        // Tải dữ liệu độc giả lần đầu khi panel được hiển thị
        loadReaders();
        clearInputFields(); // Xóa trắng các ô nhập ban đầu
    }

    // ====== HÀM TẢI DANH SÁCH ĐỘC GIẢ ======
    /**
     * Tải danh sách độc giả từ database và hiển thị lên bảng
     * Reset các trường nhập liệu sau khi tải
     */
    private void loadReaders() {
        // Lấy danh sách độc giả từ DAO
        List<Reader> readers = readerDAO.getAllReaders();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách độc giả và thêm vào bảng
        for (Reader r : readers) {
            String name = r.getName() != null ? r.getName() : "(Không xác định)";
            String address = r.getAddress() != null ? r.getAddress() : "(Không xác định)";
            String phone = r.getPhone() != null ? r.getPhone() : "(Không xác định)";
            String email = r.getEmail() != null ? r.getEmail() : "(Không xác định)";
            tableModel.addRow(new Object[]{
                    r.getId(), name, address, phone, email
            });
        }
        clearInputFields(); // Xóa trắng ô nhập sau khi tải lại
    }

    // ====== HÀM THÊM ĐỘC GIẢ ======
    /**
     * Thêm độc giả mới vào database
     * Kiểm tra dữ liệu đầu vào trước khi thêm
     */
    private void addReader() {
        try {
            // Kiểm tra các trường bắt buộc
            if (tfName.getText().trim().isEmpty() || tfAddress.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Họ tên, Địa chỉ, SĐT, Email).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // Tạo đối tượng Reader từ dữ liệu nhập liệu (luôn dùng ID 0 cho thêm mới)
            Reader r = new Reader(0,
                    tfName.getText().trim(),
                    // Lưu ý: Chưa xử lý trường Ngày sinh
                    tfAddress.getText().trim(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim()
            );
            // Gọi phương thức insert của DAO
            if (readerDAO.insertReader(r)) {
                // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!");
                loadReaders();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyReaderDataChanged();
            } else {
                // Hiển thị thông báo thất bại
                JOptionPane.showMessageDialog(this, "Thêm độc giả thất bại.");
            }
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm độc giả: " + ex.getMessage());
        }
    }

    // ====== HÀM SỬA ĐỘC GIẢ ======
    /**
     * Cập nhật thông tin độc giả đã chọn
     * Kiểm tra dữ liệu đầu vào trước khi cập nhật
     */
    private void updateReader() {
        try {
             // Kiểm tra xem đã chọn độc giả từ bảng chưa
             if (tfId.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả cần cập nhật từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                 return;
             }
            // Kiểm tra các trường bắt buộc
             if (tfName.getText().trim().isEmpty() || tfAddress.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Họ tên, Địa chỉ, SĐT, Email).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // Tạo đối tượng Reader từ dữ liệu nhập liệu (bao gồm ID)
            Reader r = new Reader(
                    Integer.parseInt(tfId.getText()), // Lấy ID từ ô nhập liệu
                    tfName.getText().trim(),
                    // Lưu ý: Chưa xử lý trường Ngày sinh
                    tfAddress.getText().trim(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim()
            );
            // Gọi phương thức update của DAO
            if (readerDAO.updateReader(r)) {
                // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadReaders();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyReaderDataChanged();
            } else {
                // Hiển thị thông báo thất bại
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Mã độc giả không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật độc giả: " + ex.getMessage());
        }
    }

    // ====== HÀM XÓA ĐỘC GIẢ ======
    /**
     * Xóa độc giả đã chọn khỏi database
     * Hiển thị xác nhận trước khi xóa
     */
    private void deleteReader() {
        try {
             // Kiểm tra xem đã chọn độc giả từ bảng chưa
            if (tfId.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả cần xóa từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                 return;
             }
            // Lấy ID độc giả cần xóa
            int id = Integer.parseInt(tfId.getText());
            // Hiển thị hộp thoại xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn xoá độc giả này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            // Nếu người dùng xác nhận xóa
            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi phương thức delete của DAO
                if (readerDAO.deleteReader(id)) {
                    // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                    JOptionPane.showMessageDialog(this, "Xoá thành công!");
                    loadReaders();
                    clearInputFields();
                    // Thông báo cho các panel khác cập nhật dữ liệu
                    notifyReaderDataChanged();
                } else {
                    // Hiển thị thông báo thất bại
                    JOptionPane.showMessageDialog(this, "Xoá thất bại.");
                }
            }
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Mã độc giả không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa độc giả: " + ex.getMessage());
        }
    }

    // ====== HÀM TÌM KIẾM ĐỘC GIẢ ======
    /**
     * Tìm kiếm độc giả theo từ khóa trên tất cả các trường
     * Nếu ô tìm kiếm rỗng sẽ hiển thị lại toàn bộ danh sách
     */
    private void searchReader() {
        // Lấy từ khóa tìm kiếm và chuyển về chữ thường
        String keyword = tfSearch.getText().trim().toLowerCase();
        // Nếu từ khóa rỗng, tải lại toàn bộ danh sách
        if (keyword.isEmpty()) {
            loadReaders(); // Tải lại toàn bộ danh sách nếu ô tìm kiếm trống
            return;
        }
        // Lấy toàn bộ danh sách độc giả
        List<Reader> readers = readerDAO.getAllReaders();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách độc giả để tìm kiếm
        for (Reader r : readers) {
            // Kiểm tra nếu từ khóa khớp với bất kỳ trường nào (tên, địa chỉ, SĐT, email)
            if (String.valueOf(r.getId()).contains(keyword) || // Thêm tìm kiếm theo ID
                r.getName().toLowerCase().contains(keyword) ||
                r.getAddress().toLowerCase().contains(keyword) ||
                r.getPhone().toLowerCase().contains(keyword) ||
                r.getEmail().toLowerCase().contains(keyword)) { // Thêm tìm kiếm theo email
                // Thêm hàng dữ liệu nếu khớp
                tableModel.addRow(new Object[]{
                        r.getId(), r.getName(), r.getAddress(), r.getPhone(), r.getEmail()
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
    
    /**
     * Xóa trắng các trường nhập liệu và bật lại ô ID
     */
    private void clearInputFields() {
        tfId.setText("");
        tfName.setText("");
        tfAddress.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
        tfId.setEnabled(true);
    }

    // Phương thức thông báo cho các panel khác cập nhật dữ liệu độc giả
    private void notifyReaderDataChanged() {
        // Tìm tất cả các panel trong ứng dụng
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            // Tìm tất cả các panel trong frame
            Component[] components = ((JFrame) parent).getContentPane().getComponents();
            for (Component comp : components) {
                if (comp instanceof BorrowingManagerPanel) {
                    // Gọi phương thức cập nhật dữ liệu của BorrowingManagerPanel
                    ((BorrowingManagerPanel) comp).loadComboBoxData();
                }
            }
        }
    }
}