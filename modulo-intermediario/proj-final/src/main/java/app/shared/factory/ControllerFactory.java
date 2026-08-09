package app.shared.factory;

import javafx.util.Callback;
import app.application.controller.MainViewController;
import app.application.service.TransactionService;
import app.application.controller.FormViewController;
import app.repository.TransactionDAO;
import app.repository.TransactionDbRepository;
import app.shared.exceptions.FactoryInstantiationException;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private final TransactionService service;

    public ControllerFactory() {
        TransactionDAO dao = new TransactionDAO();
        TransactionDbRepository repository = new TransactionDbRepository(dao);
        this.service = new TransactionService(repository);
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
