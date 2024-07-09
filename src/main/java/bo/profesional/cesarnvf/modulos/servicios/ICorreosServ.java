package bo.profesional.cesarnvf.modulos.servicios;

import javax.mail.MessagingException;

import bo.profesional.cesarnvf.modulos.entidades.Correos;

public interface ICorreosServ {

    public boolean enviarcorreo(Correos correo) throws MessagingException;
}
