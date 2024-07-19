package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Submenus;

@Mapper
public interface ISubmenuAod {

    @Select("select idenlace as idsubmenu, enlace as nombre, ruta, iconoenlace as icono from usuarios join usuariosroles using(idusuario) join roles using(idrol) join enlacesroles using(idrol) join enlaces using(idenlace) where idusuario=#{idusuario} and idcategoria=#{idcategoria} group by idenlace, enlace, ruta, iconoenlace, orden order by orden; ")
    List<Submenus> datos(Long idusuario, Long idcategoria);

}