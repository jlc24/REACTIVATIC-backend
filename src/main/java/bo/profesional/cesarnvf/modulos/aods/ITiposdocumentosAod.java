package bo.profesional.cesarnvf.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Tiposdocumentos;

@Mapper
public interface ITiposdocumentosAod {

    @Select("SELECT idtipodocumento, tipodocumento FROM tiposdocumentos where idtipodocumento=#{id} ")
    Tiposdocumentos dato(Long id);
}