package com.researchforgood.demo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    private int M;
    private int N;
    private byte[][] bytes;
    private String header;

}
