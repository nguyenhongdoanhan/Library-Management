package NhanVien;

import MuonTra.BorrowingManagerPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Lớp panel quản lý thông tin Nhân viên
public class StaffManagerPanel extends JPanel {

    // Khai báo các thành phần giao diện
    private JTable table; // Bảng hiển thị danh sách nhân viên
    private DefaultTableModel tableModel; // Model cho bảng
    // Các trường nhập liệu và tìm kiếm
    private JTextField tfStaffId, tfName, tfPhone, tfEmail, tfSearch;
    private JComboBox<String> cbPosition; // ComboBox chọn Chức vụ

    // Khai báo DAO để tương tác với cơ sở dữ liệu nhân viên
    private StaffDAO staffDAO = new StaffDAO();

    // Constructor của lớp StaffManagerPanel
    public StaffManagerPanel() {
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
                new TitledBorder("Thông tin Nhân viên")
        ));
        inputPanel.setBackground(new Color(235, 245, 255));
        // Khởi tạo GridBagConstraints để cấu hình vị trí và kích thước các thành phần trong GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cho phép giãn theo chiều ngang
        // gbc.weightx = 1.0; // Give extra horizontal space to components that can expand (adjusted)

        // Khởi tạo các trường nhập liệu
        tfStaffId = new JTextField(10);
        tfName = new JTextField(15);
        tfPhone = new JTextField(10);
        tfEmail = new JTextField(15);
        // Khởi tạo ComboBox chức vụ
        cbPosition = new JComboBox<>(new String[]{"Thủ kho", "Quản lý", "Nhân viên"});

        // Áp dụng style bo góc cho các ô nhập liệu và ComboBox
        JTextField[] textFields = {tfStaffId, tfName, tfPhone, tfEmail};
        for (JTextField f : textFields) {
            f.setBorder(new RoundedBorder(8));
            f.setBackground(new Color(255, 255, 255));
        }
        cbPosition.setBackground(Color.WHITE);

        // Thêm các components vào panel nhập liệu với GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5; // Field
        inputPanel.add(tfStaffId, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5; // Field
        inputPanel.add(tfName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5; // Field
        inputPanel.add(tfPhone, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5; // Field
        inputPanel.add(tfEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0; // Label
        inputPanel.add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5; // Field
        // gbc.gridwidth = 3; // Span across remaining columns if needed
        inputPanel.add(cbPosition, gbc);
        // gbc.gridwidth = 1; // Reset gridwidth

        // ====== GỘP PANEL TÌM KIẾM VÀ NHẬP LIỆU Ở TRÊN ======
        // Tạo panel chứa cả searchPanel và inputPanel và đặt ở phía trên cùng của panel chính
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ====== BẢNG HIỂN THỊ DỮ LIỆU ======
        // Khởi tạo tableModel với các cột và không cho phép sửa trực tiếp
        tableModel = new DefaultTableModel(new String[]{
            "Mã NV", "Họ tên", "Chức vụ", "SĐT", "Email"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
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
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã NV
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Họ tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // SĐT
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Email
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Chức vụ

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
        btnLoad.addActionListener(e -> loadStaffs());
        btnAdd.addActionListener(e -> addStaff());
        btnUpdate.addActionListener(e -> updateStaff());
        btnDelete.addActionListener(e -> deleteStaff());
        btnSearch.addActionListener(e -> searchStaff());
        tfSearch.addActionListener(e -> searchStaff()); // Thêm listener cho Enter trong ô tìm kiếm

        // Thêm MouseListener cho bảng để xử lý sự kiện click chuột vào hàng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tfStaffId.setText(tableModel.getValueAt(row, 0).toString()); // Mã NV
                    tfName.setText(tableModel.getValueAt(row, 1).toString());    // Họ tên
                    cbPosition.setSelectedItem(tableModel.getValueAt(row, 2).toString()); // Chức vụ
                    tfPhone.setText(tableModel.getValueAt(row, 3).toString());   // SĐT
                    tfEmail.setText(tableModel.getValueAt(row, 4).toString());   // Email
                    tfStaffId.setEnabled(false);
                }
            }
        });

        // Tải dữ liệu nhân viên lần đầu khi panel được hiển thị
        loadStaffs();
        clearInputFields(); // Xóa trắng các ô nhập lúc đầu
    }

    // Phương thức để tải danh sách nhân viên từ database và hiển thị lên bảng
    private void loadStaffs() {
        // Lấy danh sách nhân viên từ DAO
        List<Staff> staffs = staffDAO.getAllStaffs();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách nhân viên và thêm vào bảng
        for (Staff s : staffs) {
            String name = s.getName() != null ? s.getName() : "(Không xác định)";
            String position = s.getPosition() != null ? s.getPosition() : "(Không xác định)";
            String phone = s.getPhone() != null ? s.getPhone() : "(Không xác định)";
            String email = s.getEmail() != null ? s.getEmail() : "(Không xác định)";
            tableModel.addRow(new Object[]{
                s.getId(), name, position, phone, email
            });
        }
        clearInputFields(); // Xóa trắng ô nhập sau khi tải lại
    }

    // Phương thức xử lý thêm nhân viên mới
    private void addStaff() {
        try {
            if (tfName.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty() || cbPosition.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Họ tên, SĐT, Email, Chức vụ).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Tạo đối tượng Staff đúng thứ tự constructor (id, name, position, phone, email)
            Staff s = new Staff(0,
                    tfName.getText().trim(),
                    cbPosition.getSelectedItem().toString(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim()
            );
            if (staffDAO.insertStaff(s)) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadStaffs();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyStaffDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + ex.getMessage());
        }
    }

    // Phương thức xử lý cập nhật thông tin nhân viên
    private void updateStaff() {
        try {
            if (tfStaffId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần cập nhật từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (tfName.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty() || cbPosition.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Họ tên, SĐT, Email, Chức vụ).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Tạo đối tượng Staff đúng thứ tự constructor (id, name, position, phone, email)
            Staff s = new Staff(
                    Integer.parseInt(tfStaffId.getText()),
                    tfName.getText().trim(),
                    cbPosition.getSelectedItem().toString(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim()
            );
            if (staffDAO.updateStaff(s)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadStaffs();
                clearInputFields();
                // Thông báo cho các panel khác cập nhật dữ liệu
                notifyStaffDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhân viên: " + ex.getMessage());
        }
    }

    // Phương thức xử lý xóa nhân viên
    private void deleteStaff() {
        try {
            // Kiểm tra xem đã chọn nhân viên từ bảng chưa
            if (tfStaffId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Lấy ID nhân viên cần xóa
            int id = Integer.parseInt(tfStaffId.getText());
            // Hiển thị hộp thoại xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn xoá nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            // Nếu người dùng xác nhận xóa
            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi phương thức delete của DAO
                if (staffDAO.deleteStaff(id)) {
                    // Hiển thị thông báo thành công, tải lại bảng, và xóa trắng ô nhập
                    JOptionPane.showMessageDialog(this, "Xoá thành công!");
                    loadStaffs();
                    clearInputFields();
                    // Thông báo cho các panel khác cập nhật dữ liệu
                    notifyStaffDataChanged();
                } else {
                    // Hiển thị thông báo thất bại
                    JOptionPane.showMessageDialog(this, "Xoá thất bại.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Xử lý lỗi và hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên: " + ex.getMessage());
        }
    }

    // Phương thức xử lý tìm kiếm nhân viên
    private void searchStaff() {
        // Lấy từ khóa tìm kiếm và chuyển về chữ thường
        String keyword = tfSearch.getText().trim().toLowerCase();
        // Nếu từ khóa rỗng, tải lại toàn bộ danh sách
        if (keyword.isEmpty()) {
            loadStaffs(); // Tải lại toàn bộ danh sách nếu ô tìm kiếm trống
            return;
        }
        // Lấy toàn bộ danh sách nhân viên
        List<Staff> staffs = staffDAO.getAllStaffs();
        // Xóa dữ liệu cũ trên bảng
        tableModel.setRowCount(0);
        // Duyệt qua danh sách nhân viên để tìm kiếm
        for (Staff s : staffs) {
            // Kiểm tra nếu từ khóa khớp với bất kỳ trường nào (ID, tên, SĐT, email, chức vụ)
            if (String.valueOf(s.getId()).contains(keyword)
                    || s.getName().toLowerCase().contains(keyword)
                    || s.getPosition().toLowerCase().contains(keyword)
                    || s.getPhone().toLowerCase().contains(keyword)
                    || s.getEmail().toLowerCase().contains(keyword)) {
                // Thêm hàng dữ liệu nếu khớp
                tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getPosition(),
                    s.getPhone(),
                    s.getEmail()
                });
            }
        }
    }

    // ====== CLASS BO GÓC CHO Ô NHẬP ======
    // Lớp nội bộ để tạo hiệu ứng bo tròn cho viền các ô nhập liệu
    static class RoundedBorder extends AbstractBorder {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            // Vẽ hình chữ nhật bo góc
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Điều chỉnh khoảng cách nội dung bên trong border
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            // Điều chỉnh khoảng cách nội dung bên trong border (override)
            insets.left = insets.right = insets.top = insets.bottom = this.radius + 1;
            return insets;
        }
    }

    // Phương thức tiện ích để xóa trắng các ô nhập liệu và bật lại ô ID
    private void clearInputFields() {
        tfStaffId.setText("");
        tfName.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
        cbPosition.setSelectedIndex(-1); // Chọn item đầu tiên (nếu có) hoặc không chọn gì (-1)
        tfStaffId.setEnabled(true); // Bật lại ô ID
    }

    // Phương thức thông báo cho các panel khác cập nhật dữ liệu nhân viên
    private void notifyStaffDataChanged() {
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
