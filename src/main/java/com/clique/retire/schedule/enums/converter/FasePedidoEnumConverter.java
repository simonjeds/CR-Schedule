package com.clique.retire.schedule.enums.converter;

import com.clique.retire.schedule.enums.FasePedidoEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FasePedidoEnumConverter implements AttributeConverter<FasePedidoEnum, String> {

  @Override
  public String convertToDatabaseColumn(FasePedidoEnum faseEnum) {
    return faseEnum.getChave();
  }

  @Override
  public FasePedidoEnum convertToEntityAttribute(String chave) {
    return FasePedidoEnum.buscarPorChave(chave);
  }

}
