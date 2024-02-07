package business.subStaff;

import business.subStaffI;
import data.UtilizadorDAO;

import java.util.Map;

public class SubStaffFacade implements subStaffI {
    private final Map<String, Utilizador> staff = UtilizadorDAO.getInstance();

    public SubStaffFacade() {
    }

    @Override
    public void registarConsulta(String codGestor) throws GestorInvalidoException {
        try {
            ((Gestor) staff.get(codGestor)).registarConsulta();
        } catch (ClassCastException | NullPointerException e) {
            throw new GestorInvalidoException(e);
        }
    }

}
