package com.researchforgood.demo.service;

import com.researchforgood.demo.Image;
import com.researchforgood.demo.exception.DecoderException;

public interface DecoderService {

    Image decodeImage(Image image) throws DecoderException;

    Image encodeImage(Image  input);
}
