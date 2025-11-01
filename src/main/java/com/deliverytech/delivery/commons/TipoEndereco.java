package com.deliverytech.delivery.commons;

public enum TipoEndereco {
    RESIDENCIAL("Residencial"),
    COMERCIAL("Comercial");

    private final String descricao;

    TipoEndereco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
