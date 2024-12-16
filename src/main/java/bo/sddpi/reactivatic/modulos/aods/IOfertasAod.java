package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Ofertas;

@Mapper
public interface IOfertasAod {

    @Select("SELECT * FROM ofertas WHERE idempresa=#{empresa}")
    List<Ofertas> ofertas(Long empresa);

    @Select("SELECT * FROM ofertas WHERE idempresa=#{empresa} AND idoferta=#{id}")
    Ofertas dato(Long empresa, Long id);

    @Select("SELECT * FROM ofertas WHERE oferta=#{oferta}")
    Long verificaroferta(String oferta);

    @Insert("INSERT INTO ofertas (idempresa, tipooferta, oferta, estado) values (#{idempresa}, #{tipooferta}, #{oferta}, true)")
    void adicionar(Ofertas oferta);

    @Update("UPDATE ofertas SET tipooferta=#{tipooferta}, oferta=#{oferta}")
    void modificar(Ofertas oferta);

    @Delete("DELETE FROM ofertas WHERE idoferta=#{idoferta}")
    void eliminar(Long idoferta);
}
