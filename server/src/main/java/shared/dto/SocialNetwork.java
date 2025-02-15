package shared.dto;

import java.io.Serializable;

public class SocialNetwork implements Serializable {

    private int socialId;
    private int userId;
    private Platform platform;
    private String accessToken;

    public enum Platform {
        facebook,
        twitter,
        linkedin
    }

    public SocialNetwork(int socialId, int userId, Platform platform, String accessToken) {
        this.socialId = socialId;
        this.userId = userId;
        this.platform = platform;
        this.accessToken = accessToken;
    }

    public int getSocialId() {
        return socialId;
    }

    public void setSocialId(int socialId) {
        this.socialId = socialId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
