package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Empresas;

@Mapper
public interface IEmpresasAod {

    @Select("SELECT idempresa, idsubrubro, idsubrubro as ifore2, idlocalidad, idlocalidad as ifore3, idrepresentante, idrepresentante as ifore1, idasociacion, idasociacion as ifore4, empresa, empresas.descripcion, tipo, empresas.direccion, empresas.telefono, empresas.celular, empresas.correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas join subrubros using(idsubrubro) join rubros using(idrubro) join localidades using(idlocalidad) join municipios using(idmunicipio) join asociaciones using(idasociacion) WHERE concat(asociacion,' ',empresa,' ',subrubro,' ',rubro,' ',localidad,' ',municipio) ilike '%'||#{buscar}||'%' ORDER BY empresa LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "representante", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "subrubro", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ISubrubrosAod.dato")),
        @Result(property = "localidad", column = "ifore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod.dato")),
        @Result(property = "asociacion", column = "ifore4", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IAsociacionesAod.dato"))
    })
    List<Empresas> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idempresa) FROM empresas join subrubros using(idsubrubro) join rubros using(idrubro) join localidades using(idlocalidad) join municipios using(idmunicipio) join asociaciones using(idasociacion) where concat(asociacion,' ',empresa,' ',subrubro,' ',rubro,' ',localidad,' ',municipio) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idempresa, idsubrubro, idlocalidad, idlocalidad as idfore1, idrepresentante, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas where idempresa=#{id} ")
    @Results({
        @Result(property = "localidad", column = "idfore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod.dato"))
    })
    Empresas dato(Long id);

    @Insert("insert into empresas(idsubrubro, idlocalidad, idrepresentante, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud, nit, bancamovil, fechaapertura, servicios, capacidad, unidadmedida, razon, otrarazon, familiar, involucrados, otroinvolucrado, trabajadores, participacion, capacitacion, zona, referencia, transporte, idusuario) values (#{idsubrubro}, #{idlocalidad}, #{idrepresentante}, #{idasociacion}, #{empresa}, #{descripcion}, #{tipo}, #{direccion}, #{telefono}, #{celular}, #{correo}, #{facebook}, #{twitter}, #{instagram}, #{paginaweb}, #{otro}, #{registrosenasag}, #{latitud}, #{longitud}, #{nit}, #{bancamovil}, #{fechaapertura}, #{servicios}, #{capacidad}, #{unidadmedida}, #{razon}, #{otrarazon}, #{familiar}, #{involucrados}, #{otroinvolucrado}, #{trabajadores}, #{participacion}, #{capacitacion}, #{zona}, #{referencia}, #{transporte}, #{idusuario})")
    void adicionar(Empresas dato);

    @Update("update empresas set idsubrubro=#{idsubrubro}, idlocalidad=#{idlocalidad}, idrepresentante=#{idrepresentante}, idasociacion=#{idasociacion}, empresa=#{empresa}, descripcion=#{descripcion}, tipo=#{tipo}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo}, facebook=#{facebook}, twitter=#{twitter}, instagram=#{instagram}, paginaweb=#{paginaweb}, otro=#{otro}, registrosenasag=#{registrosenasag}, latitud=#{latitud}, longitud=#{longitud}, nit=#{nit}, bancamovil=#{bancamovil}, fechaapertura=#{fechaapertura}, servicios=#{servicios}, capacidad=#{capacidad}, unidadmedida=#{unidadmedida}, razon=#{razon}, otrarazon=#{otrarazon}, familiar=#{familiar}, involucrado=#{involucrado}, otroinvolucrado=#{otroinvolucrado}, trabajadores=#{trabajadores}, participacion=#{participacion}, capacitacion=#{capacitacion}, zona=#{zona}, referencia=#{referencia}, transporte=#{transporte}, idusuario=#{idusuario} where idempresa=#{idempresa} ")
    void modificar(Empresas dato);

    @Select("select idempresa from empresas join representantes using(idrepresentante) join personas using(idpersona) join usuarios using(idpersona) where idusuario=#{idusuario}")
    Long idempresa(Long idusuario);

    @Delete("delete from empresas where idempresa=#{id}")
    void eliminar(Long id);

    @Select("SELECT idempresa, idsubrubro, idsubrubro as ifore2, idlocalidad, idlocalidad as ifore3, idrepresentante, idrepresentante as ifore1, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas join subrubros using(idsubrubro) join rubros using(idrubro) join localidades using(idlocalidad) join municipios using(idmunicipio) WHERE concat(empresa,' ',subrubro,' ',rubro,' ',localidad,' ',municipio) ilike '%'||#{buscar}||'%' ")
    @Results({
        @Result(property = "representante", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "subrubro", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ISubrubrosAod.dato")),
        @Result(property = "localidad", column = "ifore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod.dato"))
    })
    List<Empresas> datosrepo(String buscar);

    @Select("SELECT idempresa, idsubrubro, idlocalidad, idrepresentante, idasociacion, empresa, descripcion, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud FROM empresas where idproducto=#{id} ")
    Empresas datoproducto(Long id);

    @Select("SELECT idempresa, idsubrubro, idsubrubro as ifore2, idlocalidad, idlocalidad as ifore3, idrepresentante, idrepresentante as ifore1, idasociacion, empresa, descripcion, tipo, empresas.direccion, empresas.telefono, empresas.celular, empresas.correo, facebook, twitter, instagram, paginaweb, otro, registrosenasag, latitud, longitud from empresas join representantes using(idrepresentante)join personas using(idpersona) join usuarios using(idpersona) where idusuario=#{id} ")
    @Results({
        @Result(property = "representante", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod.dato")),
        @Result(property = "subrubro", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ISubrubrosAod.dato")),
        @Result(property = "localidad", column = "ifore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod.dato"))
    })
    Empresas perfilempresa(Long id);

}




