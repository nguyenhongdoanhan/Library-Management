package ThongKe;

import MuonTra.Borrowing;
import MuonTra.BorrowingDAO;
import Sach.Book;
import Sach.BookDAO;
import TheLoai.Category;
import TheLoai.CategoryDAO;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;

public class StatisticsPanel extends JPanel {

    public StatisticsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));

        JLabel titleLabel = new JLabel("Panel Thống kê và Báo cáo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Khu vực này sẽ chứa các biểu đồ, bảng thống kê chi tiết
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // Chia làm 2 phần
        contentPanel.setBackground(new Color(235, 245, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel bên trái chứa bảng thống kê
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Bảng thống kê sách được mượn nhiều nhất
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Mã sách", "Tiêu đề sách", "Số lượt mượn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp
            }
        };
        JTable statisticsTable = new JTable(tableModel);
        statisticsTable.setRowHeight(25);
        statisticsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statisticsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        statisticsTable.setSelectionBackground(new Color(200, 230, 255));

        // Điều chỉnh độ rộng cột
        statisticsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        statisticsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        statisticsTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(statisticsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel);

        // Panel bên phải chứa biểu đồ tròn
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(createPieChart(), BorderLayout.CENTER);
        contentPanel.add(chartPanel);

        add(contentPanel, BorderLayout.CENTER);

        // ====== PANEL NÚT CHỨC NĂNG Ở DƯỚI ======
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 235, 245));
        JButton btnLoad = new JButton("Tải lại");

        btnLoad.setBackground(new Color(100, 180, 255));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);
        btnLoad.setFont(new Font("Arial", Font.BOLD, 13));

        buttonPanel.add(btnLoad);
        add(buttonPanel, BorderLayout.SOUTH); // Đặt panel nút ở dưới cùng

        // Load dữ liệu thống kê khi panel được tạo
        loadMostBorrowedBooks(tableModel);
        
        // ====== SỰ KIỆN ======
        btnLoad.addActionListener(e -> {
            loadMostBorrowedBooks(tableModel);
            updatePieChart(chartPanel);
        });
    }

    // Phương thức tạo biểu đồ tròn
    private JPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Lấy dữ liệu từ database
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        BookDAO bookDAO = new BookDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        
        List<Borrowing> borrowings = borrowingDAO.getAllBorrowings();
        List<Book> allBooks = bookDAO.getAllBooks();
        List<Category> allCategories = categoryDAO.getAllCategories();
        
        // Đếm số sách đang mượn theo thể loại
        Map<Integer, Integer> categoryCounts = new HashMap<>();
        for (Borrowing b : borrowings) {
            if ("Đang mượn".equals(b.getStatus())) {
                for (Book book : allBooks) {
                    if (book.getId() == b.getBookId()) {
                        categoryCounts.put(book.getCategoryId(), 
                            categoryCounts.getOrDefault(book.getCategoryId(), 0) + 1);
                        break;
                    }
                }
            }
        }
        
        // Thêm dữ liệu vào dataset
        for (Map.Entry<Integer, Integer> entry : categoryCounts.entrySet()) {
            for (Category cat : allCategories) {
                if (cat.getId() == entry.getKey()) {
                    dataset.setValue(cat.getName(), entry.getValue());
                    break;
                }
            }
        }
        
        // Tạo biểu đồ
        JFreeChart chart = ChartFactory.createPieChart(
            "Tỷ lệ sách đang mượn theo thể loại",  // Tiêu đề
            dataset,                              // Dataset
            true,                                 // Legend
            true,                                 // Tooltips
            false                                 // URLs
        );
        
        // Tùy chỉnh font chữ
        chart.setTitle(new TextTitle("Tỷ lệ sách đang mượn theo thể loại", 
            new Font("Arial", Font.BOLD, 16)));
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        
        // Tạo panel chứa biểu đồ
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        return chartPanel;
    }
    
    // Phương thức cập nhật biểu đồ tròn
    private void updatePieChart(JPanel chartPanel) {
        chartPanel.removeAll();
        chartPanel.add(createPieChart(), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // Phương thức để tải và hiển thị thống kê sách được mượn nhiều nhất
    private void loadMostBorrowedBooks(DefaultTableModel tableModel) {
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        BookDAO bookDAO = new BookDAO();
        List<Borrowing> borrowings = borrowingDAO.getAllBorrowings();
        List<Book> allBooks = bookDAO.getAllBooks();

        // Đếm số lượt mượn cho mỗi sách (chỉ tính trạng thái "Đang mượn")
        Map<Integer, Integer> borrowedCounts = new HashMap<>();
        for (Borrowing b : borrowings) {
            // Chỉ đếm nếu trạng thái là "Đang mượn"
            if ("Đang mượn".equals(b.getStatus())) {
                borrowedCounts.put(b.getBookId(), borrowedCounts.getOrDefault(b.getBookId(), 0) + 1);
            }
        }

        // Sắp xếp sách theo số lượt mượn giảm dần
        Map<Integer, Integer> sortedBorrowedCounts = borrowedCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Clear bảng hiện tại
        tableModel.setRowCount(0);

        // Thêm dữ liệu vào bảng
        for (Map.Entry<Integer, Integer> entry : sortedBorrowedCounts.entrySet()) {
            int bookId = entry.getKey();
            int count = entry.getValue();
            String bookTitle = "Không rõ";
            for (Book book : allBooks) {
                if (book.getId() == bookId) {
                    bookTitle = book.getTitle();
                    break;
                }
            }
            tableModel.addRow(new Object[]{bookId, bookTitle, count});
        }
    }
} 