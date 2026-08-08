package app.factory;

import javafx.util.Callback;
import app.controller.MainViewController;
import app.controller.FormViewController;
import app.repository.TransactionDbRepository;
import app.service.TransactionService;
import app.exceptions.FactoryInstantiationException;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private final TransactionService service;

    public ControllerFactory() {
        this.service = new TransactionService(new TransactionDbRepository());
    }

    public ControllerFactory(TransactionService service) {
        this.service = service;
    }

    @Override
    public Object call(Class<?> param) {
        if (param == MainViewController.class) {
            return new MainViewController(service);
        }
        if (param == FormViewController.class) {
            return new FormViewController(service);
        }
        try {
            return param.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO FACTORY]: " + e.getMessage());
            throw new FactoryInstantiationException("Erro ao instanciar dinamicamente o controlador: " + param.getName(), e);
        }
    }
}
