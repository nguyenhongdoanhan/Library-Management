package MuonTra;

import DocGia.Reader;
import DocGia.ReaderDAO;
import NhanVien.Staff;
import NhanVien.StaffDAO;
import Sach.Book;
import Sach.BookDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

// Import cho JDateChooser (Cần thêm thư viện JCalendar vào project)
// import com.toedter.calendar.JDateChooser;

// Import DAO và Model cho mượn/trả sách khi cần
// import dao.BorrowingDAO;
// import model.Borrowing;

public class BorrowingManagerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    // Thêm các JTextField hoặc JComboBox cho nhập liệu mượn/trả sách tại đây
    private JTextField tfBorrowId, tfSearch, tfBorrowDate, tfReturnDate;
    private JComboBox<Book> cbBook;
    private JComboBox<Reader> cbReader;
    private JComboBox<Staff> cbStaff; // Thêm JComboBox cho Nhân viên
    private JComboBox<String> cbStatus; // Thay thế JTextField tfStatus bằng JComboBox

    // Khai báo DAO khi cần
    private BorrowingDAO borrowingDAO = new BorrowingDAO();
    private BookDAO bookDAO = new BookDAO();
    private ReaderDAO readerDAO = new ReaderDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BorrowingManagerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 255));

        // ====== PANEL TÌM KIẾM Ở TRÊN ======
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

        // ====== GỘP PANEL TÌM KIẾM VÀ NHẬP LIỆU Ở TRÊN ======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        // ====== PANEL NHẬP LIỆU Ở GIỮA ======
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new TitledBorder("Thông tin Mượn/Trả")
        ));
        inputPanel.setBackground(new Color(235, 245, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.weightx = 1.0; // Cho phép giãn theo chiều ngang

        // Thêm các thành phần nhập liệu mượn/trả sách tại đây
        tfBorrowId = new JTextField(10);
        cbBook = new JComboBox<>();
        cbReader = new JComboBox<>();
        cbStaff = new JComboBox<>(); // Khởi tạo JComboBox cho Nhân viên
        cbStatus = new JComboBox<>(new String[]{"Đang mượn", "Đã trả"}); // Khởi tạo ComboBox trạng thái
        tfBorrowDate = new JTextField(10); // Thay thế JDateChooser bằng JTextField
        tfReturnDate = new JTextField(10); // Thay thế JDateChooser bằng JTextField

        // Load dữ liệu vào ComboBox Sách, Độc giả, và Nhân viên
        loadComboBoxData();

        // Tạm thời giữ class RoundedBorder ở đây nếu cần sử dụng lại
        // Copy class RoundedBorder từ BookManagerPanel hoặc ReaderManagerPanel.
         JTextField[] textFields = {tfBorrowId, tfBorrowDate, tfReturnDate};
         for (JTextField f : textFields) {
             f.setBorder(new RoundedBorder(8));
             f.setBackground(new Color(255, 255, 255));
         }
         cbBook.setBackground(Color.WHITE);
         cbReader.setBackground(Color.WHITE);
         cbStaff.setBackground(Color.WHITE); // Style cho ComboBox Nhân viên
         cbStatus.setBackground(Color.WHITE);

        // Ví dụ:
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Mã Mượn:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(tfBorrowId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Sách:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(cbBook, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Độc giả:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(cbReader, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Nhân viên:"), gbc); // Thêm Label cho Nhân viên
        gbc.gridx = 3; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(cbStaff, gbc); // Thêm ComboBox cho Nhân viên

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Ngày Mượn:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(tfBorrowDate, gbc); // Thêm JTextField ngày mượn

        gbc.gridx = 2; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        inputPanel.add(new JLabel("Ngày Trả:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5;
        inputPanel.add(tfReturnDate, gbc); // Thêm JTextField ngày trả

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; // Chuyển xuống dòng mới
        inputPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.5; // Chuyển xuống dòng mới
        gbc.gridwidth = 3; // Mở rộng hết phần còn lại của dòng
        inputPanel.add(cbStatus, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        topPanel.add(inputPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ====== BẢNG HIỂN THỊ DỮ LIỆU ======
        tableModel = new DefaultTableModel(new String[]{
                "Mã Mượn", "Sách", "Độc giả", "Nhân viên", "Ngày Mượn", "Ngày Trả", "Trạng thái"
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
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã Mượn
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Sách
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Độc giả
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Nhân viên
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Ngày Mượn
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Ngày Trả
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER); // Đặt bảng ở giữa

        // ====== PANEL NÚT CHỨC NĂNG Ở DƯỚI ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 235, 245));
        JButton btnLoad = new JButton("Tải lại");
        JButton btnAdd = new JButton("Thêm"); // Có thể là nút "Tạo phiếu mượn"
        JButton btnUpdate = new JButton("Sửa"); // Có thể là nút "Cập nhật trạng thái" hoặc "Ghi trả sách"
        JButton btnDelete = new JButton("Xóa"); // Có thể là nút "Hủy phiếu mượn"

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
        add(buttonPanel, BorderLayout.SOUTH); // Đặt panel nút ở dưới cùng

        // ====== SỰ KIỆN ======
        // Thêm các ActionListener cho các nút tại đây
        btnLoad.addActionListener(e -> {
            loadBorrowings();
            loadComboBoxData();
        });
        btnSearch.addActionListener(e -> searchBorrowing());
        tfSearch.addActionListener(e -> searchBorrowing());

        btnAdd.addActionListener(e -> addBorrowing());
        btnUpdate.addActionListener(e -> updateBorrowing());
        btnDelete.addActionListener(e -> deleteBorrowing());

        // Thêm MouseListener cho bảng nếu cần để hiển thị thông tin lên các trường nhập liệu
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfBorrowId.setText(tableModel.getValueAt(row, 0).toString());
                if (row >= 0) {
                    tfBorrowId.setText(tableModel.getValueAt(row, 0).toString());

                    try {
                        int borrowingId = Integer.parseInt(tfBorrowId.getText());
                        Borrowing selectedBorrowing = borrowingDAO.getBorrowingById(borrowingId);

                        if (selectedBorrowing != null) {
                            // Sử dụng dữ liệu từ đối tượng Borrowing để điền vào các trường nhập liệu
                            setSelectedComboBoxById(cbBook, selectedBorrowing.getBookId());
                            setSelectedComboBoxById(cbReader, selectedBorrowing.getReaderId());
                            setSelectedComboBoxById(cbStaff, selectedBorrowing.getStaffId()); // Set ComboBox Nhân viên
                            tfBorrowDate.setText(selectedBorrowing.getBorrowDate() != null ? dateFormat.format(selectedBorrowing.getBorrowDate()) : "");
                            tfReturnDate.setText(selectedBorrowing.getReturnDate() != null ? dateFormat.format(selectedBorrowing.getReturnDate()) : "");
                            cbStatus.setSelectedItem(selectedBorrowing.getStatus());
                        }

                    } catch (NumberFormatException ex) {
                        System.err.println("Error parsing Borrowing ID: " + ex.getMessage());
                        // Handle the error, perhaps clear fields or show a message
                         clearInputFields();
                    } catch (Exception ex) {
                         System.err.println("Error loading borrowing data: " + ex.getMessage());
                         // Handle other exceptions
                         clearInputFields();
                    }

                    tfBorrowId.setEnabled(false);
                }
            }
        });

        // Gọi hàm load dữ liệu ban đầu
        loadBorrowings();
        clearInputFields(); // Xóa trắng các ô nhập lúc đầu
    }

    public void loadComboBoxData() {
         // Clear existing items
        cbBook.removeAllItems();
        cbReader.removeAllItems();
        cbStaff.removeAllItems(); // Clear ComboBox Nhân viên

        // Load data
        for (Book book : bookDAO.getAllBooks()) cbBook.addItem(book);
        for (Reader reader : readerDAO.getAllReaders()) cbReader.addItem(reader);
        for (Staff staff : staffDAO.getAllStaffs()) cbStaff.addItem(staff); // Load data cho ComboBox Nhân viên
    }

    // ====== CÁC HÀM XỬ LÝ DỮ LIỆU (Cần triển khai) ======
    private void loadBorrowings() {
        List<Borrowing> borrowings = borrowingDAO.getAllBorrowings();
        tableModel.setRowCount(0);

        // Lấy danh sách sách, độc giả, và nhân viên để hiển thị tên
        List<Book> allBooks = bookDAO.getAllBooks();
        List<Reader> allReaders = readerDAO.getAllReaders();
        List<Staff> allStaffs = staffDAO.getAllStaffs();

        for (Borrowing b : borrowings) {
            String bookTitle = "(Không xác định)";
            String readerName = "(Không xác định)";
            String staffName = "(Không xác định)";
            for(Book book : allBooks) {
                if (book.getId() == b.getBookId()) {
                    bookTitle = book.getTitle();
                    break;
                }
            }
            for(Reader reader : allReaders) {
                if (reader.getId() == b.getReaderId()) {
                    readerName = reader.getName();
                    break;
                }
            }
            for(Staff staff : allStaffs) {
                if (staff.getId() == b.getStaffId()) {
                    staffName = staff.getName();
                    break;
                }
            }
            tableModel.addRow(new Object[]{
                b.getId(),
                bookTitle,
                readerName,
                staffName,
                b.getBorrowDate(),
                b.getReturnDate(),
                b.getStatus()
            });
        }
    }

    private void addBorrowing() {
        try {
            Book selectedBook = (Book) cbBook.getSelectedItem();
            Reader selectedReader = (Reader) cbReader.getSelectedItem();
            Staff selectedStaff = (Staff) cbStaff.getSelectedItem(); // Lấy Nhân viên được chọn
            String status = (String) cbStatus.getSelectedItem();
            Date borrowUtilDate = null;
            try {
                borrowUtilDate = dateFormat.parse(tfBorrowDate.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ngày mượn không hợp lệ! Định dạng đúng: yyyy-MM-dd", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedBook == null || selectedReader == null || selectedStaff == null || status == null || borrowUtilDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Sách, Độc giả, Nhân viên, Ngày Mượn, Trạng thái).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = selectedBook.getId();
            int readerId = selectedReader.getId();
            int staffId = selectedStaff.getId(); // Lấy ID Nhân viên
            java.sql.Date borrowSqlDate = new java.sql.Date(borrowUtilDate.getTime());

            // Tạo đối tượng Borrowing từ dữ liệu nhập liệu (luôn dùng ID 0 cho thêm mới)
            Borrowing newBorrowing = new Borrowing(0, readerId, bookId, staffId, borrowSqlDate, null, status);

            if (borrowingDAO.insertBorrowing(newBorrowing)) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu mượn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadBorrowings();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phiếu mượn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phiếu mượn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBorrowing() {
        try {
            if (tfBorrowId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn cần cập nhật từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfBorrowId.getText());
            Book selectedBook = (Book) cbBook.getSelectedItem();
            Reader selectedReader = (Reader) cbReader.getSelectedItem();
            Staff selectedStaff = (Staff) cbStaff.getSelectedItem(); // Lấy Nhân viên được chọn
            String status = (String) cbStatus.getSelectedItem();
            Date borrowUtilDate = null;
            Date returnUtilDate = null;
            try {
                borrowUtilDate = dateFormat.parse(tfBorrowDate.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ngày mượn không hợp lệ! Định dạng đúng: yyyy-MM-dd", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if (!tfReturnDate.getText().trim().isEmpty()) {
                    returnUtilDate = dateFormat.parse(tfReturnDate.getText());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ngày trả không hợp lệ! Định dạng đúng: yyyy-MM-dd", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedBook == null || selectedReader == null || selectedStaff == null || status == null || borrowUtilDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (Sách, Độc giả, Nhân viên, Ngày Mượn, Trạng thái).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = selectedBook.getId();
            int readerId = selectedReader.getId();
            int staffId = selectedStaff.getId(); // Lấy ID Nhân viên
            java.sql.Date borrowSqlDate = new java.sql.Date(borrowUtilDate.getTime());
            java.sql.Date returnSqlDate = null;
            if (returnUtilDate != null) {
                returnSqlDate = new java.sql.Date(returnUtilDate.getTime());
            }

            Borrowing updatedBorrowing = new Borrowing(id, readerId, bookId, staffId, borrowSqlDate, returnSqlDate, status);

            if (borrowingDAO.updateBorrowing(updatedBorrowing)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu mượn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadBorrowings();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu mượn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã mượn không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phiếu mượn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBorrowing() {
        try {
            if (tfBorrowId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn cần xóa từ bảng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfBorrowId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa phiếu mượn này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (borrowingDAO.deleteBorrowing(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa phiếu mượn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadBorrowings();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa phiếu mượn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phiếu mượn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBorrowing() {
        String keyword = tfSearch.getText().trim().toLowerCase();
        List<Borrowing> allBorrowings = borrowingDAO.getAllBorrowings();
        tableModel.setRowCount(0);

        // Lấy danh sách sách, độc giả, và nhân viên để tìm kiếm theo tên
        List<Book> allBooks = bookDAO.getAllBooks();
        List<Reader> allReaders = readerDAO.getAllReaders();
        List<Staff> allStaffs = staffDAO.getAllStaffs();

        for (Borrowing b : allBorrowings) {
            String bookTitle = "(Không xác định)";
            String readerName = "(Không xác định)";
            String staffName = "(Không xác định)";
            String borrowDateStr = (b.getBorrowDate() != null) ? dateFormat.format(b.getBorrowDate()) : "";
            String returnDateStr = (b.getReturnDate() != null) ? dateFormat.format(b.getReturnDate()) : "";

            // Tìm tên sách
            for(Book book : allBooks) {
                if (book.getId() == b.getBookId()) {
                    bookTitle = book.getTitle();
                    break;
                }
            }

            // Tìm tên độc giả
            for(Reader reader : allReaders) {
                if (reader.getId() == b.getReaderId()) {
                    readerName = reader.getName();
                    break;
                }
            }
            
            // Tìm tên nhân viên
            for(Staff staff : allStaffs) {
                if (staff.getId() == b.getStaffId()) {
                    staffName = staff.getName();
                    break;
                }
            }

            // Kiểm tra nếu từ khóa khớp với bất kỳ thông tin nào
            if (String.valueOf(b.getId()).contains(keyword) ||
                bookTitle.toLowerCase().contains(keyword) ||
                readerName.toLowerCase().contains(keyword) ||
                staffName.toLowerCase().contains(keyword) ||
                borrowDateStr.toLowerCase().contains(keyword) ||
                returnDateStr.toLowerCase().contains(keyword) ||
                b.getStatus().toLowerCase().contains(keyword)) {

                tableModel.addRow(new Object[]{
                    b.getId(),
                    bookTitle,
                    readerName,
                    staffName,
                    b.getBorrowDate(),
                    b.getReturnDate(),
                    b.getStatus()
                });
            }
        }
    }

    // Phương thức trợ giúp để xóa các trường nhập
    private void clearInputFields() {
        tfBorrowId.setText("");
        cbBook.setSelectedIndex(-1);
        cbReader.setSelectedIndex(-1);
        cbStaff.setSelectedIndex(-1);
        tfBorrowDate.setText("");
        tfReturnDate.setText("");
        cbStatus.setSelectedIndex(-1);
        tfBorrowId.setEnabled(true);
    }

    // Phương thức trợ giúp để thiết lập mục được chọn trong JComboBox theo ID
    private <T> void setSelectedComboBoxById(JComboBox<T> comboBox, int id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object item = comboBox.getItemAt(i);
            if (item instanceof Book) {
                if (((Book) item).getId() == id) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            } else if (item instanceof Reader) {
                 if (((Reader) item).getId() == id) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            } else if (item instanceof Staff) { // Thêm xử lý cho Staff
                 if (((Staff) item).getId() == id) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // ====== CLASS BO GÓC CHO Ô NHẬP (Nếu cần sử dụng lại) ======
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
} 