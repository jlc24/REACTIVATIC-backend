package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Beneficios;

@Mapper
public interface IBeneficiosAod {
    
    @Select("SELECT * FROM beneficios WHERE idbeneficio=#{id}")
    @Results({
        @Result(property = "tipobeneficio", column = "idtipobeneficio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposbeneficiosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
        @Result(property = "capacitador", column = "idcapacitador", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IUsuariosAod.dato")),
        @Result(property = "usuario", column = "idusuario", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IUsuariosAod.dato")),
    })
    Beneficios dato(Long id);

    @Select("<script>" +
                "SELECT * FROM beneficios " +
                "WHERE beneficio ilike '%'||#{buscar}||'%' " +
                "<if test='idusuario != null'> AND idusuario = #{idusuario} </if>" +
                "ORDER BY created_at DESC " +
                "LIMIT #{cantidad} OFFSET #{pagina}" +
            "</script>")
    @Results({
        @Result(property = "tipobeneficio", column = "idtipobeneficio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposbeneficiosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
    })
    List<Beneficios> buscar(Long idusuario, String buscar, Integer cantidad, Integer pagina);

    @Select("<script>" +
                "SELECT count(idbeneficio) FROM beneficios " +
                "WHERE beneficio ilike '%'||#{buscar}||'%' " +
                "<if test='idusuario != null'> AND idusuario = #{idusuario} </if>" +
            "</script>")
    Integer cantidad(Long idusuario, String buscar);

    @Select("SELECT * FROM beneficios WHERE estado=true ORDER BY beneficio")
    @Results({
        @Result(property = "tipobeneficio", column = "idtipobeneficio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposbeneficiosAod.dato")),
        @Result(property = "municipio", column = "idmunicipio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato")),
    })
    List<Beneficios> listar();

    @Insert("INSERT INTO beneficios (beneficio, descripcion, idtipobeneficio, idmunicipio, direccion, fechainicio, fechafin, idcapacitador, capacidad, idusuario) " +
            "VALUES (#{beneficio}, #{descripcion}, #{idtipobeneficio}, #{idmunicipio}, #{direccion}, #{fechainicio}, #{fechafin}, #{idcapacitador}, #{capacidad}, #{idusuario})")
    @Options(useGeneratedKeys = true, keyProperty = "idbeneficio")
    void insertar(Beneficios beneficio);

    @Update("UPDATE beneficios SET beneficio=#{beneficio}, descripcion=#{descripcion}, idtipobeneficio=#{idtipobeneficio}, idmunicipio=#{idmunicipio}, direccion=#{direccion}, fechainicio=#{fechainicio}, fechafin=#{fechafin}, idcapacitador=#{idcapacitador}, capacidad=#{capacidad} " +
            "WHERE idbeneficio=#{idbeneficio}")
    void actualizar(Beneficios beneficio);

    @Update("UPDATE beneficios SET estado=#{estado} WHERE idbeneficio=#{idbeneficio}")
    void cambiarestado(Beneficios beneficio);

    @Delete("DELETE FROM beneficios WHERE idbeneficio=#{idbeneficio}")
    void eliminar(Beneficios beneficio);

    @Select("SELECT * FROM beneficios " +
            "WHERE estado=true " +
            "AND idtipobeneficio=7 " +
            "AND CAST(fechainicio AS DATE) <= CAST(CURRENT_DATE AS DATE) " +
            "AND CAST(fechafin AS DATE) >= CAST(CURRENT_DATE AS DATE); ")
    Beneficios negocios();
}
