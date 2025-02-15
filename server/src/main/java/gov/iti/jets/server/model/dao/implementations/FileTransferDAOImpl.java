package gov.iti.jets.server.model.dao.implementations;

import gov.iti.jets.server.model.dao.interfaces.FileTransferDAOInt;
import shared.dto.FileTransfer;
import shared.utils.DB_UtilityClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileTransferDAOImpl implements FileTransferDAOInt {

    @Override
    public boolean insertFile(FileTransfer fileTransfer) {
        String sql = "INSERT INTO file_transfer (file_id, sender_id, receiver_id, group_id, " +
                "file_name, file_type, file_path, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fileTransfer.getFileId().toString());
            ps.setInt(2, fileTransfer.getSenderId());

            if (fileTransfer.getReceiverId() != null) {
                ps.setInt(3, fileTransfer.getReceiverId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (fileTransfer.getGroupId() != null) {
                ps.setInt(4, fileTransfer.getGroupId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, fileTransfer.getFileName());
            ps.setString(6, fileTransfer.getFileType());
            ps.setString(7, fileTransfer.getFilePath());
            ps.setTimestamp(8, fileTransfer.getTimestamp());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public String getFileName(UUID fileID) {
        String sql = "SELECT file_name FROM file_transfer WHERE file_id = ?";
        String fileName = null;

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fileID.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fileName = rs.getString("file_name");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return fileName;
    }

    @Override
    public FileTransfer getLastFileBetweenUsers(int userAId, int userBId) {
        String sql = "SELECT * FROM file_transfer ft WHERE ft.timestamp = ( SELECT MAX(timestamp) FROM file_transfer WHERE sender_id = ? AND receiver_id = ? OR (sender_id = ? AND receiver_id = ?))";

//        String sql = "SELECT * FROM file_transfer " +
//                "WHERE ((sender_id = ? AND receiver_id = ?) OR " +
//                "(sender_id = ? AND receiver_id = ?)) ";

        FileTransfer fileTransfer = new FileTransfer();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userBId);
            ps.setInt(2, userAId);
            ps.setInt(3, userAId);
            ps.setInt(4, userBId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fileTransfer = mapResultSetToFileTransfer(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return fileTransfer;
    }


    @Override
    public FileTransfer getLastFileByGroupId(int groupId) {
        String sql = "SELECT * FROM file_transfer WHERE group_id = ? ORDER BY timestamp DESC LIMIT 1";
        FileTransfer files = new FileTransfer();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files = mapResultSetToFileTransfer(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }


    @Override
    public FileTransfer getFileById(UUID fileId) {
        String sql = "SELECT * FROM file_transfer WHERE file_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fileId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFileTransfer(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    @Override
    public List<FileTransfer> getAllFiles() {
        String sql = "SELECT * FROM file_transfer";
        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                files.add(mapResultSetToFileTransfer(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    @Override
    public boolean deleteFile(UUID fileId) {
        String sql = "DELETE FROM file_transfer WHERE file_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fileId.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    @Override
//    public List<FileTransfer> getFilesBetweenUsersByType(int userAId, int userBId, String fileType) {
//        String sql = "SELECT * FROM file_transfer " +
//                "WHERE ((sender_id = ? AND receiver_id = ?) OR " +
//                "(sender_id = ? AND receiver_id = ?)) " +
//                "AND file_type = ?";
//
//        List<FileTransfer> files = new ArrayList<>();
//
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//            ps.setInt(1, userAId);
//            ps.setInt(2, userBId);
//            ps.setInt(3, userBId);
//            ps.setInt(4, userAId);
//            ps.setString(5, fileType);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    files.add(mapResultSetToFileTransfer(rs));
//                }
//            }
//        } catch (SQLException e) {
//            handleSQLException(e);
//        }
//        return files;
//    }
    public List<FileTransfer> getFilesBetweenUsers(int userAId, int userBId) {
        String sql = "SELECT * FROM file_transfer " +
                "WHERE ((sender_id = ? AND receiver_id = ?) OR " +
                "(sender_id = ? AND receiver_id = ?)) ";

        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userAId);
            ps.setInt(2, userBId);
            ps.setInt(3, userBId);
            ps.setInt(4, userAId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileTransfer(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    @Override
    public List<FileTransfer> getFilesByGroupId(int groupId) {
        String sql = "SELECT * FROM file_transfer WHERE group_id = ?";
        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileTransfer(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    @Override
    public List<FileTransfer> getFilesByGroupIdAndType(int groupId, String fileType) {
        String sql = "SELECT * FROM file_transfer WHERE group_id = ? AND file_type = ?";
        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            ps.setString(2, fileType);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileTransfer(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    @Override
    public List<FileTransfer> getFilesBySender(int senderId) {
        String sql = "SELECT * FROM file_transfer WHERE sender_id = ?";
        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, senderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileTransfer(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    @Override
    public List<FileTransfer> getRecentFilesForUser(int userId, int maxResults) {
        String sql = "SELECT * FROM file_transfer " +
                "WHERE (receiver_id = ? OR group_id IN " +
                "(SELECT group_id FROM group_members WHERE user_id = ?)) " +
                "ORDER BY timestamp DESC LIMIT ?";

        List<FileTransfer> files = new ArrayList<>();

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, maxResults);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileTransfer(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return files;
    }

    private FileTransfer mapResultSetToFileTransfer(ResultSet rs) throws SQLException {
        return new FileTransfer(
                UUID.fromString(rs.getString("file_id")),
                rs.getInt("sender_id"),
                rs.getObject("receiver_id", Integer.class),
                rs.getObject("group_id", Integer.class),
                rs.getString("file_name"),
                rs.getString("file_type"),
                rs.getString("file_path"),
                rs.getTimestamp("timestamp")
        );
    }

    private void handleSQLException(SQLException e) {
        // Implement proper error logging/handling
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}
//package gov.iti.jets.server.model.dao.implementations;
//
//
//import gov.iti.jets.server.model.dao.interfaces.FileTransferDAOInt;
//import shared.dto.FileTransfer;
//import shared.utils.DB_UtilityClass;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FileTransferDAOImpl implements FileTransferDAOInt {
//
//
//    public  boolean insertFile(FileTransfer fileTransfer) {
//
//        boolean result=false;
//        String query = "INSERT INTO file_transfer (sender_id, receiver_id, file_name, file_type, file_size, file_path, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query))
//        {
//
//            preparedStatement.setInt(1, fileTransfer.getSenderId());
//            preparedStatement.setInt(2, fileTransfer.getReceiverId());
//            preparedStatement.setString(3, fileTransfer.getFileName());
//            preparedStatement.setString(4, fileTransfer.getFileType());
//            preparedStatement.setLong(5, fileTransfer.getFileSize());
//            preparedStatement.setString(6, fileTransfer.getFilePath());
//            preparedStatement.setTimestamp(7, fileTransfer.getTimestamp());
//
//           result= preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    public FileTransfer getFileById(int fileId) {
//
//        FileTransfer fileTransfer=new FileTransfer();
//        String query = "SELECT * FROM file_transfer WHERE file_id = ?";
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, fileId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                fileTransfer= new FileTransfer(
//                        resultSet.getInt("file_id"),
//                        resultSet.getInt("sender_id"),
//                        resultSet.getInt("receiver_id"),
//                        resultSet.getString("file_name"),
//                        resultSet.getString("file_type"),
//                        resultSet.getLong("file_size"),
//                        resultSet.getString("file_path"),
//                        resultSet.getTimestamp("timestamp")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return fileTransfer;
//    }
//
//
//    public List<FileTransfer> getAllFiles() {
//
//        FileTransfer fileTransfer=new FileTransfer();
//        List<FileTransfer> fileTransfers = new ArrayList<>();
//        String query = "SELECT * FROM file_transfer";
//        try (Connection connection = DB_UtilityClass.getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            while (resultSet.next()) {
//                       fileTransfer = new FileTransfer(
//                        resultSet.getInt("file_id"),
//                        resultSet.getInt("sender_id"),
//                        resultSet.getInt("receiver_id"),
//                        resultSet.getString("file_name"),
//                        resultSet.getString("file_type"),
//                        resultSet.getLong("file_size"),
//                        resultSet.getString("file_path"),
//                        resultSet.getTimestamp("timestamp")
//                );
//                fileTransfers.add(fileTransfer);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return fileTransfers;
//    }
//
//
//    public boolean updateFile(FileTransfer fileTransfer) {
//        String query = "UPDATE file_transfer SET sender_id = ?, receiver_id = ?, file_name = ?, file_type = ?, file_size = ?, file_path = ?, timestamp = ? WHERE file_id = ?";
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, fileTransfer.getSenderId());
//            preparedStatement.setInt(2, fileTransfer.getReceiverId());
//            preparedStatement.setString(3, fileTransfer.getFileName());
//            preparedStatement.setString(4, fileTransfer.getFileType());
//            preparedStatement.setLong(5, fileTransfer.getFileSize());
//            preparedStatement.setString(6, fileTransfer.getFilePath());
//            preparedStatement.setTimestamp(7, fileTransfer.getTimestamp());
//            preparedStatement.setInt(8, fileTransfer.getFileId());
//
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    public boolean deleteFile(int fileId) {
//        boolean result=false;
//        String query = "DELETE FROM file_transfer WHERE file_id = ?";
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query))
//        {
//            preparedStatement.setInt(1, fileId);
//            result= preparedStatement.executeUpdate() > 0;
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    //  retrieve files sent or received by a user filtered by file type
//    public List<FileTransfer> getFilesBetweenUsersByType(int userAId, int userBId, String fileType) {
//
//        List<FileTransfer> files = new ArrayList<>();
//        String query = "SELECT * FROM file_transfer " +
//                "WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
//                "AND file_type = ?";
//
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, userAId); // sender_id = userAId
//            statement.setInt(2, userBId); // receiver_id = userBId
//            statement.setInt(3, userBId); // sender_id = userBId (reverse direction)
//            statement.setInt(4, userAId); // receiver_id = userAId (reverse direction)
//            statement.setString(5, fileType); // file_type
//
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    FileTransfer file = new FileTransfer();
//                    file.setFileId(resultSet.getInt("file_id"));
//                    file.setSenderId(resultSet.getInt("sender_id"));
//                    file.setReceiverId(resultSet.getInt("receiver_id"));
//                    file.setFileName(resultSet.getString("file_name"));
//                    file.setFileType(resultSet.getString("file_type"));
//                    file.setFileSize(resultSet.getLong("file_size"));
//                    file.setFilePath(resultSet.getString("file_path"));
//                    file.setTimestamp(resultSet.getTimestamp("timestamp"));
//
//                    files.add(file);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return files;
//    }
//
//
//
//}
