package bo.sddpi.reactivatic.modulos.servicios;

import javax.mail.MessagingException;

import bo.sddpi.reactivatic.modulos.entidades.Correos;

public interface ICorreosServ {

    public boolean enviarcorreo(Correos correo) throws MessagingException;
}
