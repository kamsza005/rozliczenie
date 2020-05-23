package com.example.pss.gui;

import com.example.pss.model.AutoCapacityEnum;
import com.example.pss.model.Delegation;
import com.example.pss.service.DelegationService;
import com.example.pss.service.UserService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.pss.model.TransportEnum;
import com.example.pss.model.User;

import java.time.LocalDate;
import java.util.List;

public class Delegacje extends VerticalLayout {

    private UserService userService;
    private DelegationService delegationService;
    private User loggedUser;

    @Autowired
    public Delegacje(UserService userService, DelegationService delegationService, long userId) {
        this.userService = userService;
        this.delegationService = delegationService;
        this.loggedUser = userService.getUser(userId);

        Grid<Delegation> delegationGrid = new Grid<>();
        delegationGrid.addColumn(Delegation::getDescription).setCaption("Opis");
        delegationGrid.addColumn(Delegation::getTravelDietAmount).setCaption("Dieta");
        delegationGrid.addColumn(Delegation::getBreakfastNumber).setCaption("Sniadanie");
        delegationGrid.addColumn(Delegation::getDinnerNumber).setCaption("Obiad");
        delegationGrid.addColumn(Delegation::getSupperNumber).setCaption("Kolacja");
        delegationGrid.addColumn(Delegation::getDateTimeStart).setCaption("Data rozpoczecia").setWidth(150);
        delegationGrid.addColumn(Delegation::getDateTimeStop).setCaption("Data zakonczenia").setWidth(150);

        delegationGrid.addColumn(delegation -> {
            String s = delegation.getTransportEnum().toString();
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            s = s.replace("_", " ");
            return s;
        }).setCaption("Transport");

        delegationGrid.addColumn(Delegation::getTicketPrice).setCaption("Cena biletu");

        delegationGrid.addColumn(delegation -> {
            String s = delegation.getAutoCapacityEnum().toString();
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            s = s.replace("_", " ");
            return s;
        }).setCaption("Auto capacity");

        delegationGrid.addColumn(Delegation::getKm).setCaption("Km");
        delegationGrid.addColumn(Delegation::getAccommodationPrice).setCaption("Zakwaterowanie").setWidth(50);
        delegationGrid.addColumn(Delegation::getOtherOutlayDesc).setCaption("Other outlay desc").setWidth(50);
        delegationGrid.addColumn(Delegation::getOtherOutlayPrice).setCaption("Other outlay").setWidth(50);

        delegationGrid.setWidth(1100, Unit.PIXELS);
        delegationGrid.setHeight(250,Unit.PIXELS);
        delegationGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        List<Delegation> delegationList = delegationService.getAllByUserId(loggedUser.getId());
        ListDataProvider<Delegation> provider = DataProvider.ofCollection(delegationList);
        provider.setSortOrder(Delegation::getDateTimeStart,
                SortDirection.ASCENDING);
        delegationGrid.setDataProvider(provider);

        HorizontalLayout actionsHorizontalLayout = new HorizontalLayout();

        Button addButton = new Button("Dodaj");
        addButton.setWidth("150");
        addButton.addClickListener(clickEvent -> {
                }
        );

        Button editButton = new Button("Zmien");
        editButton.setWidth("150");
        editButton.addClickListener(clickEvent -> {
                }
        );

        Button deleteButton = new Button("Usun");
        deleteButton.setWidth("150");
        deleteButton.addClickListener(clickEvent -> {
                    if (delegationGrid.getSelectedItems().size() == 1) {
                        Delegation delegation = delegationGrid.getSelectedItems().iterator().next();

                        if (LocalDate.now().isBefore(delegation.getDateTimeStart())) {
                            int indexDelegation = delegationList.indexOf(delegation);

                            delegation.removeUser(loggedUser);
                            delegationService.updateDelegation(delegation);
                            delegationService.deleteEmptyDelegation(delegation);

                            delegationList.remove(indexDelegation);
                            provider.refreshAll();

                            Notification.show("Usunieto pomyslnie", "",
                                    Notification.Type.HUMANIZED_MESSAGE);
                        } else {
                            Notification.show("Nie mozna usunac tej delegacji", "",
                                    Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        Notification.show("Wybierz delegacje", "",
                                Notification.Type.ERROR_MESSAGE);
                    }
                }
        );

        actionsHorizontalLayout.setMargin(true);
        actionsHorizontalLayout.setSpacing(true);
        actionsHorizontalLayout.addComponents(addButton, editButton, deleteButton);


        HorizontalLayout layout1 = new HorizontalLayout();

        HorizontalLayout layout2 = new HorizontalLayout();

        HorizontalLayout layout3 = new HorizontalLayout();

        HorizontalLayout layout4 = new HorizontalLayout();

        HorizontalLayout layout5 = new HorizontalLayout();

        HorizontalLayout layout6 = new HorizontalLayout();

        TextField descriptionTextField = new TextField("Opis");
        DateField dateTimeStartDateField = new DateField("Data rozpoczecia");
        DateField dateTimeStopDateField = new DateField("Data zakonczenia");
        TextField travelDietAmountTextField = new TextField("Dieta");
        TextField breakfastNumberTextField = new TextField("Sniadanie");
        TextField dinnerNumberTextField = new TextField("Obiad");
        TextField supperNumberTextField = new TextField("Kolacja");

        ComboBox<TransportEnum> transportTypeComboBox = new ComboBox<>("Transport");
        transportTypeComboBox.setItems(TransportEnum.values());

        TextField ticketPriceTextField = new TextField("Cena biletu");

        ComboBox<AutoCapacityEnum> autoCapacityComboBox = new ComboBox<>("Auto capacity");
        autoCapacityComboBox.setItems(AutoCapacityEnum.values());

        TextField kmTextField = new TextField("Km");
        TextField accommodationPriceTextField = new TextField("Cena zakwaterowania");
        TextField otherOutlayDescTextField = new TextField("Other outlay desc");
        TextField otherOutlayPriceTextField = new TextField("Other outlay price");

        delegationGrid.addSelectionListener(event -> {
            if (event.getAllSelectedItems().size() == 1) {
                Delegation delegation = event.getFirstSelectedItem().get();

                descriptionTextField.setValue(delegation.getDescription());
                dateTimeStartDateField.setValue(delegation.getDateTimeStart());
                dateTimeStopDateField.setValue(delegation.getDateTimeStop());
                travelDietAmountTextField.setValue(delegation.getTravelDietAmount() + "");
                breakfastNumberTextField.setValue(delegation.getBreakfastNumber() + "");
                dinnerNumberTextField.setValue(delegation.getDinnerNumber() + "");
                supperNumberTextField.setValue(delegation.getSupperNumber() + "");
                transportTypeComboBox.setValue(delegation.getTransportEnum());
                ticketPriceTextField.setValue(delegation.getTicketPrice() + "");
                autoCapacityComboBox.setValue(delegation.getAutoCapacityEnum());
                kmTextField.setValue(delegation.getKm() + "");
                accommodationPriceTextField.setValue(delegation.getAccommodationPrice() + "");
                otherOutlayDescTextField.setValue(delegation.getOtherOutlayDesc() + "");
                otherOutlayPriceTextField.setValue(delegation.getOtherOutlayPrice() + "");
            }
        });

        addButton.addClickListener(clickEvent -> {
                    try {
                        Delegation delegation = new Delegation(
                                descriptionTextField.getValue(),
                                dateTimeStartDateField.getValue(),
                                dateTimeStopDateField.getValue(),
                                Double.parseDouble(travelDietAmountTextField.getValue()),
                                Integer.parseInt(breakfastNumberTextField.getValue()),
                                Integer.parseInt(dinnerNumberTextField.getValue()),
                                Integer.parseInt(supperNumberTextField.getValue()),
                                transportTypeComboBox.getValue(),
                                Double.parseDouble(ticketPriceTextField.getValue()),
                                autoCapacityComboBox.getValue(),
                                Double.parseDouble(kmTextField.getValue()),
                                Double.parseDouble(accommodationPriceTextField.getValue()),
                                Double.parseDouble(otherOutlayDescTextField.getValue()),
                                Double.parseDouble(otherOutlayPriceTextField.getValue())
                        );

                        delegation.addUser(loggedUser);

                        delegation = delegationService.createDelegation(delegation);
                        delegationList.add(delegation);
                        provider.refreshAll();

                        Notification.show("Dodano pomyslnie", "",
                                Notification.Type.HUMANIZED_MESSAGE);
                    } catch (Exception e) {
                        Notification.show("Blad!", "",
                                Notification.Type.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
        );

        editButton.addClickListener(clickEvent -> {
                    if (delegationGrid.getSelectedItems().size() == 1) {
                        Delegation delegation = delegationGrid.getSelectedItems().iterator().next();

                        if (LocalDate.now().isBefore(delegation.getDateTimeStart())) {
                            try {
                                delegation.setDescription(descriptionTextField.getValue());
                                delegation.setDateTimeStart(dateTimeStartDateField.getValue());
                                delegation.setDateTimeStop(dateTimeStopDateField.getValue());
                                delegation.setTravelDietAmount(Double.parseDouble(travelDietAmountTextField.getValue()));
                                delegation.setBreakfastNumber(Integer.parseInt(breakfastNumberTextField.getValue()));
                                delegation.setDinnerNumber(Integer.parseInt(dinnerNumberTextField.getValue()));
                                delegation.setSupperNumber(Integer.parseInt(supperNumberTextField.getValue()));
                                delegation.setTransportEnum(transportTypeComboBox.getValue());
                                delegation.setTicketPrice(Double.parseDouble(ticketPriceTextField.getValue()));
                                delegation.setAutoCapacityEnum(autoCapacityComboBox.getValue());
                                delegation.setKm(Double.parseDouble(kmTextField.getValue()));
                                delegation.setAccommodationPrice(Double.parseDouble(accommodationPriceTextField.getValue()));
                                delegation.setOtherOutlayDesc(Double.parseDouble(otherOutlayDescTextField.getValue()));
                                delegation.setOtherOutlayPrice(Double.parseDouble(otherOutlayPriceTextField.getValue()));

                                delegationList.remove(delegation);
                                delegation = delegationService.updateDelegation(delegation);
                                delegationList.add(delegation);
                                provider.refreshAll();

                                Notification.show("Zmieniono pomyslnie", "",
                                        Notification.Type.HUMANIZED_MESSAGE);

                            } catch (Exception e) {
                                Notification.show("Blad!", "",
                                        Notification.Type.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                        } else {
                            Notification.show("Nie mozna edytowac tej delegacji!", "",
                                    Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        Notification.show("Wybierz delegacje", "",
                                Notification.Type.ERROR_MESSAGE);
                    }
                }
        );




        layout1.addComponents(descriptionTextField, travelDietAmountTextField);
        layout2.addComponents(dateTimeStartDateField, dateTimeStopDateField);
        layout3.addComponents(breakfastNumberTextField, dinnerNumberTextField, supperNumberTextField);
        layout4.addComponents(transportTypeComboBox, ticketPriceTextField);
        layout5.addComponents(autoCapacityComboBox, kmTextField);
        layout6.addComponents(accommodationPriceTextField, otherOutlayDescTextField, otherOutlayPriceTextField);

        addComponents(delegationGrid, layout1,layout2,layout3,layout4,layout5,layout6,actionsHorizontalLayout);
    }
}
