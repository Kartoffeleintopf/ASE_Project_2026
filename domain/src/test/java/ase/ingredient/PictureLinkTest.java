package ase.ingredient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictureLinkTest {

    @Test
    void getUrl() {
        PictureLink pictureLink = new PictureLink("link");
        assertEquals("link", pictureLink.getUrl());
    }

    @Test
    void pictureLinkEquality() {
        PictureLink a = new PictureLink("www.link.net");
        PictureLink b = new PictureLink("www.link.net");
        assertEquals(a, b);
    }

    @Test
    void pictureLinkInequality() {
        PictureLink a = new PictureLink("www.link1.net");
        PictureLink b = new PictureLink("www.link2.net");
        assertNotEquals(a, b);
    }

    @Test
    void hashCodeConsistentWithEquals() {
        PictureLink a = new PictureLink("link");
        PictureLink b = new PictureLink("link");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void getExtension() {
        PictureLink link = new PictureLink("images/tomato.png");
        assertEquals(".png", link.getExtension());
    }

    @Test
    void getExtensionNoExtension() {
        PictureLink link = new PictureLink("tomato");
        assertEquals("", link.getExtension());
    }

    @Test
    void getFileName() {
        PictureLink link = new PictureLink("images/tomato.png");
        assertEquals("tomato.png", link.getFileName());
    }

    @Test
    void getFileNameNoPath() {
        PictureLink link = new PictureLink("tomato.png");
        assertEquals("tomato.png", link.getFileName());
    }
}