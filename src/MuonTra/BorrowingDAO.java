package MuonTra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnect;

public class BorrowingDAO {

    // Lấy danh sách tất cả phiếu mượn
    public List<Borrowing> getAllBorrowings() {
        List<Borrowing> list = new ArrayList<>(); 
        String sql = "SELECT * FROM borrowing";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Borrowing b = new Borrowing(
                    rs.getInt("borrowing_id"),
                    rs.getInt("reader_id"),
                    rs.getInt("book_id"),
                    rs.getInt("staff_id"),
                    rs.getDate("borrow_date"),
                    rs.getDate("return_date"),
                    rs.getString("status")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm phiếu mượn mới
    public boolean insertBorrowing(Borrowing borrowing) {
        String sql = "INSERT INTO borrowing (reader_id, book_id, staff_id, borrow_date, return_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowing.getReaderId());
            stmt.setInt(2, borrowing.getBookId());
            stmt.setInt(3, borrowing.getStaffId());
            stmt.setDate(4, borrowing.getBorrowDate());
            stmt.setDate(5, borrowing.getReturnDate());
            stmt.setString(6, borrowing.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin phiếu mượn
    public boolean updateBorrowing(Borrowing borrowing) {
        String sql = "UPDATE borrowing SET reader_id = ?, book_id = ?, staff_id = ?, borrow_date = ?, return_date = ?, status = ? WHERE borrowing_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowing.getReaderId());
            stmt.setInt(2, borrowing.getBookId());
            stmt.setInt(3, borrowing.getStaffId());
            stmt.setDate(4, borrowing.getBorrowDate());
            stmt.setDate(5, borrowing.getReturnDate());
            stmt.setString(6, borrowing.getStatus());
            stmt.setInt(7, borrowing.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa phiếu mượn
    public boolean deleteBorrowing(int borrowingId) {
        String sql = "DELETE FROM borrowing WHERE borrowing_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowingId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy phiếu mượn theo ID
    public Borrowing getBorrowingById(int borrowingId) {
        String sql = "SELECT * FROM borrowing WHERE borrowing_id = ?";
        Borrowing borrowing = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    borrowing = new Borrowing(
                        rs.getInt("borrowing_id"),
                        rs.getInt("reader_id"),
                        rs.getInt("book_id"),
                        rs.getInt("staff_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date"),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowing;
    }

    // Phương thức xoá phiếu mượn theo book_id
    public boolean deleteBorrowingsByBookId(int bookId) {
        String sql = "DELETE FROM borrowing WHERE book_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate(); // Use executeUpdate for DELETE, it returns row count
            return true; // Assuming success if no exception
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức xoá phiếu mượn theo reader_id
    public boolean deleteBorrowingsByReaderId(int readerId) {
        String sql = "DELETE FROM borrowing WHERE reader_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức xoá phiếu mượn theo staff_id
    public boolean deleteBorrowingsByStaffId(int staffId) {
        String sql = "DELETE FROM borrowing WHERE staff_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
