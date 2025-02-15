package shared.dto;


import java.io.Serializable;

public class Invitation implements Serializable {

    private Integer invitationId;
    private Integer senderId;
    private Integer receiverId;
    private Status status;

    public enum Status {
        //   PENDING, ACCEPTED, REJECTED, BLOCKED
        pending, accepted, rejected, blocked
    }

    public Invitation() {
    }

    public Invitation(Integer invitationId, Integer senderId, Integer receiverId, Status status) {
        this.invitationId = invitationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public Integer getInvitationId() {
        return invitationId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setInvitationId(Integer invitationId) {
        this.invitationId = invitationId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "invitationId=" + invitationId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", status='" + status + '\'' +
                '}';
    }


}
