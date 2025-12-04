package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Sach.Book;
import Sach.BookDAO;
import DocGia.Reader;
import DocGia.ReaderDAO;
import NhanVien.Staff;
import NhanVien.StaffDAO;
import MuonTra.Borrowing;
import MuonTra.BorrowingDAO;

/**
 * Simple chat assistant dialog that helps managers quickly discover
 * information about the modules that exist inside the application.
 */
public class ChatAssistantDialog extends JDialog {

    private final JTextArea conversationArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private final List<String> projectSections;
    private final BookDAO bookDAO = new BookDAO();
    private final ReaderDAO readerDAO = new ReaderDAO();
    private final StaffDAO staffDAO = new StaffDAO();
    private final BorrowingDAO borrowingDAO = new BorrowingDAO();

    public ChatAssistantDialog(Window owner, List<String> projectSections) {
        super(owner, "Trợ lý hỗ trợ", ModalityType.MODELESS);
        this.projectSections = projectSections;
        initUI();
        appendBotMessage(
                "Xin chào! Tôi là trợ lý chat của phần mềm quản lý thư viện.\n" +
                "Bạn có thể gõ từ khoá để tìm kiếm theo yêu cầu:\n" +
                "- 'tìm sách: <từ khóa>'\n" +
                "- 'thông tin sách: <mã sách>'\n" +
                "- 'thông tin độc giả: <mã độc giả>' để xem chi tiết."
        );
    }

    private void initUI() {
        setSize(420, 520);
        setLocationRelativeTo(getOwner());

        conversationArea.setEditable(false);
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);

