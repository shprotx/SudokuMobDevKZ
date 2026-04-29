package ru.shprot.sudokumobdevkz.model.update;

public class UpdateInfo {

    private final String tagName;
    private final String releaseNotes;
    private final String downloadUrl;

    public UpdateInfo(String tagName, String releaseNotes, String downloadUrl) {
        this.tagName = tagName;
        this.releaseNotes = releaseNotes;
        this.downloadUrl = downloadUrl;
    }

    public String getTagName() {
        return tagName;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
