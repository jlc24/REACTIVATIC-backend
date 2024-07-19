package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Bitacoras;

@Mapper
public interface IBitacorasAod {

    @Select("SELECT idbitacora, idusuario, bitacora, fechabitacora, horabitacora FROM bitacoras WHERE idusuario = #{idusuario} and bitacora ilike '%'||#{buscar}||'%' ORDER BY fechabitacora desc, horabitacora desc LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "usuario", column="idusuario", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IUsuariosAod.dato"))
    })
    List<Bitacoras> datos(Long idusuario, String buscar, Integer pagina, Integer cantidad);

    @Insert("insert into bitacoras (idusuario, bitacora) values (#{idusuario}, #{bitacora}) ")
    void adicionar(Bitacoras dato);

}


