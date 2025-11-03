package com.deliverytech.delivery.commons;

public enum TipoEndereco {
    RESIDENCIAL("Residencial"),
    COMERCIAL("Comercial"),
    MAE("Mãe"),
    TRABALHO("Trabalho"),
    PAI("Pai"),
    VO("Vô"),
    NAMORADA("Namorada"),
    OUTRO("Outro");

    private final String descricao;

    TipoEndereco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
