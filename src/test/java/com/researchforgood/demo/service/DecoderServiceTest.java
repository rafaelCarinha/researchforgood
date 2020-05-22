package com.researchforgood.demo.service;

import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;
import com.researchforgood.demo.service.impl.DecoderServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DecoderServiceTest {

    @Autowired
    DecoderServiceImpl decoderService;

    private static final byte[][] imageBytes = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,1}};
    private static final byte[][] imageBytes2 = new byte[][] {{0,0,0,1}, {1,1,1,1}, {1,0,0,0}};
    private static final byte[][] imageBytesOnlyZeros = new byte[][] {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
    private static final byte[][] imageBytesOnlyOnes = new byte[][] {{1,1,1,1}, {1,1,1,1}, {1,1,1,1}};

    @Test
    public void decodeImageTest_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytes);

        Assert.assertEquals("3 4 3 6 2 1", decoderService.decodeImage(image).getHeader());

    }

    @Test
    public void decodeImageTest2_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytes2);

        Assert.assertEquals("3 4 3 6 3", decoderService.decodeImage(image).getHeader());

    }

    @Test
    public void decodeImageOnlyZeros_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytesOnlyZeros);

        Assert.assertEquals("3 4 12", decoderService.decodeImage(image).getHeader());

    }

    @Test
    public void decodeImageOnlyOnes_success() throws Exception{

        Image image = new Image();
        image.setBytes(imageBytesOnlyOnes);

        Assert.assertEquals("3 4 12", decoderService.decodeImage(image).getHeader());

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
        image.setHeader("3 4 3 6 2 1");
        image.setN(3);
        image.setM(4);

        Assert.assertArrayEquals(imageBytes, decoderService.encodeImage(image).getBytes());
    }

    @Test
    public void uploadImage_Decode_and_Encode_Back() throws IOException, DecoderException {
        BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir")+File.separator+"image.jpeg"));
        byte[][] pixels = new byte[image.getWidth()][];

        for (int x = 0; x < image.getWidth(); x++) {
            pixels[x] = new byte[image.getHeight()];

            for (int y = 0; y < image.getHeight(); y++) {
                pixels[x][y] = (byte) (image.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
            }
        }
        Image imageObj = new Image();
        imageObj.setBytes(pixels);

        Image decodedImage = decoderService.decodeImage(imageObj);

        Image imageObj2 = new Image();
        imageObj2.setN(decodedImage.getN());
        imageObj2.setM(decodedImage.getM());
        imageObj2.setHeader(decodedImage.getHeader());

        byte[][] encodedImage = decoderService.encodeImage(imageObj2).getBytes();

        Assert.assertArrayEquals(pixels , encodedImage);

    }

}
