//package gov.iti.jets.server.model.dao.interfaces;
//
//
//import shared.dto.FileTransfer;
//
//import java.util.List;
//
//public interface FileTransferDAOInt {
//    boolean insertFile(FileTransfer fileTransfer);
//    FileTransfer getFileById(int fileId);
//    List<FileTransfer> getAllFiles();
//    boolean updateFile(FileTransfer fileTransfer);
//    boolean deleteFile(int fileId);
//    List<FileTransfer> getFilesBetweenUsersByType(int userAId, int userBId, String fileType);
//}

package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.FileTransfer;

import java.util.List;
import java.util.UUID;

public interface FileTransferDAOInt {
    boolean insertFile(FileTransfer fileTransfer);
    FileTransfer getFileById(UUID fileId);
    List<FileTransfer> getAllFiles();
    boolean deleteFile(UUID fileId);

    // For individual messages
    List<FileTransfer> getFilesBetweenUsers(int userAId, int userBId);

    // For group messages
    List<FileTransfer> getFilesByGroupId(int groupId);
    List<FileTransfer> getFilesByGroupIdAndType(int groupId, String fileType);

    // Common queries
    List<FileTransfer> getFilesBySender(int senderId);
    List<FileTransfer> getRecentFilesForUser(int userId, int maxResults);

    String getFileName(UUID fileID);

    FileTransfer getLastFileBetweenUsers(int userAId, int userBId) ;
    FileTransfer getLastFileByGroupId(int groupId);

}