package pl.io4.model.external_services;

/**
 * Created by Marcin on 10.05.2017.
 */
public class Auth1 {
    public static boolean authorize(int id, int pin, double price){
        return price<50;
    }
}
