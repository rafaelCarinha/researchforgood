package com.researchforgood.demo.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;
import com.researchforgood.demo.service.DecoderService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.HelloWorld;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DecoderServiceImpl implements DecoderService {

    Logger logger = LoggerFactory.getLogger(DecoderService.class);

    public String decodeImage(Image image) throws DecoderException {
        Preconditions.checkNotNull(image, "Image cannot be null");
        Preconditions.checkNotNull(image.getBytes(), "Image bytes cannot be null");

        List<String> binaryList = Arrays
                .stream(image.getBytes())
                .map(Arrays::toString)
                .map(str -> str
                        .replaceAll(",", "")
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll("\\s", ""))
                .collect(Collectors.toList());

        String flatBinary = binaryList.stream().collect(Collectors.joining());

        Pattern pattern = Pattern.compile("0+|1+");
        Matcher matcher = pattern.matcher(flatBinary);

        int N = binaryList.size();

        int M = 0;
        if(!binaryList.isEmpty()){
            M = binaryList.get(0).length();
        } else {
            logger.error("Not a valid project binary file!");
            throw new DecoderException("Not a valid project binary file!");
        }

        StringBuilder output = new StringBuilder(N+""+M);

        while (matcher.find()) {
            output.append(matcher.group(0).length());
        }

        logger.info("Image successfully decoded!");
        return output.toString();
    }

    public byte[][] encodeImage(Image image){
        Preconditions.checkNotNull(image, "Image cannot be null");
        Preconditions.checkNotNull(image.getHeader(), "Image header cannot be null");

        String input = image.getHeader();
        String[] charSplit = input.substring(2).split("");
        int secondDimension = Integer.parseInt(input.substring(1,2));

        byte[][] outputImage = new byte[Integer.parseInt(input.substring(0,1))][secondDimension];

        String filler = new String();
        for (int i = 0; i < charSplit.length; i++) {

            int split = Integer.parseInt(charSplit[i]);
            if(i % 2 == 0){
                //Adds zeros
                filler = Strings.padEnd(filler, filler.length()+split, '0');
            }else{
                //Adds ones
                filler = Strings.padEnd(filler, filler.length()+split, '1');

            }

        }

        String[] multi = filler.split("");

        byte[] dimension = new byte[secondDimension];
        int leftCounter = 0;
        int rightCounter = 0;
        for (int i = 0; i < multi.length; i++){

            dimension[rightCounter] = (byte) Integer.parseInt(multi[i]);
            rightCounter++;

            if(rightCounter != 0 & rightCounter % 4 == 0){
                outputImage[leftCounter] = dimension;
                dimension = new byte[secondDimension];
                leftCounter++;
                rightCounter = 0;
            }
        }

        logger.info("Image successfully encoded!");
        return outputImage;

    }
}
