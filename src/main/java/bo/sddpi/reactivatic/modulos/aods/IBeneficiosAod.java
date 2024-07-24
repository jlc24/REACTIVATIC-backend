package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Beneficios;

@Mapper
public interface IBeneficiosAod {
    
    @Select("SELECT * FROM beneficios WHERE idbeneficios=#{id}")
    Beneficios dato(Long id);

    @Select("SELECT * FROM beneficios WHERE beneficio ilike '%'||#{buscar}||'%' ORDER BY created_at DESC LIMIT #{cantidad} OFFSET #{pagina}")
    List<Beneficios> buscar(String buscar, Integer cantidad, Integer pagina);

    @Select("SELECT count(idbeneficio) FROM beneficios WHERE beneficios ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idbeneficio, beneficio FROM beneficios WHERE estado=true ORDER BY beneficio")
    List<Beneficios> listar();

    @Insert("INSERT INTO beneficios (beneficio, participacion, fecha, lugar) VALUES (#{beneficio}, #{participacion}, #{fecha}, #{lugar})")
    @Options(useGeneratedKeys = true, keyProperty = "idbeneficio")
    void insertar(Beneficios beneficio);

    @Update("UPDATE beneficios SET beneficio=#{beneficio}, participacion=#{participacion}, fecha=#{fecha}, lugar=#{lugar} WHERE idbeneficio=#{idbeneficio}")
    void actualizar(Beneficios beneficio);

    @Update("UPDATE beneficios SET estado=#{estado} WHERE idbeneficio=#{idbeneficio}")
    void cambiarestado(Beneficios beneficio);

    @Delete("DELETE FROM beneficios WHERE idbeneficio=#{idbeneficio}")
    void eliminar(Beneficios beneficio);
}
