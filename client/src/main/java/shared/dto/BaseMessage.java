package shared.dto;

import java.sql.Timestamp;

public abstract class BaseMessage {


    public abstract String getSenderName2();

    public abstract int getSenderID2();

    public abstract Timestamp getTimeStamp2();

    public abstract String getMessageContent2();
}
