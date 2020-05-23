package com.example.pss.gui;

import com.example.pss.model.Role;
import com.example.pss.model.User;
import com.example.pss.service.RoleService;
import com.example.pss.service.UserService;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Title("Rozliczenie podróży i diet")
@SpringUI(path = "")
public class MainMenu extends UI {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private VerticalLayout layout;
    private HorizontalLayout dane;
    private HorizontalLayout email;
    private HorizontalLayout firma;
    private HorizontalLayout haslo;

    @Override
    protected void init(VaadinRequest request) {



        layout = new VerticalLayout();
        layout.setWidthFull();
        dane=new HorizontalLayout();
        email=new HorizontalLayout();
        firma=new HorizontalLayout();
        haslo=new HorizontalLayout();

        Label label1=new Label("Rozliczenie podróży i diet");


        Button loginButton = new Button("Zaloguj sie");
        loginButton.setWidth("200");
        loginButton.addClickListener(clickEvent -> {
                    getUI().getPage().setLocation("/login");
                }
        );

        Button signUpButton = new Button("Zarejestruj sie");
        signUpButton.setWidth("200");
        signUpButton.addClickListener(clickEvent -> {
                    layout.removeAllComponents();
                    loadSignUpView();
                }
        );

        layout.setSpacing(true);
        layout.setMargin(new MarginInfo(true,true,true,true));
        layout.addComponents(label1,loginButton, signUpButton);

        setContent(layout);
    }

    private void loadSignUpView() {
        TextField nameTextField = new TextField("Imie");
        TextField lastNameTextField = new TextField("Nazwisko");
        TextField emailTextField = new TextField("Email");
        TextField companyNameTextField = new TextField("Nazwa firmy");
        TextField companyAddressTextField = new TextField("Adres firmy");
        TextField companyNipTextField = new TextField("NIP firmy");

        PasswordField newPasswordField = new PasswordField("Haslo");
        PasswordField newPasswordField2 = new PasswordField("Powtorz haslo");

        Button registerButton = new Button("Register");
        registerButton.setWidth("200");
        registerButton.addClickListener(clickEvent -> {

                    try {
                        if (userService.findAllByEmail(emailTextField.getValue()).size() == 0) {

                            User user = new User(companyNameTextField.getValue(),
                                    companyAddressTextField.getValue(),
                                    companyNipTextField.getValue(),
                                    nameTextField.getValue(),
                                    lastNameTextField.getValue(),
                                    emailTextField.getValue(),
                                    newPasswordField.getValue()
                            );

                            if (newPasswordField.getValue().equals(newPasswordField2.getValue())) {
                                    user = userService.createUser(user);

                                    List<Role> roles = roleService.findAllByRoleName("Role1");
                                    if (roles.size() > 0) {
                                        Role role = roles.get(0);
                                        role.addUser(user);

                                        user = userService.updateUser(user);
                                    }
                                    Notification.show("Zarejestrowano",
                                            "",
                                            Notification.Type.HUMANIZED_MESSAGE);

                                    Authentication auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                    getUI().getPage().setLocation("/logged");

                            }else{
                                Notification.show("Hasla sie sa identyczne", "",
                                        Notification.Type.ERROR_MESSAGE);
                            }
                        }else{
                            Notification.show("Ten email jest zajety", "",
                                    Notification.Type.ERROR_MESSAGE);
                        }
                    }catch(Exception e) {
                        Notification.show("Blad!", "",
                                Notification.Type.ERROR_MESSAGE);
                        e.printStackTrace();
                    }

                }
        );

        dane.addComponents(nameTextField, lastNameTextField);
        email.addComponents(emailTextField);
        firma.addComponents(companyNameTextField,companyAddressTextField,companyNipTextField);
        haslo.addComponents(newPasswordField,newPasswordField2);

        layout.addComponents(dane,email,firma,haslo, registerButton);

        layout.setSpacing(true);
    }


}
