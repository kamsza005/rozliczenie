package com.example.pss.gui;

import com.example.pss.model.User;
import com.example.pss.service.UserService;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ZmianaHasla extends VerticalLayout {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User loggedUser;


    @Autowired
    public ZmianaHasla(UserService userService, PasswordEncoder passwordEncoder, long userId) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.loggedUser = userService.getUser(userId);

        setSpacing(true);

        HorizontalLayout hasla=new HorizontalLayout();

        PasswordField currentPasswordField = new PasswordField("Obecne haslo");
        PasswordField newPasswordField = new PasswordField("Nowe haslo");
        PasswordField newPasswordField2 = new PasswordField("Powtorz nowe haslo");

        Button saveButton = new Button("Zapisz");
        saveButton.addClickListener(clickEvent -> {
            User user = userService.getUser(loggedUser.getId());

            if (passwordEncoder.matches(currentPasswordField.getValue(), user.getPassword())) {
                if (newPasswordField.getValue().equals(newPasswordField2.getValue())) {
                        user.setPassword(passwordEncoder.encode(newPasswordField.getValue()));
                        userService.updateUser(user);

                        Notification.show("Zmieniono haslo",
                                "",
                                Notification.Type.HUMANIZED_MESSAGE);

                        currentPasswordField.setValue("");
                        newPasswordField.setValue("");
                        newPasswordField2.setValue("");

                } else {
                    Notification.show("Hasla sie roznia", "",
                            Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show("Obecne haslo jest nieprawidlowe", "",
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        hasla.addComponents(currentPasswordField, newPasswordField, newPasswordField2);
        addComponents(hasla, saveButton);

    }
}
