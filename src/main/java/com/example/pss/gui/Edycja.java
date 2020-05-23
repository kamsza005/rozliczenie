package com.example.pss.gui;

import com.example.pss.model.Role;
import com.example.pss.model.User;
import com.example.pss.service.UserService;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

public class Edycja extends VerticalLayout {

    private UserService userService;

    private User loggedUser;

    private Button editButton;
    private Button saveButton;

    private TextField nameTextField;
    private TextField lastNameTextField;
    private TextField emailTextField;
    private TextField companyNameTextField;
    private TextField companyAddressTextField;
    private TextField companyNipTextField;

    private CheckBox statusCheckbox;
    private DateField registrationDateField;

    @Autowired
    public Edycja(UserService userService, long userId) {
        this.userService = userService;
        this.loggedUser = userService.getUser(userId);

        setSpacing(true);

        HorizontalLayout dane=new HorizontalLayout();
        HorizontalLayout firma=new HorizontalLayout();

        nameTextField = new TextField("Imie", loggedUser.getName());
        lastNameTextField = new TextField("Nazwisko", loggedUser.getLastName());
        emailTextField = new TextField("Email", loggedUser.getEmail());
        companyNameTextField = new TextField("Nazwa firmy", loggedUser.getCompanyName());
        companyAddressTextField = new TextField("Adres firmy", loggedUser.getCompanyAddress());
        companyNipTextField = new TextField("NIP firmy", loggedUser.getCompanyNip());

        statusCheckbox = new CheckBox("Status", loggedUser.isStatus());
        statusCheckbox.setEnabled(false);

        registrationDateField = new DateField("Data rejestracji", loggedUser.getRegistrationDate());
        registrationDateField.setEnabled(false);

        CheckBoxGroup<String> rolesCheckBoxGroup =
                new CheckBoxGroup<>("Roles");
        rolesCheckBoxGroup.setItems(loggedUser.getRoles().stream()
                .map(Role::getRoleName));

        loggedUser.getRoles().stream()
                .map(Role::getRoleName)
                .forEach(s -> rolesCheckBoxGroup.select(s));

        rolesCheckBoxGroup.setEnabled(false);

        saveButton = new Button("Zapisz");
        saveButton.addClickListener(clickEvent -> {
            try {
                User user = userService.getUser(loggedUser.getId());

                user.setName(nameTextField.getValue());
                user.setLastName(lastNameTextField.getValue());
                user.setEmail(emailTextField.getValue());
                user.setCompanyName(companyNameTextField.getValue());
                user.setCompanyAddress(companyAddressTextField.getValue());
                user.setCompanyNip(companyNipTextField.getValue());

                userService.updateUser(user);
                Notification.show("Zapisano", "",
                        Notification.Type.HUMANIZED_MESSAGE);
            } catch (Exception e) {
                Notification.show("Blad!", "",
                        Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });

        dane.addComponents(nameTextField, lastNameTextField, emailTextField);
        firma.addComponents(companyNameTextField, companyAddressTextField, companyNipTextField);
        addComponents(dane,firma,saveButton);

    }



}

