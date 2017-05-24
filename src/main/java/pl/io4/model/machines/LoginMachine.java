package pl.io4.model.machines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import pl.io4.model.Cachable;
import pl.io4.model.Model;
import pl.io4.model.entities.Employee;
import pl.io4.model.exceptions.IncorrectEmployeeDataException;
import pl.io4.model.exceptions.UnknownMethodException;

/**
 * Created by Zax37 on 22.03.2017.
 */

public final class LoginMachine extends Cachable {
    public enum LoginMethod {
        threeFirstLettersFiveLastPeselDigits,
        withPESEL, withEMAIL, withFullName
    }
    private EnumSet<LoginMethod> legalMethods = EnumSet.noneOf(LoginMethod.class);
    private Map<String, Employee> correctLogins;

    private static final int NAME_LETTERS_NB = 3;
    private static final int PESEL_DIGITS_NB = 5;

    public LoginMachine() {
        correctLogins = new HashMap<String, Employee>();
    }

    public static String createLogin(Employee employee, LoginMethod method)
    throws IncorrectEmployeeDataException, UnknownMethodException {
        switch (method) {
            case threeFirstLettersFiveLastPeselDigits:
                String firstName = employee.getFirstName(),
                        lastName = employee.getLastName(),
                        pesel = employee.getPesel();
                if (firstName.length() < NAME_LETTERS_NB
                        || lastName.length() < NAME_LETTERS_NB
                        || pesel.length() < PESEL_DIGITS_NB) {
                    throw new IncorrectEmployeeDataException("");
                }
                return firstName.substring(0, NAME_LETTERS_NB)
                        + lastName.substring(0, NAME_LETTERS_NB)
                        + pesel.substring(pesel.length() - PESEL_DIGITS_NB);
            case withEMAIL:
                return employee.getEmail();
            case withFullName:
                return employee.getFirstName() + " " + employee.getLastName();
            case withPESEL:
                return employee.getPesel();
            default:
                throw new UnknownMethodException("Unknown login method.");
        }
    }

    public void reloadLogins(EmployeesMachine employeesMachine)
    throws IncorrectEmployeeDataException, UnknownMethodException {
        correctLogins.clear();
        for (Employee employee : employeesMachine.getEmployees()) {
            for (LoginMethod method : legalMethods) {
                correctLogins.put(createLogin(employee, method), employee);
            }
        }
    }

    public boolean tryToLogIn(String login, String password) {
        if (correctLogins.containsKey(login)) {
            Employee employee = correctLogins.get(login);
            if (EncryptionMachine.getEmployeePasswordLocalHash(employee)
                .equals(EncryptionMachine.encryptLocal(password))) {
                Model.setCurrentlyLoggedInUser(employee);
                return true;
            }
        }
        return false;
    }

    public void addLoginMethod(LoginMethod loginMethod) {
        legalMethods.add(loginMethod);
    }

    public void setLoginMethod(LoginMethod loginMethod) {
        legalMethods.clear();
        legalMethods.add(loginMethod);
    }

    @Override
    public JSONObject cache() {
        JSONObject ret = new JSONObject();
        ret.put("legalMethods", Cachable.cache(legalMethods));
        return ret;
    }

    @Override
    public void load(JSONObject data) {
        legalMethods = Cachable.load(LoginMethod.class, data.getJSONArray("legalMethods"));
    }
}