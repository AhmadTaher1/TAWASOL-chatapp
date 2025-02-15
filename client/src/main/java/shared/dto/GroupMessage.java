package shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class GroupMessage extends BaseMessage implements Serializable {
    private int messageId;
    private int senderId;
    private int groupId;
    private String messageContent;
    private String fontStyle;
    private String fontColor;
    private String textBackground;
    private int fontSize;
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderlined;
    private Timestamp timestamp;

    public GroupMessage() {
    }

    public GroupMessage(int messageId, int senderId, int groupId, String messageContent, String fontStyle, String fontColor, String textBackground, int fontSize, boolean isBold, boolean isItalic, boolean isUnderlined, Timestamp timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.groupId = groupId;
        this.messageContent = messageContent;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.textBackground = textBackground;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderlined = isUnderlined;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getTextBackground() {
        return textBackground;
    }

    public void setTextBackground(String textBackground) {
        this.textBackground = textBackground;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public boolean isUnderlined() {
        return isUnderlined;
    }

    public void setUnderlined(boolean underlined) {
        isUnderlined = underlined;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Message ID: %d, Sender ID: %d, Group ID: %d, Message Content: %s", messageId, senderId, groupId, messageContent);
    }

    @Override
    public String getSenderName2() {
        return "GM";
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
        return getMessageContent();
    }
}