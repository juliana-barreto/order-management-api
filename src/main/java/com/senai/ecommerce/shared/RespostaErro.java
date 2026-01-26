package com.senai.ecommerce.shared;

import java.time.LocalDateTime;

public record RespostaErro(
    LocalDateTime timestamp,
    Integer status,
    String erro,
    String mensagem,
    String path
) {

}
