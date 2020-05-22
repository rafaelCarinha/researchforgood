package com.researchforgood.demo.controller;

import com.researchforgood.demo.Image;
import com.researchforgood.demo.service.DecoderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/decoder")
public class DecoderController {

    final DecoderService decoderService;

    public DecoderController(DecoderService decoderService){
        this.decoderService = decoderService;
    }

    @PostMapping("/decode")
    Image decode(@RequestBody Image image) throws Exception{
        return decoderService.decodeImage(image);
    }


    @PostMapping("/encode")
    Image encode(@RequestBody Image image) {
        return decoderService.encodeImage(image);
    }

}
