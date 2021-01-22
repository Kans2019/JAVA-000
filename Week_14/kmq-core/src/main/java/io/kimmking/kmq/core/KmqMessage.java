package io.kimmking.kmq.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@AllArgsConstructor
@Data
public class KmqMessage<T> implements java.io.Serializable {

    private HashMap<String,Object> headers;

    private T body;

}
