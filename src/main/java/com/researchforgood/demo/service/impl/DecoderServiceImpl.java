package com.researchforgood.demo.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;
import com.researchforgood.demo.service.DecoderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@Slf4j
public class DecoderServiceImpl implements DecoderService {

    final Logger logger = LoggerFactory.getLogger(DecoderServiceImpl.class);

    @Override
    public Image decodeImage(Image image) throws DecoderException {
        Preconditions.checkNotNull(image, "Image cannot be null");
        Preconditions.checkNotNull(image.getBytes(), "Image bytes cannot be null");

        byte[][] imageBytes = image.getBytes();

        int N = imageBytes.length;
        int M;

        if (imageBytes.length > 0){
            M = imageBytes[0].length;
        } else{
            logger.error("Not a valid project binary file!");
            throw new DecoderException("Not a valid project binary file!");
        }

        List<Integer> integerList = new ArrayList<>();
        StringBuilder output = new StringBuilder(N+" "+M+" ");

        for (byte[] b : imageBytes) {
            for (byte b1 : b) {
                integerList.add((int) b1);
            }
        }

        Integer previous = null;
        AtomicInteger count = new AtomicInteger();
        Iterator<Integer> itr = integerList.iterator();
        while (itr.hasNext()) {
            int b1 = itr.next();

            if (previous == null){
                previous = b1;
            } else{
                if (previous != b1) {
                    previous = b1;
                    output.append(count.get()).append(" ");
                    count.set(0);
                }
            }
            count.incrementAndGet();

            if (!itr.hasNext()){
                output.append(count.get());
            }
        }

        logger.info("Image successfully decoded!");
        return Image.builder()
                .header(output.toString())
                .bytes(imageBytes)
                .N(N)
                .M(M)
                .build();
    }

    @Override
    public Image encodeImage(Image image){
        Preconditions.checkNotNull(image, "Image cannot be null");
        Preconditions.checkNotNull(image.getHeader(), "Image header cannot be null");

        String input = image.getHeader();

        int[] fullArray = Stream.of(input.split(" ")).mapToInt(Integer::parseInt).toArray();

        int N = fullArray[0];
        int M = fullArray[1];
        int[] numbersIntArray = Stream.of(input.substring((N+" "+M+" ").length()).split(" ")).mapToInt(Integer::parseInt).toArray();

        byte[][] outputImage = new byte[N][M];

        String filler = "";

        for (int i = 0; i < numbersIntArray.length; i++) {

            int split = numbersIntArray[i];
            if (i % 2 == 0) {
                //Adds zeros
                filler = Strings.padEnd(filler, filler.length()+split, '0');
            } else {
                //Adds ones
                filler = Strings.padEnd(filler, filler.length()+split, '1');

            }

        }

        int[] multi = Stream.of(filler.split("")).mapToInt(Integer::parseInt).toArray();

        byte[] dimension = new byte[M];
        int leftCounter = 0;
        int rightCounter = 0;
        for (int value : multi) {

            dimension[rightCounter] = (byte) value;
            rightCounter++;

            if (rightCounter != 0 & rightCounter % M == 0) {
                outputImage[leftCounter] = dimension;
                dimension = new byte[M];
                leftCounter++;
                rightCounter = 0;
            }
        }

        logger.info("Image successfully encoded!");

        return Image.builder()
                .bytes(outputImage)
                .N(N)
                .M(M)
                .header(image.getHeader())
                .build();

    }
}
