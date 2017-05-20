package pl.io4.controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.io4.NextGen;
import pl.io4.model.LoginMachine;
import pl.io4.model.Model;
import pl.io4.views.LoginView;
import pl.io4.views.SaleTransactionView;

/**
 * Created by kamil on 12.04.2017.
 */
public final class LoginController extends Controller {
    private String login;
    private String password;
    private LoginView view;
    private LoginMachine loginMachine;
    private boolean succes;
    private int count;

    static final int MAX_ERRORS_COUNT = 4;

    public LoginController(NextGen app) throws NoSuchElementException {
        super(app);
        view = getView();
        loginMachine = Model.getLoginMachine();
        count = 0;

        addButtonClickListener("submit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (count > MAX_ERRORS_COUNT) {
                    view.tooManyErrors();
                    return;
                }
                login = view.getLogin();
                password = view.getPassword();
                if (login == null || password == null) {
                    view.inputsNotFilled();
                    count++;
                } else {
                    succes = loginMachine.checkPassword(login, password);
                    if (succes) {
                        view.loginSucces();
                        app.switchTo(SaleTransactionView.class,
                                SaleTransactionController.class);
                    } else {
                        view.loginError();
                        count++;
                    }
                }
            }
        });

    }


}
