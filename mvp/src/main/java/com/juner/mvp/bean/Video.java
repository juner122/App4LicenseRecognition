package com.juner.mvp.bean;



public class Video {


    PlayInfoList PlayInfoList;
    VideoBase VideoBase;

    public com.juner.mvp.bean.PlayInfoList getPlayInfoList() {
        return PlayInfoList;
    }

    public void setPlayInfoList(com.juner.mvp.bean.PlayInfoList playInfoList) {
        PlayInfoList = playInfoList;
    }

    public VideoBase getVideoBase() {
        return VideoBase;
    }

    public void setVideoBase(VideoBase videoBase) {
        this.VideoBase = videoBase;
    }

    public class VideoBase{

        String Title;
        String CoverURL;//封面
        String Duration;//时长

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getCoverURL() {
            return CoverURL;
        }

        public void setCoverURL(String coverURL) {
            CoverURL = coverURL;
        }

        public String getDuration() {
            return Duration;
        }

        public void setDuration(String duration) {
            Duration = duration;
        }
    }
}
