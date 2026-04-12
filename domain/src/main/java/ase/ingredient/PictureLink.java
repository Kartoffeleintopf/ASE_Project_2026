package ase.ingredient;

import jakarta.persistence.Embeddable;

@Embeddable
public class PictureLink {
    private String url;

    protected PictureLink() {}

    public PictureLink(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getExtension() {
        int lastDot = url.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return url.substring(lastDot);
    }

    public String getFileName() {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash == -1) {
            return url;
        }
        return url.substring(lastSlash + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PictureLink)) return false;
        PictureLink other = (PictureLink) o;
        return this.url.equals(other.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}