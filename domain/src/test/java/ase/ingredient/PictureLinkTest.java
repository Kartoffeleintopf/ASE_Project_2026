package ase.ingredient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictureLinkTest {
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
}