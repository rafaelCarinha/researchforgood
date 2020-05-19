package com.researchforgood.demo.service;

import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;

public interface DecoderService {

    String decodeImage(Image image) throws DecoderException;

    byte[][] encodeImage(Image  input);
}
