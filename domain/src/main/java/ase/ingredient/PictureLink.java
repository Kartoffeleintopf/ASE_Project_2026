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