        JButton sendButton = new JButton("Gửi");
        sendButton.addActionListener(this::handleSend);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSend(null);
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout(0, 10));
        add(new JScrollPane(conversationArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void handleSend(ActionEvent e) {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        appendUserMessage(text);
        inputField.setText("");

        String answer = answerUserQuestion(text);
        appendBotMessage(answer);
    }

    private void appendUserMessage(String msg) {
        conversationArea.append("Bạn: " + msg + "\n");
    }

    private void appendBotMessage(String msg) {
        conversationArea.append("Hệ thống:\n" + msg + "\n\n");
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    // ===== LOGIC TRẢ LỜI =====

    private String answerUserQuestion(String originalQuestion) {
        String q = originalQuestion.trim().toLowerCase();

        // 1. Tìm sách theo tên: "tìm sách: harry"
        if (q.startsWith("tìm sách:") || q.startsWith("tim sach:")) {
            String keyword = originalQuestion.substring(originalQuestion.indexOf(':') + 1).trim();
            if (keyword.isEmpty()) {
                return "Bạn hãy nhập theo mẫu: 'tìm sách: <từ khóa>'. Ví dụ: 'tìm sách: harry'.";
            }
            return searchBooksByKeyword(keyword);
        }

        // 2. Thông tin sách + lịch sử mượn: "thông tin sách: 5"
        if (q.startsWith("thông tin sách:") || q.startsWith("thong tin sach:")) {
            String idText = originalQuestion.substring(originalQuestion.indexOf(':') + 1).trim();
            try {
                int bookId = Integer.parseInt(idText);
                return getBookAndBorrowingsInfo(bookId);
            } catch (NumberFormatException ex) {
                return "Mã sách phải là số. Ví dụ: 'thông tin sách: 5'.";
            }
        }

        // 3. Thông tin độc giả + lịch sử mượn: "thông tin độc giả: 3"
        if (q.startsWith("thông tin độc giả:") || q.startsWith("thong tin doc gia:")
                || q.startsWith("thong tin độc giả:")) {
            String idText = originalQuestion.substring(originalQuestion.indexOf(':') + 1).trim();
            try {
                int readerId = Integer.parseInt(idText);
                return getReaderBorrowingsInfo(readerId);
            } catch (NumberFormatException ex) {
                return "Mã độc giả phải là số. Ví dụ: 'thông tin độc giả: 3'.";
            }
        }

        // Hướng dẫn chung + các câu hỏi cơ bản
        if (q.contains("tìm sách") || q.contains("tim sach")) {
            return "Bạn có thể gõ:\n"
                    + "- 'tìm sách: <từ khóa>' để liệt kê các sách có chứa từ khóa.\n"
                    + "- 'thông tin sách: <mã sách>' để xem chi tiết và lịch sử mượn.";
        }

        if (q.contains("độc giả") || q.contains("doc gia")) {
            return "Bạn có thể gõ: 'thông tin độc giả: <mã độc giả>' để xem thông tin và lịch sử mượn trả.";
        }

        if (q.contains("thống kê") || q.contains("bao cao")) {
            return "Các thống kê nằm trong tab 'Thống kê/Báo cáo'.\n"
                    + "Chatbot hiện chưa tạo thêm thống kê mới, chỉ hỗ trợ tra cứu sách và độc giả.";
        }

        if (q.contains("kê khai") || q.contains("liệt kê") || q.contains("danh sách") || q.contains("tất cả")) {
            if (projectSections != null && !projectSections.isEmpty()) {
                return "Hiện tại dự án có các phần: " + String.join(", ", projectSections);
            }
        }

        return "Hiện tại chatbot hỗ trợ:\n"
                + "- 'tìm sách: <từ khóa>'\n"
                + "- 'thông tin sách: <mã sách>'\n"
                + "- 'thông tin độc giả: <mã độc giả>'\n"
                + "Bạn thử nhập theo các mẫu trên nhé.";
    }

    // ===== HÀM PHỤ TRUY VẤN DỮ LIỆU =====

    // Tìm sách theo từ khóa (theo tiêu đề, không phân biệt hoa thường)
    private String searchBooksByKeyword(String keyword) {
        List<Book> allBooks = bookDAO.getAllBooks();
        String lower = keyword.toLowerCase();
        List<Book> matched = allBooks.stream()
                .filter(b -> b.getTitle() != null && b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());

        if (matched.isEmpty()) {
            return "Không tìm thấy sách nào với từ khóa: \"" + keyword + "\".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Các sách tìm được với từ khóa \"").append(keyword).append("\":\n");
        for (Book b : matched) {
            sb.append("- Mã: ").append(b.getId())
                    .append(" | Tên: ").append(b.getTitle())
                    .append(" | Năm XB: ").append(b.getYear())
                    .append(" | Tồn kho: ").append(b.getStock())
                    .append("\n");
        }
        sb.append("\nBạn có thể gõ 'thông tin sách: <mã>' để xem chi tiết và lịch sử mượn.");

        return sb.toString();
    }

    // Thông tin chi tiết 1 sách + các phiếu mượn liên quan
    private String getBookAndBorrowingsInfo(int bookId) {
        List<Book> allBooks = bookDAO.getAllBooks();
        Book book = allBooks.stream()
                .filter(b -> b.getId() == bookId)
                .findFirst().orElse(null);

        if (book == null) {
            return "Không tìm thấy sách có mã: " + bookId;
        }

        List<Borrowing> allBorrowings = borrowingDAO.getAllBorrowings();
        List<Reader> allReaders = readerDAO.getAllReaders();
        Map<Integer, Reader> readerMap = allReaders.stream()
                .collect(Collectors.toMap(Reader::getId, r -> r));

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("THÔNG TIN SÁCH\n");
        sb.append("- Mã: ").append(book.getId()).append("\n");
        sb.append("- Tên: ").append(book.getTitle()).append("\n");
        sb.append("- Năm XB: ").append(book.getYear()).append("\n");
        sb.append("- Giá: ").append(book.getPrice()).append("\n");
        sb.append("- Tồn kho: ").append(book.getStock()).append("\n\n");

        sb.append("LỊCH SỬ MƯỢN/TRẢ:\n");
        List<Borrowing> borrowsOfBook = allBorrowings.stream()
                .filter(b -> b.getBookId() == bookId)
                .collect(Collectors.toList());

        if (borrowsOfBook.isEmpty()) {
            sb.append("Chưa có phiếu mượn nào cho sách này.");
            return sb.toString();
        }

        for (Borrowing br : borrowsOfBook) {
            Reader r = readerMap.get(br.getReaderId());
            sb.append("- Phiếu: ").append(br.getId());
            sb.append(" | Độc giả: ")
                    .append(r != null ? r.getName() + " (ID " + r.getId() + ")" : "Không rõ");
            sb.append(" | Ngày mượn: ")
                    .append(br.getBorrowDate() != null ? df.format(br.getBorrowDate()) : "N/A");
            sb.append(" | Ngày trả: ")
                    .append(br.getReturnDate() != null ? df.format(br.getReturnDate()) : "chưa trả");
            sb.append(" | Trạng thái: ").append(br.getStatus());
            sb.append("\n");
        }

        return sb.toString();
    }

    // Thông tin độc giả + các phiếu mượn của độc giả đó
    private String getReaderBorrowingsInfo(int readerId) {
        List<Reader> allReaders = readerDAO.getAllReaders();
        Reader reader = allReaders.stream()
                .filter(r -> r.getId() == readerId)
                .findFirst().orElse(null);

        if (reader == null) {
            return "Không tìm thấy độc giả có mã: " + readerId;
        }

        List<Borrowing> allBorrowings = borrowingDAO.getAllBorrowings();
        List<Book> allBooks = bookDAO.getAllBooks();
        Map<Integer, Book> bookMap = allBooks.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("THÔNG TIN ĐỘC GIẢ\n");
        sb.append("- Mã: ").append(reader.getId()).append("\n");
        sb.append("- Tên: ").append(reader.getName()).append("\n");
        sb.append("- SĐT: ").append(reader.getPhone()).append("\n");
        sb.append("- Địa chỉ: ").append(reader.getAddress()).append("\n");
        sb.append("- Email: ").append(reader.getEmail()).append("\n\n");

        sb.append("CÁC PHIẾU MƯỢN/TRẢ:\n");
        List<Borrowing> borrowsOfReader = allBorrowings.stream()
                .filter(b -> b.getReaderId() == readerId)
                .collect(Collectors.toList());

        if (borrowsOfReader.isEmpty()) {
            sb.append("Độc giả này chưa có phiếu mượn nào.");
            return sb.toString();
        }

        for (Borrowing br : borrowsOfReader) {
            Book book = bookMap.get(br.getBookId());
            sb.append("- Phiếu: ").append(br.getId());
            sb.append(" | Sách: ")
                    .append(book != null ? book.getTitle() + " (ID " + book.getId() + ")" : "Không rõ");
            sb.append(" | Ngày mượn: ")
                    .append(br.getBorrowDate() != null ? df.format(br.getBorrowDate()) : "N/A");
            sb.append(" | Ngày trả: ")
                    .append(br.getReturnDate() != null ? df.format(br.getReturnDate()) : "chưa trả");
            sb.append(" | Trạng thái: ").append(br.getStatus());
            sb.append("\n");
        }

        return sb.toString();
    }
}