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

    private static final byte[][] imageBytes = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,1}};
    private static final byte[][] imageBytes2 = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,0}};
    private static final byte[][] imageBytesOnlyZeros = new byte[][] {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
    private static final byte[][] imageBytesOnlyOnes = new byte[][] {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}};

    @Test
    public void decodeImageTest_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytes);

        Assert.assertEquals("343621", decoderService.decodeImage(image));

    }

    @Test
    public void decodeImageTest2_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytes2);

        Assert.assertEquals("34363", decoderService.decodeImage(image));

    }

    @Test
    public void decodeImageOnlyZeros_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytesOnlyZeros);

        Assert.assertEquals("3412", decoderService.decodeImage(image));

    }

    @Test
    public void decodeImageOnlyOnes_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytesOnlyOnes);

        Assert.assertEquals("3412", decoderService.decodeImage(image));

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
