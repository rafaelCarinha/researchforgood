package com.researchforgood.demo.controller;

import com.researchforgood.demo.Image;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostControllerIntegrationTest {

    @Autowired
    DecoderController decoderController;

    private static final byte[][] imageBytes = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,1}};

    @Test
    public void testDecoder() throws Exception {
        Image image = new Image();
        image.setBytes(imageBytes);

        Assert.assertEquals("3 4 3 6 2 1", decoderController.decode(image).getHeader());
        decoderController.decode(image);
    }

    @Test
    public void testEncoder() {
        Image image = new Image();
        image.setHeader("3 4 3 6 2 1");
        image.setN(3);
        image.setM(4);

        Assert.assertArrayEquals(imageBytes, decoderController.encode(image).getBytes());
    }
}
