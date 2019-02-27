package com.juner.mvp.bean;

import java.util.List;

public class PlayInfoList {


    List<PlayInfo> PlayInfo;

    public List<PlayInfo> getPlayInfo() {
        return PlayInfo;
    }

    public void setPlayInfo(List<PlayInfo> playInfo) {
        PlayInfo = playInfo;
    }

    public class PlayInfo {

        String PlayURL;//
        String Duration;//时长
        String Format;//

        public String getFormat() {
            return Format;
        }

        public void setFormat(String format) {
            Format = format;
        }

        public String getPlayURL() {
            return PlayURL;
        }

        public void setPlayURL(String playURL) {
            PlayURL = playURL;
        }

        public String getDuration() {
            return Duration;
        }

        public void setDuration(String duration) {
            Duration = duration;
        }
    }
}
