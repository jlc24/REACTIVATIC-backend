package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Empresas;

@Mapper
public interface IEmpresasAod {

    //@Select("SELECT idempresa, empresa FROM")

    @Select("SELECT idempresa, empresa FROM empresas ORDER BY idempresa")
    List<Empresas> lista();
    
    @Select("SELECT * FROM empresas e " +
        "JOIN rubros r ON e.idrubro = r.idrubro " +
        "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
        "LEFT JOIN asociaciones a ON e.idasociacion = a.idasociacion " +
        "JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
        "LEFT JOIN personas p ON rep.idpersona = p.idpersona " +
        "WHERE CONCAT(COALESCE(a.asociacion, ''), ' ', e.empresa, ' ', r.rubro, ' ', m.municipio, ' ', p.primerNombre, ' ', p.primerApellido, ' ', p.segundoApellido, ' ', p.dip) " +
        "ILIKE '%' || #{buscar} || '%' " +
        "AND r.rubro ILIKE '%'||#{rubro}||'%' " +
        "ORDER BY e.idempresa DESC " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "representante", column = "idrepresentante", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "rubro", column = "idrubro", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
        @Result(property = "asociacion", column = "idasociacion", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IAsociacionesAod.dato"))
    })
    List<Empresas> datos(String buscar, String rubro, Integer pagina, Integer cantidad);

    @Select("SELECT count(idempresa) FROM empresas e " +
        "JOIN rubros r ON e.idrubro = r.idrubro " +
        "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
        "LEFT JOIN asociaciones a ON e.idasociacion = a.idasociacion " +
        "JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
        "LEFT JOIN personas p ON rep.idpersona = p.idpersona " +
        "WHERE CONCAT(COALESCE(a.asociacion, ''), ' ', e.empresa, ' ', r.rubro, ' ', m.municipio, ' ', p.primerNombre, ' ', p.primerApellido, ' ', p.segundoApellido, ' ', p.dip) " +
        "ILIKE '%' || #{buscar} || '%' "+
        "AND r.rubro ILIKE '%'||#{rubro}||'%' ")
    Integer cantidad(String buscar, String rubro);

    @Select("SELECT * FROM empresas where idempresa=#{id} ")
    @Results({
        @Result(property = "rubro", column = "idrubro", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
        @Result(property = "asociacion", column = "idasociacion", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IAsociacionesAod.dato")),
        @Result(property = "representante", column = "idrepresentante", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato"))
    })
    Empresas dato(Long id);

    @Insert("insert into empresas(idrubro, idmunicipio, idrepresentante, idasociacion, empresa, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, nform, registrosenasag, latitud, longitud, descripcion, nit, bancamovil, fechaapertura, servicios, capacidad, unidadmedida, motivo, otromotivo, familiar, involucrados, otrosinvolucrados, trabajadores, participacion, capacitacion, zona, referencia, transporte, idusuario, fechareg, razonsocial, estado) values (#{idrubro}, #{idmunicipio}, #{idrepresentante}, #{idasociacion}, #{empresa}, #{tipo}, #{direccion}, #{telefono}, #{celular}, #{correo}, #{facebook}, #{twitter}, #{instagram}, #{paginaweb}, #{nform}, #{registrosenasag}, #{latitud}, #{longitud}, #{descripcion}, #{nit}, #{bancamovil}, #{fechaapertura}, #{servicios}, #{capacidad}, #{unidadmedida}, #{motivo}, #{otromotivo}, #{familiar}, #{involucrados}, #{otrosinvolucrados}, #{trabajadores}, #{participacion}, #{capacitacion}, #{zona}, #{referencia}, #{transporte}, #{idusuario}, #{fechareg}, #{razonsocial}, true)")
    void adicionar(Empresas dato);

    @Update("update empresas set idrubro=#{idrubro}, idmunicipio=#{idmunicipio}, idrepresentante=#{idrepresentante}, idasociacion=#{idasociacion}, empresa=#{empresa}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo}, facebook=#{facebook}, twitter=#{twitter}, instagram=#{instagram}, paginaweb=#{paginaweb}, nform=#{nform}, registrosenasag=#{registrosenasag}, latitud=#{latitud}, longitud=#{longitud}, descripcion=#{descripcion}, nit=#{nit}, bancamovil=#{bancamovil}, fechaapertura=#{fechaapertura}, servicios=#{servicios}, capacidad=#{capacidad}, unidadmedida=#{unidadmedida}, motivo=#{motivo}, otromotivo=#{otromotivo}, familiar=#{familiar}, involucrados=#{involucrados}, otrosinvolucrados=#{otrosinvolucrados}, trabajadores=#{trabajadores}, participacion=#{participacion}, capacitacion=#{capacitacion}, zona=#{zona}, referencia=#{referencia}, transporte=#{transporte}, fechareg=#{fechareg}, razonsocial=#{razonsocial} where idempresa=#{idempresa} ")
    void modificar(Empresas dato);

    @Update("UPDATE empresas SET estado=#{estado} WHERE idempresa=#{idempresa}")
    void cambiarestado(Empresas empresa);

    @Select("SELECT e.idempresa " +
            "FROM empresas e " +
            "JOIN representantes r ON r.idrepresentante=e.idrepresentante " +
            "JOIN personas p ON p.idpersona=r.idpersona " +
            "JOIN usuarios u ON u.idpersona=p.idpersona " +
            "WHERE u.idusuario=#{idusuario}")
    Long idempresa(Long idusuario);

    @Delete("delete from empresas where idempresa=#{id}")
    void eliminar(Long id);

    @Select("SELECT idempresa, idrubro, idrubro as ifore2, idmunicipio, idmunicipio as ifore3, idrepresentante, idrepresentante as ifore1, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas join rubros using(idrubro) join join municipios using(idmunicipio) WHERE concat(empresa,' ',rubro,' ',municipio) ilike '%'||#{buscar}||'%' ")
    @Results({
        @Result(property = "representante", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "rubro", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato")),
        @Result(property = "municipio", column = "ifore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato"))
    })
    List<Empresas> datosrepo(String buscar);

    @Select("SELECT idempresa, idrubro, idmunicipio, idrepresentante, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas where idproducto=#{id} ")
    Empresas datoproducto(Long id);

    @Select("SELECT e.* FROM empresas e join representantes r using(idrepresentante) join personas p using(idpersona) join usuarios u using(idpersona) where u.idusuario=#{id} ")
    @Results({
        @Result(property = "rubro", column = "idrubro", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
        @Result(property = "asociacion", column = "idasociacion", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IAsociacionesAod.dato")),
    })
    Empresas perfilempresa(Long id);

    @Select("SELECT COUNT(e.idempresa) " +
        "FROM empresas e " +
        "JOIN representantes r ON r.idrepresentante=e.idrepresentante " +
        "WHERE r.idrepresentante=#{idrepresentante}")
    Integer total(Long idrepresentante);

    @Select("SELECT idempresa FROM empresas WHERE idrepresentante=#{idrepresentante}")
    Long empresa(Long idrepresentante);

    @SelectProvider(type = MyMapper.class, method = "construirConsultaDinamica")
    @Results({
        @Result(property = "representante", column = "idrepresentante", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "rubro", column = "idrubro", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
        @Result(property = "asociacion", column = "idasociacion", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IAsociacionesAod.dato"))
    })
    List<Empresas> obtenerEmpresasDinamico(Map<String, Object> parametros);

}




