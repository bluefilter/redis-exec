package io.redispro.redisexec.dto;

import lombok.Getter;

import java.io.Serializable;

public record MyObject(String message) implements Serializable {

}