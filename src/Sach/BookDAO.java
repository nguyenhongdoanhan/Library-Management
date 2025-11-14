package Sach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import MuonTra.BorrowingDAO;
import util.DBConnect; // Sử dụng lớp DBConnect từ package util

public class BookDAO {

    // Lấy toàn bộ danh sách sách từ cơ sở dữ liệu
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM book";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getInt("author_id"),
                    rs.getInt("publisher_id"),
                    rs.getInt("category_id"),
                    rs.getInt("year"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                list.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm một cuốn sách mới vào cơ sở dữ liệu
    public boolean insertBook(Book book) {
        String sql = "INSERT INTO book(title, author_id, publisher_id, category_id, year, price, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getPublisherId());
            stmt.setInt(4, book.getCategoryId());
            stmt.setInt(5, book.getYear());
            stmt.setDouble(6, book.getPrice());
            stmt.setInt(7, book.getStock());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật thông tin sách
    public boolean updateBook(Book book) {
        String sql = "UPDATE book SET title=?, author_id=?, publisher_id=?, category_id=?, year=?, price=?, stock=? WHERE book_id=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getPublisherId());
            stmt.setInt(4, book.getCategoryId());
            stmt.setInt(5, book.getYear());
            stmt.setDouble(6, book.getPrice());
            stmt.setInt(7, book.getStock());
            stmt.setInt(8, book.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá sách theo mã book_id
    public boolean deleteBook(int id) {
        // Trước khi xoá sách, cần xoá các phiếu mượn liên quan
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        borrowingDAO.deleteBorrowingsByBookId(id);

        String sql = "DELETE FROM book WHERE book_id=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phương thức lấy danh sách sách theo mã tác giả
    public List<Book> getBooksByAuthorId(int authorId) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE author_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("author_id"),
                        rs.getInt("publisher_id"),
                        rs.getInt("category_id"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                    list.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Phương thức xóa sách theo mã tác giả (xóa cả các phiếu mượn liên quan)
    public boolean deleteBooksByAuthorId(int authorId) {
        List<Book> booksToDelete = getBooksByAuthorId(authorId);
        boolean allDeleted = true;
        for (Book book : booksToDelete) {
            if (!deleteBook(book.getId())) { // Sử dụng lại phương thức deleteBook đã có
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    // Phương thức lấy danh sách sách theo mã nhà xuất bản
    public List<Book> getBooksByPublisherId(int publisherId) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE publisher_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, publisherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("author_id"),
                        rs.getInt("publisher_id"),
                        rs.getInt("category_id"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                    list.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Phương thức xóa sách theo mã nhà xuất bản (xóa cả các phiếu mượn liên quan)
    public boolean deleteBooksByPublisherId(int publisherId) {
        List<Book> booksToDelete = getBooksByPublisherId(publisherId);
        boolean allDeleted = true;
        for (Book book : booksToDelete) {
            if (!deleteBook(book.getId())) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    // Phương thức lấy danh sách sách theo mã thể loại
    public List<Book> getBooksByCategoryId(int categoryId) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE category_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("author_id"),
                        rs.getInt("publisher_id"),
                        rs.getInt("category_id"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                    list.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Phương thức xóa sách theo mã thể loại (xóa cả các phiếu mượn liên quan)
    public boolean deleteBooksByCategoryId(int categoryId) {
        List<Book> booksToDelete = getBooksByCategoryId(categoryId);
        boolean allDeleted = true;
        for (Book book : booksToDelete) {
            if (!deleteBook(book.getId())) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    // Phương thức đặt author_id của sách về NULL theo mã tác giả
    public boolean setAuthorIdToNullByAuthorId(int authorId) {
        String sql = "UPDATE book SET author_id = NULL WHERE author_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.executeUpdate(); // executeUpdate trả về số dòng bị ảnh hưởng
            return true; // Giả định thành công nếu không có ngoại lệ
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức đặt publisher_id của sách về NULL theo mã nhà xuất bản
    public boolean setPublisherIdToNullByPublisherId(int publisherId) {
        String sql = "UPDATE book SET publisher_id = NULL WHERE publisher_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, publisherId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức đặt category_id của sách về NULL theo mã thể loại
    public boolean setCategoryIdToNullByCategoryId(int categoryId) {
        String sql = "UPDATE book SET category_id = NULL WHERE category_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
