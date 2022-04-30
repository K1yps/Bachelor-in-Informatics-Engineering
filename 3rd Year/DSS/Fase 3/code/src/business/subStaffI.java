package business;

import business.subStaff.GestorInvalidoException;

public interface subStaffI {
    public void registarConsulta(String codGestor) throws GestorInvalidoException;
}
