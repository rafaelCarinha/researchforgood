package com.researchforgood.demo.service.impl;

import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;
import com.researchforgood.demo.service.DecoderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DecoderServiceImplTest {

    @Autowired
    DecoderService decoderService;

    private byte[][] imageBytes = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,1}};

    @Test
    public void decodeImageTest_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytes);

        Assert.assertEquals("343621", decoderService.decodeImage(image));

    }

    @Test(expected = DecoderException.class)
    public void decodeImageTest_throwsException() throws Exception{

        Image image = new Image();
        image.setBytes(new byte[0][0]);
        decoderService.decodeImage(image);

    }

    @Test
    public void encodeImageTest_success() {
        Image image = new Image();
        image.setHeader("343621");

        Assert.assertArrayEquals(imageBytes, decoderService.encodeImage(image));
    }
}
