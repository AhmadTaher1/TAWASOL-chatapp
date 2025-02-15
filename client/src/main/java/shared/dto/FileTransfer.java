package shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class FileTransfer extends BaseMessage implements Serializable {

    private UUID fileId;
    private int senderId;
    private Integer receiverId;
    private Integer groupId;
    private String fileName;
    private String fileType;
    private String filePath;
    private Timestamp timestamp;

    public FileTransfer() {
    }

    public FileTransfer(UUID fileId, int senderId, Integer receiverId, Integer groupId, String fileName, String fileType, String filePath, Timestamp timestamp) {
        this.fileId = fileId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.groupId = groupId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    public FileTransfer(UUID fileId, int senderId, Integer receiverId, Integer groupId, String fileName, String fileType, byte[] fileData, Timestamp timestamp) {
        this.fileId = fileId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.groupId = groupId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.timestamp = timestamp;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isGroupTransfer() {
        return (groupId != null);
    }

    @Override
    public String getSenderName2() {
        return "File";
    }

    @Override
    public int getSenderID2() {
        return getSenderId();
    }

    @Override
    public Timestamp getTimeStamp2() {
        return getTimestamp();
    }

    @Override
    public String getMessageContent2() {
        return fileName;
    }
}
