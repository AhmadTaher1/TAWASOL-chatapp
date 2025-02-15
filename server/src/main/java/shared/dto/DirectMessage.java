package shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class DirectMessage extends BaseMessage implements Serializable {
    private Integer messageId;
    private Integer senderId;
    private Integer receiverId;
    private String messageContent;
    private String fontStyle;
    private String fontColor;
    private String textBackground;
    private Integer fontSize;
    private Boolean isBold;
    private Boolean isItalic;
    private Boolean isUnderlined;
    private Timestamp timestamp;

    public DirectMessage() {
    }

    public DirectMessage(Integer messageId, Integer senderId, Integer receiverId, String messageContent,
                         String fontStyle, String fontColor, String textBackground, Integer fontSize,
                         Boolean isBold, Boolean isItalic, Boolean isUnderlined, Timestamp timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

    public Integer getMessageId() {
        return messageId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getTextBackground() {
        return textBackground;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Boolean getBold() {
        return isBold;
    }

    public Boolean getItalic() {
        return isItalic;
    }

    public Boolean getUnderlined() {
        return isUnderlined;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setTextBackground(String textBackground) {
        this.textBackground = textBackground;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public void setItalic(Boolean italic) {
        isItalic = italic;
    }

    public void setUnderlined(Boolean underlined) {
        isUnderlined = underlined;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DirectMessage{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageContent='" + messageContent + '\'' +
                ", fontStyle='" + fontStyle + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", textBackground='" + textBackground + '\'' +
                ", fontSize=" + fontSize +
                ", isBold=" + isBold +
                ", isItalic=" + isItalic +
                ", isUnderlined=" + isUnderlined +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public String getSenderName2() {

        return "DM";
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

