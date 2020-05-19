package com.researchforgood.demo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private int M;
    private int N;
    private byte[][] bytes;
    private String header;

}
